/**
 * 
 */
package classieTalkie;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Weston
 *
 */
public class Client_Conversation_Tester {
	private final static Logger LOG = Logger.getLogger("Client_Conversation_LOG_Test"); 
	private static FileHandler fh;
	private Message_Encoder encoder;

	@Test
	public void test() throws SecurityException, IOException {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("Client_Conversation_LOG_Test.txt");
		fh.setFormatter(new SimpleFormatter()); 
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		this.encoder = new Message_Encoder();
		
		
		/*Start Up Client*/ 
		Client_Thread ct = new Client_Thread();
		ct.start();
		ct.getCG().setInputEnteredTrue();
		
		/*Test Cases*/
		//ConnectToLAN_Test_Fail(ct);
		ConnectToLAN_Test(ct);
		
		RequestToken_Test_Fail(ct);
		RequestToken_Test(ct);
		
		ReleaseToken_Test_Fail(ct);
		ReleaseToken_Test(ct);
		
		DisconnectFromLAN_Test_Fail(ct);
		DisconnectFromLAN_Test(ct);
		
		/*End Client*/
		ct.killThreads();
		//exit(1);
	}
	
	public void ConnectToLAN_Test_Fail(Client_Thread ct)
	{
		try {		
			//Fill out GUI information
			//wrong password
			ct.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Wrong-Password", -1, -1));
			
			//delay for Server to respond
			Thread.sleep(2000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertNotEquals(-1, ct.getClient_ID());
				LOG.info("!!FAIL -> Received Valid Client ID:"+ct.getClient_ID());
			}catch(AssertionError ae)
			{
				LOG.info("!!PASS -> Did not received Valid Client ID:"+ct.getClient_ID());
				fail("Did not get a valid clientID");
			}
			
			//clear the line
			ct.getSendQueue().add(this.encoder.ClearTheLine());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ConnectToLAN_Test(Client_Thread ct)
	{
		try {		
			//Fill out GUI information
			ct.getSendQueue().add(this.encoder.AuthenticateClient("Weston", "Jensen", "A01211187","Password", -1, -1));
			
			//delay for Server to respond
			Thread.sleep(1000);
			
			//Check to see if we received a valid clientID, ie > -1
			try{
				Assert.assertNotEquals(-1, ct.getClient_ID());
				LOG.info("!!PASS -> Received Valid Client ID:"+ct.getClient_ID());
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not received Valid Client ID:"+ct.getClient_ID());
				//fail("Did not get a valid clientID");
			}
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void RequestToken_Test_Fail(Client_Thread ct)
	{
		try {
			//Request Priority Token
			//with wrong ID
			ct.getSendQueue().add(this.encoder.RequestPriorityToken(-1, -1));
			
			//Wait for a response from Server
			Thread.sleep(10000);
			
			//Assert we have the token
			try{
				Assert.assertTrue(ct.isPriorityToken());
				LOG.info("!!FAIL -> Received Priority Token");
			}catch(AssertionError ae)
			{
				LOG.info("!!PASS -> Did not receivePriority Token");
				//fail("Did not get Priority Token");
			}
			
			//clear the line
			ct.getSendQueue().add(this.encoder.ClearTheLine());
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void RequestToken_Test(Client_Thread ct)
	{
		try {
			//Request Priority Token
			ct.getSendQueue().add(this.encoder.RequestPriorityToken(ct.getClient_ID(), -1));
			
			//Wait for a response from Server
			Thread.sleep(10000);
			
			//Assert we have the token
			try{
				Assert.assertTrue(ct.isPriorityToken());
				LOG.info("!!PASS -> Received Priority Token");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not receivePriority Token");
				//fail("Did not get Priority Token");
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ReleaseToken_Test_Fail(Client_Thread ct)
	{
		try {
			//Request Priority Token
			//with wrong ID
			ct.getSendQueue().add(this.encoder.ReleasePriorityToken(-1, -1));
			
			//Wait for a response from Server
			Thread.sleep(5000);
			
			
			//Assert we have the token
			try{
				Assert.assertTrue(!ct.isPriorityToken());
				LOG.info("!!FAIL -> Priority Token Released");
			}catch(AssertionError ae)
			{
				LOG.info("!!PASS -> Did not Release Priority Token");
				//fail("Did not release Priority Token");
			}
			
			//clear the line
			ct.getSendQueue().add(this.encoder.ClearTheLine());
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ReleaseToken_Test(Client_Thread ct)
	{
		try {
			//Request Priority Token
			ct.getSendQueue().add(this.encoder.ReleasePriorityToken(ct.getClient_ID(), -1));
			
			//Wait for a response from Server
			Thread.sleep(5000);
			
			
			//Assert we have the token
			try{
				Assert.assertTrue(!ct.isPriorityToken());
				LOG.info("!!PASS -> Priority Token Released");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not Release Priority Token");
				//fail("Did not release Priority Token");
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DisconnectFromLAN_Test_Fail(Client_Thread ct)
	{
		try {
			//Request to leave LAN
			//with wrong ID
			ct.getSendQueue().add(this.encoder.ClientDisconnect(-1, -1));
			
			//Wait for a response from Server
			Thread.sleep(10000);
			
			
			//Assert we have the token
			try{
				Assert.assertEquals(-1, ct.getClient_ID());
				LOG.info("!!FAIL -> Server Ack's to our request to leave LAN, shutting down");
			}catch(AssertionError ae)
			{
				LOG.info("!!PASS -> Server NAck'd to our request to leave LAN");
			}
			
			//clear the line
			ct.getSendQueue().add(this.encoder.ClearTheLine());
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DisconnectFromLAN_Test(Client_Thread ct)
	{
		try {
			//Request to leave LAN
			ct.getSendQueue().add(this.encoder.ClientDisconnect(ct.getClient_ID(), -1));
			
			//Wait for a response from Server
			Thread.sleep(1000);
			
			
			//Assert we have the token
			try{
				Assert.assertEquals(-1, ct.getClient_ID());
				LOG.info("!!PASS -> Server Ack's to our request to leave LAN, shutting down");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Server NAck'd to our request to leave LAN");
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
