package classieTalkie;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class Client_Thread extends Thread {
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private Client_GUI cg;
	private String[] userInfo;
	private DatagramSocket udp_socket;
	private InetAddress addr;
	private int port;
	private volatile boolean running = true;
	private Send_UDP udp_sender;
	private Socket tcp_socket;
	private Send_TCP TCP_Sender;
	private Receive_TCP TCP_Receiver;
	private Client_Conversation client_convo;
	private volatile Message_Flag messageFlag;

	private int Client_ID = -1;
	private volatile boolean priorityToken = false;

	/* Send/Receive Queue */
	private Queue<Message> sendQueue;
	private Queue<Message> receiveQueue;

	public Client_Thread() {
		this.messageFlag = new Message_Flag(false,-1);
		this.sendQueue = new LinkedList<Message>();
		this.receiveQueue = new LinkedList<Message>();
		this.cg = new Client_GUI(this, this.sendQueue);
		this.Client_ID = -1;
		LOG.info("Client Thread Started");
	}

	public void run() {
		connectToServer();
		connectToLAN();

		while (this.running) {
			// in this loop we can check the client input
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(1);
	}

	private void connectToServer() {
		// Wait to receive User info
		boolean notConnected = true;
		while (notConnected) {
			if (cg.getInputEntered())// if user has entered info
			{
				userInfo = cg.getServerInfo();
				try// try to set up servers
				{
					if (beginComm() > 0)// if connection is made, break loop
					{
						notConnected = false;
					} else {
						cg.setInputEntered(false);// Reset flag
						cg.setStatus("Cannot Connect Via TCP");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Thread.yield();
		}

		cg.setInputEntered(false);// Reset flag
		cg.changeFrameTo_UserInfo();
	}

	private void connectToLAN() {
		// Wait until we are connected to move on
		while (Client_ID == -1)// while password is incorrect, or while clientID
		{
			cg.setStatus("Cannot Connect Via TCP");
			//LOG.info("Client Couldn't Connect to TCP Server");
			if (cg.getInputEntered())// if user has entered info
			{
				Message_Encoder me = new Message_Encoder();
				System.out.println(userInfo[2]+userInfo[3]);
				this.sendQueue.add(me.AuthenticateClient(userInfo[0], userInfo[1], userInfo[2], userInfo[3], -1, -1));
				cg.setInputEntered(false);// Reset flag
			}
			Thread.yield();
		}
	}

	private int beginComm() throws Exception {
		LOG.info("Initializing TCP Socket");
		
		String inetAddr = userInfo[0];
		System.out.println(inetAddr);
		this.port = 12001;
		this.addr = InetAddress.getByName(inetAddr);

		// TCP Socket
		this.tcp_socket = new Socket();
		this.tcp_socket.connect(new InetSocketAddress(addr, port), 1000);
		if (!this.tcp_socket.isConnected()) {
			// if not connected, return -1 as an error
			return -1;
		}
		//this.tcp_socket = new Socket(addr, tcp_port);
		this.TCP_Sender = new Send_TCP(this.tcp_socket, this.sendQueue, this.messageFlag);
		this.TCP_Receiver = new Receive_TCP(this.tcp_socket, this.receiveQueue, this.messageFlag);

		//Initialize Client Conversation
		this.client_convo = new Client_Conversation(this.sendQueue, this.receiveQueue, this);

		// Start Threads
		this.client_convo.start();
		this.TCP_Receiver.start();
		this.TCP_Sender.start();

		return 1;
	}

	public void killThreads() {
		try {
			this.tcp_socket.close();
			//this.udp_socket.close();
			this.TCP_Sender.setRunning(false);
			this.TCP_Receiver.setRunning(false);
			this.udp_sender.setRunning(false);
			this.setRunning(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info("->Killing ClientThread");
	}

	/* Getters/Setters */
	public boolean getRunning() {
		return this.running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public DatagramSocket getUdp_socket() {
		return udp_socket;
	}

	public void setUdp_socket(DatagramSocket udp_socket) {
		this.udp_socket = udp_socket;
	}

	public InetAddress getAddr() {
		return addr;
	}

	public void setAddr(InetAddress addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Client_GUI getCG() {
		return this.cg;
	}

	public void setCg(Client_GUI cg) {
		this.cg = cg;
	}

	public String[] getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String[] userInfo) {
		this.userInfo = userInfo;
	}

	public Send_UDP getUdp_sender() {
		return udp_sender;
	}

	public void setUdp_sender(Send_UDP udp_sender) {
		this.udp_sender = udp_sender;
	}

	public int getClient_ID() {
		return Client_ID;
	}

	public void setClient_ID(int client_ID) {
		Client_ID = client_ID;
	}

	public boolean isPriorityToken() {
		return priorityToken;
	}

	public void setPriorityToken(boolean priorityToken) {
		this.priorityToken = priorityToken;
	}

	public Send_TCP getTCP_Sender() {
		return TCP_Sender;
	}

	public void setTCP_Sender(Send_TCP tCP_Sender) {
		TCP_Sender = tCP_Sender;
	}

	public Receive_TCP getTCP_Receiver() {
		return TCP_Receiver;
	}

	public void setTCP_Receiver(Receive_TCP tCP_Receiver) {
		TCP_Receiver = tCP_Receiver;
	}

	public Message_Flag getMessageFlag() {
		return messageFlag;
	}

	public void setMessageFlag(Message_Flag messageFlag) {
		this.messageFlag = messageFlag;
	}

	public Queue<Message> getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(Queue<Message> sendQueue) {
		this.sendQueue = sendQueue;
	}

	public Queue<Message> getReceiveQueue() {
		return receiveQueue;
	}

	public void setReceiveQueue(Queue<Message> receiveQueue) {
		this.receiveQueue = receiveQueue;
	}
	
	

}
