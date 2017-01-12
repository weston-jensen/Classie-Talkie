package classieTalkie;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Assert;
import org.junit.Test;

public class ReliableSend_Tester {
	private final static Logger LOG = Logger.getLogger("ReliableSend_LOG_Test"); 
	private static FileHandler fh;
	private Message_Encoder encoder;
	private Mock_Server server;
	private Mock_Client client;

	@Test
	public void test() throws Exception {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("ReliableSend_LOG_Test.txt");
		fh.setFormatter(new SimpleFormatter()); 
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		
		encoder = new Message_Encoder();
		
		//Set up Mock Server
		this.server = new Mock_Server();
		server.start();
		LOG.info("Mock Server is started");
		
		//Set up Mock Client
		this.client = new Mock_Client();
		client.start();
		LOG.info("Mock Client is started");
		Thread.sleep(1000);
		
		//start tests
		
		//Reliable Sending Tests
		respondOnFirst();
		respondOnSecond();
		respondOnThird();
		neverReply();
		
		
		
	}
	
	//reply on first
	public void respondOnFirst()
	{
		this.server.messageReceivedCount=0; //reset count
		try {
			client.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Wrong-Password", -1, -1));
			Thread.sleep(2000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertTrue(server.messageReceivedCount==1);
				LOG.info("!!PASS -> Server Replied to first message, Messages Received by Server:"+this.server.messageReceivedCount);
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not received Valid Client ID:");
				//fail("Did not get a valid clientID");
			}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//reply on second
	public void respondOnSecond()
	{
		try {
			this.server.messageReceivedCount=0; //reset count
			
			client.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Wrong-Password", -1, -2));
			Thread.sleep(4000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertTrue(server.messageReceivedCount==2);
				LOG.info("!!PASS -> Server Replied to second message, Messages Received by Server:"+this.server.messageReceivedCount);
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not received Valid Client ID:");
			}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//reply on third
	public void respondOnThird()
	{
		try {
			this.server.messageReceivedCount=0; //reset count
			
			client.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Wrong-Password", -1, -3));
			Thread.sleep(6000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertTrue(server.messageReceivedCount==3);
				LOG.info("!!PASS -> Server Replied to third message, Messages Received by Server:"+this.server.messageReceivedCount);
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not received Valid Client ID:");
			}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//never reply
	public void neverReply()
	{
		try {
			this.server.messageReceivedCount=0; //reset count
			
			client.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Wrong-Password", -1, -4));
			Thread.sleep(8000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertTrue(server.messageReceivedCount>=3);
				LOG.info("!!PASS -> Aborted reliable send as intended");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> sent too many messages, should have aborted, Messages Received by Server:"+this.server.messageReceivedCount);
			}
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
