package classieTalkie;

import java.awt.EventQueue;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main implements Runnable {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private static FileHandler fh;
	private TCP_Server tcp;
	private UDP_Server udp;
	private UDP_Receiver udp_receiver;
	private GUI g;
	private Registrar registrar;
	private ResourceManager resouceManager;

	private volatile boolean running = true;

	//Global Variables
	static Vector<connectedClient> client_registry;
	static int ManagerID;// = 8080; 
	static String Server_Password = "CS-5200";
	static boolean killServer = false;

	private Thread thread;

	Main() throws Exception
	{
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
		fh = new FileHandler("Server_Log.txt");
	
		fh.setFormatter(new SimpleFormatter()); 
		LOG.addHandler(fh);
		LOG.setLevel(Level.INFO);
		
		this.registrar = new Registrar();
		this.resouceManager = new ResourceManager("Password", false, -1);//"P@ssword!!?>:(&**EjowYEHD"
		this.tcp = new TCP_Server(this.registrar,this.resouceManager);
		this.udp = new UDP_Server();
		this.udp_receiver = new UDP_Receiver(udp);
		
		thread = new Thread(this);
		thread.start();
		LOG.info("Server Started");
	}
	
	public void killAllThreads()
	{
		this.tcp.setRunning(false);
		this.udp_receiver.setRunning(false);
		this.g.setRunning(false);
		this.setRunning(false);
		
	}

	public TCP_Server getTcp() {
		return tcp;
	}

	public void setTcp(TCP_Server tcp) {
		this.tcp = tcp;
	}

	public UDP_Server getUdp() {
		return udp;
	}

	public void setUdp(UDP_Server udp) {
		this.udp = udp;
	}

	public UDP_Receiver getUdp_receiver() {
		return udp_receiver;
	}

	public void setUdp_receiver(UDP_Receiver udp_receiver) {
		this.udp_receiver = udp_receiver;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public GUI getG() {
		return g;
	}

	public void setG(GUI g) {
		this.g = g;
	}
	
	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main program = new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {
		System.out.println("starting main thread");
		
		//Start program threads
		tcp.start();
		udp_receiver.start();
		this.g = new GUI(this);
		g.setLocalHost(tcp.getServerSocket().getLocalSocketAddress().toString());
		
		while (running) {
			if(Main.killServer==true)
			{
				killAllThreads();
			}
			Thread.yield();
		}
		System.exit(1);
		
	}

}
