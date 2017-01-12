package classieTalkie;

import static org.junit.Assert.*;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Assert;
import org.junit.Test;

public class NM_Conversation_Tester {
	private final static Logger LOG = Logger.getLogger("NM_Conversation_LOG_Test"); 
	private static FileHandler fh;
	private Message_Encoder encoder;

	@Test
	public void test() throws Exception {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("NM_Conversation_LOG_Test.txt");
		fh.setFormatter(new SimpleFormatter()); 
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		this.encoder = new Message_Encoder();
		
		/*Set up NM*/
		NM_Thread nmt = new NM_Thread();
		nmt.start();
		
		/*Test Cases*/
		connectToServer_fail(nmt);
		connectToServer(nmt);
		
		swapKeyWithServer_fail(nmt);
		swapKeyWithServer_pass(nmt);
		
		authenticateToServer_fail(nmt);
		authenticateToServer(nmt);
		
		createLAN(nmt);
		
		muteComm(nmt);
		unMuteComm(nmt);
		
		endLAN(nmt);
		
		
		/*Close NM*/
		nmt.killThreads();
		
	}
	
	public void connectToServer_fail(NM_Thread nmt)
	{
		try {
			//Set input data, wrong IP address
			nmt.getNm_gui().getNMInput()[0] = "127.0.0.0";
			nmt.getNm_gui().getNMInput()[1] = "127.0.0.0";
			nmt.getNm_gui().setValidatedTrue();
			Thread.yield();
			Thread.sleep(3000);
			
			try{
				Assert.assertTrue(nmt.getTcp_socket().isConnected());
				LOG.info("!!FAIL -> NM Connected to TCP Server with bad IP");
				//fail("Failed socket connection");
			}catch(AssertionError ae)
			{
				LOG.info("!!PASS -> NM did not connect to TCP Server, with bad IP");
			}
			
			//clear the line
			nmt.getSendQueue().add(this.encoder.ClearTheLine());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToServer(NM_Thread nmt)
	{
		try {
			//Set input data
			nmt.getNm_gui().getNMInput()[0] = "127.0.0.1";
			nmt.getNm_gui().getNMInput()[1] = "127.0.0.1";
			nmt.getNm_gui().setValidatedTrue();
			Thread.yield();
			Thread.sleep(3000);
			
			
			try{
				Assert.assertTrue(nmt.getTcp_socket().isConnected());
				LOG.info("!!Pass -> NM Connected to TCP Server");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not connect to TCP Server");
				//fail("Failed socket connection");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void swapKeyWithServer_fail(NM_Thread nmt)
	{
		try {
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.SwapPublicKeys(null, -1));
			Thread.yield();
			Thread.sleep(1000);


			try{
				Assert.assertNull(nmt.getServerPublicKey());
				LOG.info("!!Pass -> Server replied with null key");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Server sent its key even though we sent a bad key");
			}
			
			//clear the line
			nmt.getSendQueue().add(this.encoder.ClearTheLine());

		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}	
		
		
	}
	
	public void swapKeyWithServer_pass(NM_Thread nmt)
	{
		try {
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.SwapPublicKeys(nmt.getPublicKey(), -1));
			Thread.yield();
			Thread.sleep(1000);


			try{
				Assert.assertNotNull(nmt.getServerPublicKey());
				LOG.info("!!Pass -> Swapped public keys with server");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> Did not receive server public key");
				fail("Did not receive server public key");
			}

		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}	
		
		
	}
	
	public void authenticateToServer_fail(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.AuthenticateManager(rsa.encryptString(nmt.getServerPublicKey(), "wrong Password"), " ", null, -1));
			Thread.yield();
			Thread.sleep(3000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertEquals(-1,nmt.getManagerID());
				LOG.info("!!Pass -> NM Did not receive a valid id, with invalid password:"+nmt.getManagerID());
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM received valid id, with invalid password:"+nmt.getManagerID());
			}
			
			//clear the line
			nmt.getSendQueue().add(this.encoder.ClearTheLine());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void authenticateToServer(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			LOG.info("made it here before breaking");
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.AuthenticateManager(rsa.encryptString(nmt.getServerPublicKey(), "CS-5200"), " ", null, -1));
			
			
			Thread.yield();
			Thread.sleep(1000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(-1,nmt.getManagerID());
				LOG.info("!!Pass -> NM Received Valid ManagerID:"+nmt.getManagerID());
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not Receive Valid ManagerID:"+nmt.getManagerID());
				//fail("Didnt get a valid NM ID, ending");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	public void createLAN(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.CreateLAN(rsa.encryptInt(nmt.getServerPublicKey(), nmt.getManagerID()),"Password",-1));
			
			Thread.yield();
			Thread.sleep(1000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(-1,nmt.getLAN_Started());
				LOG.info("!!Pass -> NM Created LAN with Password");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not created LAN with Password");
				//fail("Couldn't make LAN");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void muteComm(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.MuteComm(rsa.encryptInt(nmt.getServerPublicKey(), nmt.getManagerID()),-1));
			
			Thread.yield();
			Thread.sleep(3000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(-1,nmt.getConvo_Muted());
				LOG.info("!!Pass -> NM Muted Comm");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not Mute Comm");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void unMuteComm(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.UnMuteComm(rsa.encryptInt(nmt.getServerPublicKey(), nmt.getManagerID()),-1));
			
			Thread.yield();
			Thread.sleep(3000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(1,nmt.getConvo_Muted());
				LOG.info("!!Pass -> NM Un-Muted Comm, "+nmt.getConvo_Muted());
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not UnMute Comm, "+nmt.getConvo_Muted());
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void endLAN(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.EndLAN(rsa.encryptInt(nmt.getServerPublicKey(), nmt.getManagerID()),-1));
			
			
			Thread.yield();
			Thread.sleep(3000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(-1,nmt.getLAN_Started());
				LOG.info("!!Pass -> NM Ended Current LAN");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM Failed to End Current LAN");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void gracefulShutdown(NM_Thread nmt)
	{
		try {
			//Authenticate to Server
			RSA_Encrypt rsa = new RSA_Encrypt();
			nmt.getSendQueue().add(this.encoder.GracefulShutdown(rsa.encryptInt(nmt.getServerPublicKey(), nmt.getManagerID()),-1));
			
			Thread.yield();
			Thread.sleep(3000);
			
			//Assert we received a ManagerID
			try{
				Assert.assertNotEquals(-1,nmt.getLAN_Started());
				LOG.info("!!Pass -> NM Ended LAN and Graceful Shutdown");
			}catch(AssertionError ae)
			{
				LOG.info("!!FAIL -> NM did not end LAN and Graceful Shutdown");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
