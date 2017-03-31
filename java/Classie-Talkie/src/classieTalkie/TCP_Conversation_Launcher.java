package classieTalkie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TCP_Conversation_Launcher extends Thread {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	// TCP Components
	private Socket socket;
	private TCP_Server tcp;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private int clientID;
	private Registrar reg_instance;
	private ResourceManager rm_instance;
	private Key privateKey;
	private Key publicKey;

	// boolean variable to check that client is running or not
	private volatile boolean running = true;

	public TCP_Conversation_Launcher(Socket socket, TCP_Server tcp, Registrar reg, ResourceManager rm) {
		try {
			this.reg_instance = reg;
			this.rm_instance = rm;
			this.socket = socket;
			this.socket.setKeepAlive(true);
			this.tcp = tcp;
			this.inFromClient = new ObjectInputStream(socket.getInputStream());
			this.outToClient = new ObjectOutputStream(socket.getOutputStream());
			generateRSAKeys();
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private void generateRSAKeys()
	{
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			keygen.initialize(2048);
			KeyPair kp = keygen.genKeyPair();
			this.publicKey = kp.getPublic();
			this.privateKey = kp.getPrivate();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean wait = true;
		JSON_Decoder decode = new JSON_Decoder();
		try {
			while (wait) 
			{	
				LOG.info("-> TCP Conversation is Running");
				
				//Message m = null;
				//m = (Message)inFromClient.readObject();
				String mesg = (String)inFromClient.readObject();
				System.out.println(mesg);
				Message m = decode.decodeMessage(mesg);
				
				if(m != null)
				{	
					LOG.info("-> TCP Server Received Incoming Connection with Mesg ID: "+m.mesgID);
					
					if((m.mesgID)==0)
					{
						//Network Manager Connecting
						LOG.info("-> New Network Manager Conversation Started");
						NM_Conversation nmc = new NM_Conversation(this.socket, this.inFromClient,this.outToClient, this.reg_instance, this.rm_instance, m);
						nmc.run();
						wait = false;
					}
					else if((m.mesgID)==3)					{
						//Client Connecting
						LOG.info("-> New Client Conversation Started");
						Client_Conversation cc = new Client_Conversation(this.socket,this.inFromClient,this.outToClient, this.reg_instance, this.rm_instance, m);
						cc.run();
						wait = false;
					}
					else
					{
						//Error
						//to do
						//make error message to return
					}
					
				}
				Thread.sleep(1);
				
			}

			// close all connections
			LOG.info("-> Ending TCP Conversation Launcher Thread");
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
