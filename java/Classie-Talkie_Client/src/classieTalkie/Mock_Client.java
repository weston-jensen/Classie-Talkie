package classieTalkie;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Mock_Client extends Thread {
	private InetAddress addr;
	private Socket tcp_socket;
	private Send_TCP TCP_Sender;
	private Receive_TCP TCP_Receiver;
	private Queue<Message> sendQueue;
	private Queue<Message> receiveQueue;
	private volatile Message_Flag messageFlag;
	public boolean running = true;
	
	public Mock_Client() throws Exception
	{
		this.sendQueue = new LinkedList<Message>();
		this.receiveQueue = new LinkedList<Message>();
		this.messageFlag = new Message_Flag(false,-1);
		beginComm();
	}
	
	public void run()
	{
		while(running)
		{
			//keep socket open...
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int beginComm() throws Exception {
		this.addr = InetAddress.getByName("127.0.0.1");

		// TCP Socket
		this.tcp_socket = new Socket();
		this.tcp_socket.connect(new InetSocketAddress(addr, 12001), 1000);
		if (!this.tcp_socket.isConnected()) {
			// if not connected, return -1 as an error
			return -1;
		}
		//this.tcp_socket = new Socket(addr, tcp_port);
		this.TCP_Sender = new Send_TCP(this.tcp_socket, this.sendQueue, this.messageFlag);
		this.TCP_Receiver = new Receive_TCP(this.tcp_socket, this.receiveQueue, this.messageFlag);

		this.TCP_Receiver.start();
		this.TCP_Sender.start();

		return 1;
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
