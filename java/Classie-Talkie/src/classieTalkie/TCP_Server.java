package classieTalkie;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCP_Server extends Thread {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private ServerSocket serverSocket;
	private Registrar reg_instance;
	private ResourceManager rm_instance;
	private volatile boolean running = true;
	
	public TCP_Server(Registrar reg, ResourceManager rm) throws IOException
	{
		this.reg_instance = reg;
		this.rm_instance = rm;
		
		this.serverSocket = new ServerSocket(12001, 100, InetAddress.getLocalHost());
		this.serverSocket.setSoTimeout(1000);
		
		System.out.println(this.serverSocket.getLocalSocketAddress());
		LOG.info("TCP Server is up and running");
	}
	
	public void run()
	{
		while (running) {
			try 
			{
				Socket socket = serverSocket.accept();
				if(socket.isConnected())
				{
					LOG.info("Accepted socket connection");
					TCP_Conversation_Launcher tcl = new TCP_Conversation_Launcher(socket, this, this.reg_instance, this.rm_instance);
					tcl.start();
				}
				Thread.sleep(1);
			} 
			catch (IOException | InterruptedException e)
			{
				//e.printStackTrace();
				//running = false;
			}
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

}
