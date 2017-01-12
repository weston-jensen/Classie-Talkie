package classieTalkie;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Assert;
import org.junit.Test;

public class ClassieTalkie_Tester {

	private final static Logger LOG = Logger.getLogger("ClassieTalkie_UTest_Log"); 
	private static FileHandler fh;

	@Test
	public void test() throws Exception {
		//Set up unit test log
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("ClassieTalkie_UTest_Log.txt");
		fh.setFormatter(new SimpleFormatter()); 
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		
		/*Correctly Working Tests*/
		Client_Thread_test();
		TCP_LocalAddr_tester("127.0.0.1",12001);
		
		/*InCorrectly Working Tests*/
		Client_Thread_Error_test();
	}
	
	/*
	 * Correct IP, Correct Port
	 * Wrong IP, Correct Port
	 * Wrong IP, Wrong Port
	 * COrrect IP, Wrong Port
	 * */
	//Test Client Interface
	public void Client_Thread_test() throws Exception
	{
		//this start program, start GUI
		Client_Thread ct = new Client_Thread();
		ct.start();
		
		//Fill out GUI information
		ct.getCG().setUserInput("Weston", "Jensen", "A01211187", "Password", "127.0.0.1");
		Thread.sleep(100);
		
		ct.getCG().setInputEnteredTrue();
		
		//Assert First name
		try{
			Assert.assertEquals("Weston",ct.getCG().getUserInput()[0]);
			LOG.info("PASS!!! First Name was returned correctly");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! First Name was returned incorrectly");
		}
		
		//Assert Last name
		try{
			Assert.assertEquals("Jensen",ct.getCG().getUserInput()[1]);
			LOG.info("PASS!!! Last Name was returned correctly");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! Last Name was returned incorrectly");
		}
		
		//Assert Anumber
		try{
			Assert.assertEquals("A01211187",ct.getCG().getUserInput()[2]);
			LOG.info("PASS!!! A-Number was returned correctly");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! A-Number was returned incorrectly");
		}
		
		//Assert Network Password
		try{
			Assert.assertEquals("Password",ct.getCG().getUserInput()[3]);
			LOG.info("PASS!!! Password was returned correctly");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! Password was returned incorrectly");
		}
		
		//Assert IP address
		try{
			Assert.assertEquals("127.0.0.1",ct.getCG().getUserInput()[4]);
			LOG.info("PASS!!! IP Addr was returned correctly");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! IP Addr was returned incorrectly");
		}
		
		
		UDP_Connection_tester(ct);
	}
	
	public void TCP_LocalAddr_tester(String addr, int port) throws IOException, InterruptedException
	{
		
		//this start program, start GUI
		Client_Thread ct = new Client_Thread();
		ct.start();

		//Fill out GUI information
		ct.getCG().setUserInput("Weston", "Jensen", "A01211187", "Password", "127.0.0.1");
		Thread.sleep(100);
		
		//Let Program know we entered info
		ct.getCG().setInputEnteredTrue();
	
		try{
			Assert.assertEquals("/127.0.0.1",ct.getAddr());
			LOG.info("PASS!!! InetAddress is correct");
		}catch(AssertionError e)
		{
			LOG.info("FAIL!!! InetAddress is incorrect");
		}
		ct.killThreads();
	}
	
	public void UDP_Connection_tester(Client_Thread ct) throws Exception
	{
	
		try{
			Assert.assertTrue(ct.getUdp_socket().isConnected());
			LOG.info("PASS!!! UDP Socket is connected");
		}catch(AssertionError e){
			LOG.info("ERROR!!! UDP Socket is not connected");
		}
		
		
	}
	
	public void Client_Thread_Error_test() throws Exception
	{
		//this start program, start GUI
		Client_Thread ct = new Client_Thread();
		ct.start();
		
		//Fill out GUI information
		ct.getCG().setUserInput("Weston", "Jensen", "A01211187", "Password", "127.0.0.50");
		Thread.sleep(100);
		
		ct.getCG().setInputEnteredTrue();
		
		
		//Assert IP address
		try{
			Assert.assertEquals("127.0.0.1",ct.getCG().getUserInput()[4]);
			LOG.info("FAIL!!! IP Addr was returned correctly, with incorrect IP");
		}catch(AssertionError e)
		{
			LOG.info("PASS!!! IP Addr was returned incorrectly, with incorrect IP");
		}
		
		
		//UDP_Connection_Error_tester(ct);
		
	}
	
	public void UDP_Connection_Error_tester(Client_Thread ct) throws Exception
	{
	
		try{
			Assert.assertTrue(ct.getUdp_socket().isConnected());
			LOG.info("FAIL!!! UDP Socket is connected, with incorrect IP");
		}catch(AssertionError e){
			LOG.info("ERROR!!! UDP Socket is not connected, with incorrect IP");
		}
		
		
	}

}
