package classieTalkie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.Queue;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Receive_TCP extends Thread{
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private Socket tcp_socket;
	private Queue<Message> receiveQueue;
	private ObjectInputStream inFromServer;
	private Message_Flag messageFlag;
	private volatile boolean running = true;
	
	public Receive_TCP(Socket socket, Queue<Message> rq, Message_Flag messageFlag) throws IOException
	{
		this.tcp_socket = socket;
		this.receiveQueue = rq;
		this.messageFlag = messageFlag;
		this.inFromServer  = new ObjectInputStream(this.tcp_socket.getInputStream());
	}
	
	public void run()
	{

		while(running)
		{
			try {
				if(!this.tcp_socket.isClosed())
				{
					Message m = null;
					if(inFromServer!=null)
					{
						m = (Message)inFromServer.readObject();
						if(m.mesgID >= 0)
						{
							if((this.messageFlag.getMesgID() == m.mesgID)&&(this.messageFlag.isFlagSet()==true))
							{
								this.messageFlag.setFlag(false);//received message pack
								LOG.info("->Received Ack from Server");	
							}
							
							this.receiveQueue.add(m);
						}
					}
				
				}
				Thread.sleep(1);
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				running = false;
				e.printStackTrace();
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}
