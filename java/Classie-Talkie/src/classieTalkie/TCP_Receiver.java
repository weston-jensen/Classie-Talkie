package classieTalkie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;

public class TCP_Receiver extends Thread{
	private Queue<Message> receiveQueue;
	private ObjectInputStream inFromClient;
	private boolean running = true;
	
	public TCP_Receiver(ObjectInputStream inFromClient, Queue<Message> rq)
	{
		this.inFromClient = inFromClient;
		this.receiveQueue = rq;
	}
	
	public void run()
	{
		while(running)
		{
			try {
				//need someway to determine if socket is still connected
				Message m = null;
				if(inFromClient !=null)
				{
					m = (Message)this.inFromClient.readObject();
					this.receiveQueue.add(m);
				}
			} catch (ClassNotFoundException | IOException e) {
				running = false; //kill thread
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	

}
