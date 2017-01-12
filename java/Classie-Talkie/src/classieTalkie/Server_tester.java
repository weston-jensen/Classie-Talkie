package classieTalkie;

import static org.junit.Assert.fail;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Assert;
import org.junit.Test;

public class Server_tester {
	private final static Logger LOG = Logger.getLogger("UTest_Log");
	private static FileHandler fh;

	@Test
	public void test() throws Exception {
		// Set up unit test log
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("ClassieTalkie_Server_UTest_Log.txt");
		fh.setFormatter(new SimpleFormatter());
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		
		Main main = new Main();
		Thread.sleep(500);
		startUDP_StartTCP(main);
		gracefulShutdown_test(main);
	}

	public void startUDP_StartTCP(Main main) throws Exception {
		try {
			Assert.assertTrue(main.getTcp().isAlive());
			LOG.info("PASS!!! TCP Server was initialized");
		} catch (AssertionError e) {
			LOG.info("FAIL!!! TCP Server wasn't initialized");
			fail("FAIL");
		}
		
		try {
			Assert.assertTrue(main.getUdp().welcomeSocket.isBound());
			LOG.info("PASS!!! UDP Server was initialized");
		} catch (AssertionError e) {
			LOG.info("FAIL!!! UDP Server wasn't initialized");
		}	
	}
	
	public void gracefulShutdown_test(Main main) throws InterruptedException
	{
		main.killAllThreads();
	
		
		try{
			Assert.assertTrue(!main.getTcp().isAlive());
			LOG.info("PASS!!! TCP Server Thread has been shutdown");
		}catch (AssertionError e){
			LOG.info("FAIL!!! TCP Server Thread was not shutdown");
		}
		
		try{
			Assert.assertTrue(!main.getUdp_receiver().isAlive());
			LOG.info("PASS!!! UDP Receiver Thread has been shutdown");
		}catch (AssertionError e){
			LOG.info("FAIL!!! UDP Receiver Thread was not shutdown");
		}
		
		try{
			Assert.assertTrue(!main.getG().getThread().isAlive());
			LOG.info("PASS!!! GUI Thread has been shutdown");
		}catch (AssertionError e){
			LOG.info("FAIL!!! GUI Thread was not shutdown");
		}
		
		try{
			Assert.assertTrue(!main.getThread().isAlive());
			LOG.info("PASS!!! Main Thread has been shutdown");
		}catch (AssertionError e){
			LOG.info("FAIL!!! Main Thread was not shutdown");
		}
				
	}
	

}
