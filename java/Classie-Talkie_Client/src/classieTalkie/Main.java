package classieTalkie;

import java.awt.EventQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	static boolean haveToken = true;
	private final static Logger LOG = Logger.getLogger("Client_Log"); 
	private static FileHandler fh;
	
	Main() throws Exception
	{
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		//fh = new FileHandler("Client_Log.txt");
		//fh.setFormatter(new SimpleFormatter()); 
		//LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		Main_Thread mt = new Main_Thread();
		mt.start();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					Main program = new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
