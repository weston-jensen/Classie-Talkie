package classieTalkie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Mock_Server extends Thread{
	private ServerSocket serverSocket;
	private boolean running1 = true;
	private boolean running2 = true;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private Message_Encoder encoder;

	public volatile int messageReceivedCount =0;


	public Mock_Server() throws IOException
	{
		encoder = new Message_Encoder();
		this.serverSocket = new ServerSocket(12001, 100, InetAddress.getByName("localhost"));	
	}

	public void run()
	{
		//First Loop only gets a socket connection
		while (running1) 
		{
			//add timeout
			Socket socket;
			try {
				socket = serverSocket.accept();
				if(socket.isConnected())
				{
					this.inFromClient = new ObjectInputStream(socket.getInputStream());
					this.outToClient = new ObjectOutputStream(socket.getOutputStream());
					running1 = false;
				}
				Thread.sleep(10);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}


		//Second loop get input from Client or NM
		while(running2)
		{
			try {
				Message m = null;
				m = (Message)this.inFromClient.readObject();
				if(m != null)//if received Message is not NULL
				{	
					Messages_Received(m);//get response message
				}
				Thread.sleep(10);

			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				//this.running = false;//if we lose connection kill Thread
				e.printStackTrace();
			}
		}

	}

	private void Messages_Received(Message m)
	{		
		//test reliable send
		//test encryption
		int status = -1;
		int decryptedID = -1;

		switch((m.getMesgID()))
		{
		case 0:
			//AuthenticateManager reply from server
			String decryptedPassword = m.getServerPass();
			if(decryptedPassword.equals("CS-5200"))
			{
				writeObjectToClient(this.encoder.AuthenticateManager(m.getServerPass(),"Correct", 8080, 1));
			}
			else
			{
				writeObjectToClient(this.encoder.AuthenticateManager(m.getServerPass(), "Incorrect", -1, -1));
			}

			status = -1;
			decryptedID = -1;

			break;
		case 1:
			//Create LAN
			if(decryptedID == 8080)
			{
				writeObjectToClient(this.encoder.CreateLAN((m.getManagerID()), "Password", 1));
			}
			else
			{
				writeObjectToClient(this.encoder.CreateLAN((m.getManagerID()), m.getLANPass(), -1));
			}
			break;
		case 2:
			//End LAN
			decryptedID = m.getManagerID();
			if(decryptedID == 8080)
			{
				status = 1;
			}
			else
			{
				status = -1;
			}
			writeObjectToClient(this.encoder.EndLAN(m.getManagerID(),status));
			break;
		case 3:
			//AuthenticateClient 
			if(m.getMesgStatus()==-1)
			{
				//1
				this.messageReceivedCount++;
				writeObjectToClient(this.encoder.AuthenticateClient(m.getFname(), m.getLname(), m.getaNum(),m.getLANPass(), 1, 1));
			}
			else if(m.getMesgStatus()==-2)
			{
				//2
				if(this.messageReceivedCount==1)
				{
					this.messageReceivedCount++;
					writeObjectToClient(this.encoder.AuthenticateClient(m.getFname(), m.getLname(), m.getaNum(),m.getLANPass(), 1, 1));
				}
				else
				{
					this.messageReceivedCount++;
				}
			}
			else if(m.getMesgStatus()==-3)
			{
				//3
				if(this.messageReceivedCount==2)
				{
					this.messageReceivedCount++;
					writeObjectToClient(this.encoder.AuthenticateClient(m.getFname(), m.getLname(), m.getaNum(),m.getLANPass(), 1, 1));
				}
				else
				{
					this.messageReceivedCount++;
				}
			}
			else if(m.getMesgStatus()==-4)
			{
				//4+
				this.messageReceivedCount++;
			}
		case 4:
			//Request Priority Token
			writeObjectToClient(this.encoder.RequestPriorityToken(1,1));
			break;
		case 5:
			//Release Priority Token Response
			status = 1;
			writeObjectToClient(this.encoder.ReleasePriorityToken(1,1));
			break;
		case 6:
			//Requested Analytic Data Response
			decryptedID = m.getManagerID();
			if(decryptedID == 8080)
			{
				writeObjectToClient(this.encoder.RequestAnalyticData((m.getManagerID()),
						1, "Client Data",1));
			}
			else
			{
				writeObjectToClient(this.encoder.RequestAnalyticData((m.getManagerID()),
						1, "Client Data",1));
			}
			break;
		case 7:
			//Client Disconnect 
			status=1;
			writeObjectToClient(this.encoder.ClientDisconnect(1, status));
			break;
		case 8:
			//muteComm Ack Received
			decryptedID = m.getManagerID();
			if(decryptedID == 8080)
			{
				//Send Ack back to NM
				writeObjectToClient(this.encoder.MuteComm((m.getManagerID()), 1));
			}
			else//wrong NM id, error
			{
				writeObjectToClient(this.encoder.MuteComm((m.getManagerID()), -1));
			}
			break;
		case 9:
			//unMuteComm Ack Received
			decryptedID = m.getManagerID();
			if(decryptedID == 8080)
			{
				writeObjectToClient(this.encoder.UnMuteComm((m.getManagerID()), 1));
			}
			else
			{
				writeObjectToClient(this.encoder.UnMuteComm((m.getManagerID()), -1));
			}
			break;
		case 10:
			//Graceful shutdown
			//Reply Back that NM can now close
			writeObjectToClient(this.encoder.GracefulShutdown((m.getManagerID()), 1));
			break;
		case 11:
			//Swap public Keys

			//Save NM public Key
			

			//Send out public Key to NM
			

			break;
		}
	}

	public void writeObjectToClient(String output)
	{
		try {
			this.outToClient.writeObject(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
