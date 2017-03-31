package classieTalkie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Client_Conversation extends Conversation{
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private boolean running = true;
	private Message_Encoder encoder;
	private int ClientID = -99;
	private boolean endLAN_Ack = false;
	private boolean shutdown_Ack = false;
	private int prevMesgID = -1;
	private Queue<Message> receiveQueue;
	private TCP_Receiver receiver;

	public Client_Conversation(Socket socket, ObjectInputStream inFromClient, ObjectOutputStream outToClient, Registrar reg, ResourceManager rm, Message first) {
		super(socket, inFromClient, outToClient, reg, rm);
		encoder = new Message_Encoder();
		
		this.receiveQueue = new LinkedList<Message>();
		this.receiver = new TCP_Receiver(inFromClient,receiveQueue);
		this.receiver.start();
		
		LOG.info("->Client Conversation has been initialized!");
		Messages_Received(first);
	}

	public void run()
	{

		while(running)
		{
			try {
				//Check to see if NM is passing us a command
				checkForNMCommands();

				//If message was received, send correct response
				Message in = null;
				if(!this.receiveQueue.isEmpty())
				{
					in = this.receiveQueue.remove();
					Messages_Received(in);	
				}

				Thread.sleep(0);
			
			} catch (InterruptedException e) {
				if(this.getSocket().isClosed())
				{
					this.running = false;
				}
				e.printStackTrace();
			}
		}

		//Thread is ending, kill connections
		try {
			LOG.info("Killing Client Threads");
			this.receiver.setRunning(false);
			this.getSocket().close();
			this.getOutToClient().close();
			this.getInFromClient().close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void Messages_Received(Message m)
	{
		if(duplicateMessage(m.getMesgID())==false)//check for duplicate messages
		{
			if(this.getRegistrar().inRegistry(m.getClientID()))//check to see if client is already in registry
			{
				LOG.info("-> Client Message Received from Client: " + m.getClientID() + " with mesgID: "+m.getMesgID());
			}
			else
			{
				LOG.info("-> New Client Connected To Server, Generating ID, mesgID: "+m.getMesgID());
			}
			int ID = -1;
			int status = -1;

			switch(m.getMesgID())
			{
			case 2:
				//Ending LAN, making sure Client knows the LAN has ended
				if(m.getMesgStatus()>0)//Client ack to EndLAN
				{
					//Remove from registry
					this.getRegistrar().removeClientFromRegistry(ID);
					LOG.info("-> Client removed from Registry, size is now:"+this.getRegistrar().getRegistrySize());
					this.endLAN_Ack = false;
				}
				else//Client NAck to End LAN
				{
					//Resend EndLAN
					status =1;
					writeObjectToClient(this.encoder.EndLAN(-99, status));
				}
				break;
			case 3:
				//AuthenticateClient 
				/*
				 * compare password with LAN password, if correct
				 * add to registry and assign client an ID
				 * else return error message
				 */			
				if((m.getClientID()<0)&&(!this.getRegistrar().findByAnum(m.getaNum())))//doesn't already have an ID
				{
					if(m.getLANPass().equals(this.getResourceManager().getLAN_Password()))
					{
						ID = this.getRegistrar().addNewClient(m.getFname(),m.getLname(),m.getaNum());//Assign client an ID
						this.ClientID = ID;//set Global Variable for Client ID
						status = 1;
					}
				}
				else//Already has an ID
				{
					ID = this.ClientID;
					status = 1;
				}
				
				LOG.info("assigned client id of "+ID);
				writeObjectToClient(this.encoder.AuthenticateClient(m.getFname(), m.getLname(), m.getaNum(),m.getLANPass(), ID, status));

				break;
			case 4:
				//Request Priority Token
				/*
				 * check to see if Id exists in registry
				 * check to see if that can have the token
				 * if they can return a positive ack
				 * otherwise return a negative ack
				 */
				ID = m.getClientID();
				status = -1;

				if(this.getRegistrar().inRegistry(ID))
				{
					if((this.getResourceManager().getPToken().isInUse())==false)//if token is not in use
					{
						LOG.info("-> Giving Client "+m.getClientID()+" The Priority Token");
						this.getResourceManager().setPtoken(true, ID);//give token to client
						status = 1;
					}
					else
					{
						LOG.info("-> Not Giving Client "+m.getClientID()+" The Priority Token");
						status = -1;//token is in use, try again later
					}
				}
				writeObjectToClient(this.encoder.RequestPriorityToken(ID, status));

				break;
			case 5:
				//Release Priority Token Response
				/*
				 * check to see if ID exists in registry
				 * check to see who has the token, if this client still has it
				 * release it, otherwise do nothing. 
				 * 
				 * return ack
				 */
				ID = m.getClientID();
				status = 1;

				//Client is in registry
				if(this.getRegistrar().inRegistry(ID))
				{
					//Message is a request (-1)
					if(m.getMesgStatus()==-1)
					{
						//Ptoken is still in use
						if(this.getResourceManager().getPToken().isInUse()==true)
						{
							//If I happen to be holding the token
							if((this.getResourceManager().getPToken().getHolderID()) == ID)
							{
								//Release the token
								LOG.info("-> Releasing the priority token from Client");
								this.getResourceManager().setPtoken(false, -1);
							}
						}
						status = 1;
						writeObjectToClient(this.encoder.ReleasePriorityToken(ID, status));
					}
					else//Message is a reply (1)
					{
						LOG.info("-> Nothing to do, setting to null");
					}
				}
				break;
			case 7:
				//Client Disconnect 
				/*
				 * check to see if Id exists in registry
				 * if so release it and return positive ack
				 * 
				 * otherwise return negative ack
				 */
				ID = m.getClientID();
				status = -1;

				if(this.getRegistrar().inRegistry(ID))
				{
					this.getRegistrar().removeClientFromRegistry(ID);
					status = 1;
				}
				this.running = false;
				writeObjectToClient(this.encoder.ClientDisconnect(ID, status));
				break;
			case 10:
				//GracefulShutdown 
				/*
				 * received acknowledgment that client knows we are shutting down
				 */
				if(this.getRegistrar().inRegistry(this.ClientID))
				{
					/*
					 * Delete from registry
					 * Tell Thread to die
					 */
					this.getRegistrar().removeClientFromRegistry(this.ClientID);
					setRunning(false);
				}
				break;
			}
		}
	}

	private void checkForNMCommands()
	{
		if((this.getRegistrar().isEndLAN())&&(this.endLAN_Ack==false)&&(this.getRegistrar().inRegistry(this.ClientID)))
		{
			//Send Client a message that LAN is shutting down
			LOG.info("-> NM is asking us to kick clients off LAN");
			this.endLAN_Ack = true;//Received message from NM
			writeObjectToClient(this.encoder.EndLAN(-99, -1));			
		}

		if((this.getRegistrar().isShuttingdown())&&(this.shutdown_Ack==false))
		{
			//Send Client a message that Server is shutting down
			LOG.info("-> NM is asking us to kick clients off Server");
			this.shutdown_Ack = true;//Received message from NM
			writeObjectToClient(this.encoder.GracefulShutdown(-99, -1));		
		}
		
		if((this.getResourceManager().isNMHasToken())&&(this.getResourceManager().getPToken().getHolderID()==this.ClientID))
		{
			//If NM has token flag was set, and if the token holder ID matches my id 
			LOG.info("-> NM is forcefully taking over priorty token, kicking off Client");
			writeObjectToClient(this.encoder.ReleasePriorityToken(this.ClientID, -1));
			this.getResourceManager().setPtoken(false, -1);
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	private boolean duplicateMessage(int currMesgID)
	{
		if(currMesgID == this.prevMesgID)
		{
			this.prevMesgID = -5;//currMesgID;
			return true;
		}
		else
		{
			this.prevMesgID = currMesgID;
			return false;
		}
	}





}
