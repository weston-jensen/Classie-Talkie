package classieTalkie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;

public class TCP_Receiver extends Thread{
	private Queue<Message> receiveQueue;
	private ObjectInputStream inFromClient;
	private boolean running = true;
	private JSON_Decoder decode;
	
	public TCP_Receiver(ObjectInputStream inFromClient, Queue<Message> rq)
	{
		this.inFromClient = inFromClient;
		this.receiveQueue = rq;
		this.decode = new JSON_Decoder();
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
					
					String mesg = (String)inFromClient.readObject();
					System.out.println(mesg);
					m = decode.decodeMessage(mesg);//translate JSON to Message
					
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
