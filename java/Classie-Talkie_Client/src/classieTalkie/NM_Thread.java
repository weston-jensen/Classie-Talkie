package classieTalkie;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLSocket;

public class NM_Thread extends Thread {
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private NM_GUI nm_gui;
	private Socket tcp_socket;
	private Send_TCP TCP_Sender;
	private InetAddress addr;
	private Receive_TCP TCP_Receiver;	
	private int ManagerID;
	private NM_Conversation nm_convo;
	private String[] NM_input;
	private volatile boolean running = true;
	private Message_Flag messageFlag;
	private int LAN_Started = -1;
	private int Convo_Muted = -1;

	/*Send/Receive Queue*/
	private Queue<String> sendQueue;
	private Queue<Message> receiveQueue;
	
	public NM_Thread()
	{
		this.messageFlag = new Message_Flag(false,-1);
		this.sendQueue = new LinkedList<String>();
		this.receiveQueue = new LinkedList<Message>();
		nm_gui = new NM_GUI(this, this.sendQueue);
		this.ManagerID = -1;
		LOG.info("NM Thread Started");
	}

	public void run()
	{
		connectToServer();
		while (this.running) {
			// in this loop we can check the client input
			Thread.yield();
		}
		System.exit(1);
	}
	
	public void killThreads() {
		try {
			this.tcp_socket.close();
			this.TCP_Sender.setRunning(false);
			this.TCP_Receiver.setRunning(false);
			this.running = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("->Killing NM_Thread");
	}
	
	private void connectToServer() {
		// Wait to receive User info
		boolean notConnected = true;
		while (notConnected) {
			if (nm_gui.getValidated())// if user has entered info
			{
				this.NM_input = this.nm_gui.getNMInput();
				try// try to set up servers
				{
					if (beginComm() > 0)// if connection is made, break loop
					{
						notConnected = false;
					} else {
						nm_gui.setValidated(false);// Reset flag
						nm_gui.setStatus("Cannot Connect Via TCP");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		nm_gui.setValidated(false);// Reset flag
		nm_gui.changeToValidateNM();
	}
	
	private int beginComm() throws Exception
	{
		/*****************Set up sockets*****************/
		String inetAddr = NM_input[0];
		int tcp_port = 12001;
		this.addr = InetAddress.getByName(inetAddr);
		
		//TCP Socket
		this.tcp_socket = new Socket();
		this.tcp_socket.connect(new InetSocketAddress(addr, tcp_port), 1000);
		if (!this.tcp_socket.isConnected()) {
			// if not connected, return -1 as an error
			return -1;
		}
		
		// TCP Sender/Receiver
		this.TCP_Sender = new Send_TCP(this.tcp_socket,this.sendQueue, this.messageFlag);
		this.TCP_Receiver = new Receive_TCP(this.tcp_socket, this.receiveQueue, this.messageFlag);
		 
		//Initialize NM Conversation
		this.nm_convo = new NM_Conversation(this.sendQueue, this.receiveQueue, this);
		
		//Start Threads
		this.nm_convo.start();
		this.TCP_Sender.start();
		this.TCP_Receiver.start();
		
		return 1;
	}
	
	

	public int getManagerID() {
		return ManagerID;
	}

	public void setManagerID(int managerID) {
		ManagerID = managerID;
	}

	public NM_GUI getNm_gui() {
		return nm_gui;
	}

	public void setNm_gui(NM_GUI nm_gui) {
		this.nm_gui = nm_gui;
	}

	public Socket getTcp_socket() {
		return tcp_socket;
	}

	public void setTcp_socket(Socket tcp_socket) {
		this.tcp_socket = tcp_socket;
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

	public Queue<String> getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(Queue<String> sendQueue) {
		this.sendQueue = sendQueue;
	}

	public Queue<Message> getReceiveQueue() {
		return receiveQueue;
	}

	public void setReceiveQueue(Queue<Message> receiveQueue) {
		this.receiveQueue = receiveQueue;
	}

	public int getLAN_Started() {
		return LAN_Started;
	}

	public void setLAN_Started(int lAN_Started) {
		LAN_Started = lAN_Started;
	}

	public int getConvo_Muted() {
		return Convo_Muted;
	}

	public void setConvo_Muted(int convo_Muted) {
		Convo_Muted = convo_Muted;
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
