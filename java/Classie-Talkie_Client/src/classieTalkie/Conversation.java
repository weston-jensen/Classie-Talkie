package classieTalkie;

import java.util.Queue;

public class Conversation extends Thread {
	private Client_GUI gui;
	private Queue<String> sendQueue;
	private Queue<Message> receiveQueue;
	private Message_Encoder encode;
	private boolean running = true;
	
	public Conversation(Queue<String> sq, Queue<Message> rq)
	{
		this.sendQueue = sq;
		this.receiveQueue = rq;
		this.running = true;
		this.encode = new Message_Encoder();
	}
	
	/*
	 * Monitor send and receive queues
	 * read receive queue
	 * determine appropriate response
	 * put new message into send queue
	 * repeat
	 */

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

	public Message_Encoder getEncode() {
		return encode;
	}

	public void setEncode(Message_Encoder encode) {
		this.encode = encode;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
	
	
}
