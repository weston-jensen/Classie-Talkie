package classieTalkie;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Queue;
import java.util.logging.Logger;

public class NM_Conversation extends Conversation {
	private NM_Thread nmt;
	private final static Logger LOG = Logger.getLogger("Client_Log");
	
	public NM_Conversation(Queue<String> sq, Queue<Message> rq, NM_Thread nmt) {
		super(sq, rq);
		this.nmt = nmt;
	}

	public void run()
	{
		while(this.isRunning())
		{
			Message in = new Message();
			if(!this.getReceiveQueue().isEmpty())
			{
				in = this.getReceiveQueue().remove();
				Messages_Received(in);	
			}
			Thread.yield();
		}
		this.nmt.killThreads();
	}

	private void Messages_Received(Message m)
	{
		LOG.info("->NM Received Message with Mesg ID: "+m.getMesgID());

		switch((m.getMesgID()))
		{
		case 0:
			//AuthenticateManager reply from server
			if((m.getMesgStatus())>0)//If there is no error
			{
				//set our Manager ID
				this.nmt.setManagerID(m.getManagerID());
				this.nmt.getNm_gui().setStatus("Valid Password, Welcome Professor");
				this.nmt.getNm_gui().changeFrame();
				LOG.info("->NM is now connected with ManagerID: "+this.nmt.getManagerID());
			}
			else //There was an error
			{
				//Set Error Message in GUI window
				this.nmt.getNm_gui().setStatus(m.getMessage());
				this.nmt.getNm_gui().changeToValidateNM();
				LOG.info("->NM Failed to Authenticate to Server because:"+m.getMessage());
			}
			break;
		case 1:
			//Create LAN
			if((m.getMesgStatus())>0)//Server received LAN password correctly
			{
				this.nmt.getNm_gui().changeToControlWindow();
				this.nmt.getNm_gui().setStatus("LAN has been initialized");
				this.nmt.setLAN_Started(1);
			}
			else//Server did not receive LAN password correctly
			{
				this.nmt.getNm_gui().setStatus("Error initializing LAN");
				this.nmt.getNm_gui().changeFrame();
			}
			break;
		case 2:
			//End LAN
			if((m.getMesgStatus())>0)
			{
				this.nmt.getNm_gui().changeFrame();//change to setup LAN window
			}
			else
			{
				this.nmt.getNm_gui().setStatus(m.getMessage());
			}

			break;
		case 6:
			//Requested Analytic Data Response
			if((m.getMesgStatus())>0)//Got data
			{

				try{
					PrintWriter out = new PrintWriter( "AnalyticData.txt" ); 
					System.out.println(m.getParticipation());
					out.println( m.getParticipation() );
					out.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				this.nmt.getNm_gui().setStatus("Data saved to AnalyticData.txt");
			}
			else//Error getting data
			{
				this.nmt.getNm_gui().setStatus("Error getting class data");
			}

			break;
		case 8:
			//muteComm Ack Received
			//Set Message in GUI window
			this.nmt.getNm_gui().setStatus(m.getMessage());
			this.nmt.setConvo_Muted(1);
			this.nmt.getNm_gui().setMute(-1);
			this.nmt.getNm_gui().setMuteStatus("UnMute Comm.");
			this.nmt.getNm_gui().changeToControlWindow();

			break;
		case 9:
			//unMuteComm Ack Received
			//Set Message in GUI window
			this.nmt.getNm_gui().setStatus(m.getMessage());
			this.nmt.setConvo_Muted(-1);
			this.nmt.getNm_gui().setMute(1);
			this.nmt.getNm_gui().setMuteStatus("Mute Comm.");
			this.nmt.getNm_gui().changeToControlWindow();
			break;
		case 10:
			/*
			 * Graceful shutdown command 
			 * Server ends comm with clients and sends back
			 * an ack when complete
			 */
			if((m.getMesgStatus())>0)//Got data
			{
				//KILL Order 66
				this.setRunning(false);
				LOG.info("->NM ending all Threads, Shutting Down");
			}
			else//Error getting data
			{
				this.nmt.getNm_gui().setStatus(m.getMessage());
			}
			break;
		case 11:
			/*
			 * Receive back public Key, allow to send password
			 * If error try transmitting again, display error message
			 */
			if((m.getMesgStatus())>0)//Got server public key
			{
				LOG.info("-> Received Server Public Key");
				this.nmt.getNm_gui().setStatus("Received Server Public Key");
				this.nmt.getNm_gui().setEncryptionReady(true);
				this.nmt.getNm_gui().changeToValidateNM();
			}
			else//Error public Key
			{
				//Send it again
				LOG.info("-> Failed to Receive Server Public Key");
				//this.getSendQueue().add(getEncode().SwapPublicKeys(this.nmt.getPublicKey(), -1));
				this.nmt.getNm_gui().setStatus("Error Receiving Server Public Key");
				this.nmt.getNm_gui().changeToValidateNM();
			}
			break;
		}
	}
}
