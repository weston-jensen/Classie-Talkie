package classieTalkie;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Queue;
import java.util.logging.Logger;

public class Client_Conversation extends Conversation  {
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private Client_Thread ct;
	private int clientID = -1;
	private boolean running = true;
	
	public Client_Conversation(Queue<Message> sq, Queue<Message> rq, Client_Thread ct) {
		super(sq, rq);
		this.ct = ct;
		LOG.info("Starting Client Conversation");
	}
	
	public void run()
	{
		while(running)
		{
			try {
			Message in = new Message();
			
			//read receive queue
			if(!this.getReceiveQueue().isEmpty())
			{
				in = this.getReceiveQueue().remove();
				Messages_Received(in);	
			}
			
				Thread.sleep(1);
				
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
		}
	}
	
	private void Messages_Received(Message m)
	{	
		LOG.info("-> Client Received Message, with MesgID: " + m.getMesgID());
		int status = -1;
		int ID = -1;
		
		switch((m.getMesgID()))
		{
			case 2:
				ID = (m.getClientID());
				//Getting kicked off the LAN
				if((m.getMesgStatus())<0)//Received -1 Command that LAN is destroyed
				{
					//set our client ID
					this.ct.setClient_ID(-1);//reset clientID
					this.clientID = -1;
					this.ct.getCG().setStatus("Reconnect to LAN");
					this.ct.getCG().changeFrameTo_UserInfo();//change to start GUI
					status = 1;
					LOG.info("->Got Kicked off LAN");
				}
				else //There was an error
				{
					//Set Error Message in GUI window
					status = -1;
				}
				this.getSendQueue().add(this.getEncode().EndLAN(null,status));
			break;
			case 3:
				//AuthenticateClient reply from server
				if((m.getMesgStatus())>0)//If there is no error
				{
					if(this.clientID<0)
					{
						//set our client ID
						this.clientID = (m.getClientID());
						this.ct.setClient_ID((m.getClientID()));
						this.ct.getCG().changeFrameTo_PTT();
						LOG.info("->Received ClientID: "+m.getClientID());
					}
				}
				else //There was an error
				{
					//Set Error Message in GUI window
					this.ct.getCG().setStatus(m.getMessage());
					LOG.info("->There was an error, did not receive ID");
				}
				break;
			case 4:
				//Received Priority Token Request Response
				if((m.getMesgStatus())>0)//if we have received the token
				{
					this.ct.setPriorityToken(true);//set flag to true
					
					this.ct.getCG().setPttState("Push To Finish");
					this.ct.getCG().setPttToggle(0);
					this.ct.getCG().setPptMessage("Transmitting is Online.");//update GUI message
					this.ct.getCG().changeFrameTo_PTT();//update GUI
					
					//Set Up UDP Socket
					connectToUDP();
					
					LOG.info("->Received Priority Token");
				}
				else//We did not receive priority token
				{
					this.ct.setPriorityToken(false);//set flag to false
					this.ct.getCG().setPptMessage(m.getMessage());//update GUI message
					LOG.info("->Releasing Priority Token");
				}
				break;
			case 5:
				//Release Priority Token Response
				//-1 command, 1 ack
				
				this.ct.setPriorityToken(false);//set flag to false
				this.ct.getCG().setPttState("Push To Talk");
				this.ct.getCG().setPttToggle(1);
				this.ct.getCG().setPptMessage("Transmitting is Offline.");//Change GUI message
				this.ct.getCG().changeFrameTo_PTT();//update GUI
				
				//Server is commanding us to release token (-1)
				if((m.getMesgStatus())==-1)
				{	
					//Acknowledge that we have the released the token
					disconnectFromUDP();//Close UDP Socket
					LOG.info("->NM kicked us off audio transmit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					//reply with ack
					this.getSendQueue().add(this.getEncode().ReleasePriorityToken(this.ct.getClient_ID(),2));
				}
				break;
			case 7:
				//Client Disconnect Reply Received
				this.ct.setClient_ID(-1);
				disconnectFromUDP();//Close UDP Socket if it was open
				this.ct.killThreads();
				break;
			case 10:
				//GracefulShutdown Request Received
				this.getSendQueue().add(this.getEncode().GracefulShutdown(null, 1));//Tell server we are shutting down
				disconnectFromUDP();//Close UDP Socket if it was open
				this.ct.killThreads();
				this.running = false;
				break;
		}
	}
	
	public void connectToUDP()
	{
		try {
			DatagramSocket udp;
			udp = new DatagramSocket();
			int udp_port = 13002;
			
			udp.connect(this.ct.getAddr(),udp_port);
			this.ct.setUdp_socket(udp);
			
			Send_UDP send = new Send_UDP(this.ct.getUdp_socket(),this.ct.getAddr(),udp_port);
			this.ct.setUdp_sender(send);
			
			this.ct.getUdp_sender().start();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnectFromUDP()
	{
		if(this.ct.getUdp_socket().isConnected()){
			//Close UDP Socket, End Send_UDP Thread
			this.ct.getUdp_socket().close();
			this.ct.getUdp_sender().setRunning(false);
		}
	}

	
}
