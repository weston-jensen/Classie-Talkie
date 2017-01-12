package classieTalkie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.util.logging.Logger;

public class NM_Conversation extends Conversation {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private boolean running = true;
	private Message_Encoder encoder;
	private int prevMesgID = -1;
	private Key publicKey;
	private Key privateKey;
	private Key NM_publicKey;
	RSA_Encrypt rsa;

	public NM_Conversation(Socket socket, ObjectInputStream inFromClient, ObjectOutputStream outToClient, Registrar reg, ResourceManager rm, Message first, Key publicKey, Key privateKey) {
		super(socket, inFromClient, outToClient, reg, rm);
		encoder = new Message_Encoder();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.rsa = new RSA_Encrypt();
		LOG.info("->NM Conversation has been initialized");
		//writeObjectToClient(Messages_Received(first));
	}

	public void run()
	{
		while(running)
		{
			try {
				Message m = null;
				if(this.getInFromClient()!=null)
				{
					m = (Message)this.getInFromClient().readObject();
					if(m != null)//if received Message is not NULL
					{	
						Messages_Received(m);//Respond Accordingly
					}
				}
			Thread.sleep(10);
			
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				this.running = false;//if we lose connection kill Thread
				e.printStackTrace();
			}
		}

		//Thread is ending, kill connections
		try {
			
			this.getSocket().close();
			this.getOutToClient().close();
			this.getInFromClient().close();
			this.getRegistrar().setNM_Connected(false);//end NM connection
			//Main.killServer = true;
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void Messages_Received(Message m)
	{
		int status = -1;
		int decryptedID = -1;

		if(duplicateMessage((m.getMesgID()))==false)//check for duplicate messages
		{
			LOG.info("NM Message received with mesgID: "+m.getMesgID());

			switch((m.getMesgID()))
			{
			case 0:
				//AuthenticateManager reply from server
				/*
				 * check to see if a NM is already connected
				 * see if password matches
				 * if it does
				 * 		give Manager the id 8080
				 * 	if incorrect
				 * 		make Error message
				 */
				
				String decryptedPassword = rsa.decryptToString(this.privateKey,m.getServerPass());
				if(!this.getRegistrar().isNM_Connected())
				{
					if(decryptedPassword.equals(Main.Server_Password))
					{
						this.getRegistrar().setNM_Connected(true);
						writeObjectToClient(this.encoder.AuthenticateManager(m.getServerPass(),"Correct Password", this.rsa.encryptInt(this.NM_publicKey, Main.ManagerID), 1));
					}
					else
					{
						writeObjectToClient(this.encoder.AuthenticateManager(m.getServerPass(), "Incorrect Password", null, -1));
					}
				}
				else
				{
					writeObjectToClient(this.encoder.AuthenticateManager(m.getServerPass(), "Sorry Mate, a NM is already connected", null, -1));

				}

				status = -1;
				decryptedID = -1;

				break;
			case 1:
				//Create LAN
				/*
				 *First check to see if their ID matches the set manager ID
				 *if so, set the LAN
				 *else send error 
				 */
				decryptedID = this.rsa.decryptToInt(this.privateKey, m.getManagerID());
				if(decryptedID == Main.ManagerID)
				{
					this.getResourceManager().setLAN_Password(m.getLANPass());
					this.getRegistrar().setEndLAN(false);
					writeObjectToClient(this.encoder.CreateLAN((m.getManagerID()), this.getResourceManager().getLAN_Password(), 1));
					LOG.info("->New LAN created with password:"+m.getLANPass());
				}
				else
				{
					writeObjectToClient(this.encoder.CreateLAN((m.getManagerID()), m.getLANPass(), -1));
					LOG.info("->Error creating LAN");
				}
				break;
			case 2:
				//EndLAN
				decryptedID = this.rsa.decryptToInt(this.privateKey, m.getManagerID());
				if(decryptedID == Main.ManagerID)
				{
					this.getResourceManager().setLAN_Password(" ");//reset LAN
					this.getResourceManager().setLAN_Status(false);//Set status to false
					//this.getRegistrar().removeAllClientsFromRegistry();//remove all clients from registry
					this.getRegistrar().setEndLAN(true);

					LOG.info("->LAN Destroyed. Waiting for new LAN");
					status = 1;
					writeObjectToClient(this.encoder.EndLAN(m.getManagerID(),status));
				}
				else
				{
					status = -1;
					LOG.info("->Error Destroying LAN");
					writeObjectToClient(this.encoder.EndLAN(m.getManagerID(),status));
				}
				break;
			case 6:
				//Requested Analytic Data Response
				/*
				 * for now just return sucesss
				 */
				decryptedID = this.rsa.decryptToInt(this.privateKey, m.getManagerID());
				if(decryptedID == Main.ManagerID)
				{
					LOG.info("-> Sending Analytic data back to NM");
					writeObjectToClient(this.encoder.RequestAnalyticData((m.getManagerID()),
							1, this.getRegistrar().getRegistryAsString(),1));
				}
				else
				{
					writeObjectToClient(this.encoder.RequestAnalyticData((m.getManagerID()),
							1, this.getRegistrar().getRegistryAsString(),1));
				}
				break;
			case 8:
				//muteComm Received
				/*
				 * check ID
				 * force client to give up token
				 * set priority token to taken, by 8080
				 */
				decryptedID = this.rsa.decryptToInt(this.privateKey, m.getManagerID());
				if(decryptedID == Main.ManagerID)
				{
					//Set flag to tell clients to release token if anyone has it
					this.getResourceManager().setNMHasToken(true);
					
					Thread.yield();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					

					//wait for client to give up token
					while(this.getResourceManager().getPToken().isInUse()==true)
					{
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					LOG.info("-> !! NM now holds token, convo is muted!!!!");
				
					//Give priority token to NM
					this.getResourceManager().setPtoken(true, decryptedID);

					//Send Ack back to NM
					writeObjectToClient(this.encoder.MuteComm((m.getManagerID()), 1));
					LOG.info("->NM Is Muting the Conversation");

				}
				else//wrong NM id, error
				{
					writeObjectToClient(this.encoder.MuteComm((m.getManagerID()), -1));
					LOG.info("->NM Error Muting the Conversation");
				}
				break;
			case 9:
				//unMuteComm Ack Received
				/*
				 * check ID
				 * 
				 * set priority token to free, id = -1
				 */
				decryptedID = this.rsa.decryptToInt(this.privateKey, m.getManagerID());
				if(decryptedID == Main.ManagerID)
				{
					this.getResourceManager().setPtoken(false, -1);
					this.getResourceManager().setNMHasToken(false);
					writeObjectToClient(this.encoder.UnMuteComm((m.getManagerID()), 1));
					LOG.info("->NM Is Un-Muting the Conversation");
				}
				else
				{
					writeObjectToClient(this.encoder.UnMuteComm((m.getManagerID()), -1));
					LOG.info("->NM Error Un-Muting the Conversation");
				}
				break;
			case 10:
				/*
				 * NM is request the Server to destroy LAN
				 * Tell clients to end
				 * when registry is empty destroy LAN
				 */
				LOG.info("->NM wishes to shutdown LAN and Server");
				this.getResourceManager().setLAN_Password(" ");//reset LAN
				this.getResourceManager().setLAN_Status(false);//Set status to false
				this.getRegistrar().setNM_Connected(false);//end NM connection
				this.getRegistrar().setShutdown(true);//inform Clients Server is shutting down

				/*while(this.getRegistrar().getRegistrySize()>0)
				{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}//Don't want to block
				}
				*/

				//Reply Back that NM can now close
				writeObjectToClient(this.encoder.GracefulShutdown((m.getManagerID()), 1));

				LOG.info("->All Clients are disconnected, end NM");
				//End NM_Conversation Thread
				setRunning(false);
				Main.killServer = true;//tell Server it can't close
				break;
			case 11:
				LOG.info("-> Received NM Public Key");
				
				if(m.getPublicKey()!=null)
				{
					//Save NM public Key
					this.NM_publicKey = m.getPublicKey();
					
					//Send out public Key to NM
					writeObjectToClient(this.encoder.SwapPublicKeys(this.publicKey, 1));
				}
				else
				{
					//NM public key was null, send error message
					writeObjectToClient(this.encoder.SwapPublicKeys(null,-1));
				}
				
				break;
			}
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
			this.prevMesgID = currMesgID;
			return true;
		}
		else
		{
			this.prevMesgID = currMesgID;
			return false;
		}
	}





}
