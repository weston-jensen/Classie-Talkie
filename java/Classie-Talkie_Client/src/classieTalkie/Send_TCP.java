package classieTalkie;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Send_TCP extends Thread{
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private Socket tcp_socket;
	private Queue<String> sendQueue;
	private ObjectOutputStream outToServer;
	private Message_Flag messageFlag;
	private volatile boolean running = true;

	
	public Send_TCP(Socket socket, Queue<String> sq, Message_Flag messageFlag) throws IOException
	{
		this.tcp_socket = socket;
		this.sendQueue = sq;
		this.messageFlag = messageFlag;
		this.outToServer = new ObjectOutputStream(this.tcp_socket.getOutputStream());
		
	}
	
	public void run()
	{
		while(running)
		{	
			if(!this.sendQueue.isEmpty())
			{
				try {
					
                    String m = sendQueue.remove();
                    outToServer.writeObject(m);
					outToServer.flush();
					
					/*
					Message m = new Message();
					m = sendQueue.remove();
					
					if(m.mesgStatus<0)
					{
						reliableSend(m);
					}
					else
					{
						outToServer.writeObject(m);
						outToServer.flush();
					}
					*/
					Thread.sleep(1);
				
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
		try {
			if(!this.tcp_socket.isClosed())
			{
				this.tcp_socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void reliableSend(Message m)
	{
		try {
			this.messageFlag.setFlag(true);
			this.messageFlag.setMesgID(m.getMesgID());
			outToServer.writeObject(m);
			outToServer.flush();
			
			LOG.info("->Sending Message:"+m.getMesgID());
			Thread.sleep(500);

			int iter = 0;
			while((this.messageFlag.isFlagSet()==true)&&(iter<3))
			{
				try {
					outToServer.writeObject(m);
					outToServer.flush();
					LOG.info("->Sending Message:"+m.getMesgID());
					iter++;
					Thread.yield();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(iter>=3)
			{
				LOG.info("->Message Failed, Never Received Ack From Server");
				this.messageFlag.setFlag(false);//reset flag
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}
