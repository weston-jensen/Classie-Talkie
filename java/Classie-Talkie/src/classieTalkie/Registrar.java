package classieTalkie;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/*
 * adds to clientRegistrar
 * adds to NMRegistrar
 * 
 * returns valid ID
 * 
 * checks to see if an ID exists inside the queue
 */
public class Registrar {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private volatile boolean endLAN = false;
	private volatile boolean shutdown = false;
	private volatile boolean NM_Connected = false;
	
	public Registrar()
	{
		Main.client_registry = new Vector<connectedClient>();
	}
	
	/**
	 * Adds a new Client to Registry, returns ID
	 * @return Valid Client ID
	 */
	public int addNewClient(String fname, String lname, String anum)
	{
		//lock to ensure no other thread is adding a client at the same time
		//Lock lock = new ReentrantLock();
		//lock.lock();
		
		int id = genClientID();
		connectedClient client = new connectedClient(id,fname,lname,anum);
		System.out.println(fname);
		Main.client_registry.addElement(client);
		
		//lock.unlock();
		
		return id;
	}
	
	/**
	 * Find size of current directory, the new client id
	 * will be size++
	 * @return clientID
	 */
	public int genClientID()
	{	
		return Main.client_registry.size()+1;
	}
	
	/**
	 * Check to see if Client's ID is in the registry
	 * @param id Client's ID
	 * @return boolean
	 */
	public boolean inRegistry(int id)
	{
		boolean result = false;
		LOG.info("Searching the Registry, size = "+Main.client_registry.size());
		//Lock to ensure safety 
		Lock lock = new ReentrantLock();
		lock.lock();
		for(int i=0;i<Main.client_registry.size();i++)
		{
			if(Main.client_registry.get(i).getClientID()==id)
			{
				LOG.info("Match!!! Client #"+ Integer.toString(Main.client_registry.get(i).getClientID()));
				result = true;
				return result;
			}
		}
		lock.unlock();
		
		return result;
	}
	
	/**
	 * Get size of the registry
	 * @return int of size
	 */
	public int getRegistrySize()
	{
		return Main.client_registry.size();
	}
	
	public String getRegistryAsString()
	{
		String details = " ";
		for(int i=0;i<Main.client_registry.size();i++)
		{
			String temp = Integer.toString(Main.client_registry.get(i).getClientID()) + " " +
					Main.client_registry.get(i).getFname() + " " +
					Main.client_registry.get(i).getLname() + " " +
					Main.client_registry.get(i).getAnum() + " **";
			details = details + temp;
		}
		System.out.println(details);
		return details;
	}
	
	public void removeClientFromRegistry(int id)
	{
		//Lock to ensure safety 
		Lock lock = new ReentrantLock();
		lock.lock();
		for(int i=0;i<Main.client_registry.size();i++)
		{
			if(Main.client_registry.get(i).getClientID()==id)
			{
				Main.client_registry.remove(i);
				continue;
			}
		}
		lock.unlock();
	}
	
	public void removeAllClientsFromRegistry()
	{
		Lock lock = new ReentrantLock();
		lock.lock();
		
		Main.client_registry.clear();
		
		lock.unlock();
	}
	
	/**
	 * Add new Network Manager
	 * @return
	 */
	public int addNewNM()
	{
		int id = genNMID();
		Main.ManagerID = id;
		
		return id;
	}
	
	/**
	 * Generate a new NM id
	 */
	public int genNMID()
	{	
		//temp Manager ID
		return 8080;
	}
	
	public boolean isNM(int id)
	{
		boolean result = false;
		
		//shouldn't need to lock, but will just in case
		Lock lock = new ReentrantLock();
		lock.lock();
		
		if(Main.ManagerID == id)
		{
			result = true;
		}
		return result;
	}

	public boolean isEndLAN() {
		return endLAN;
	}

	public void setEndLAN(boolean endLAN) {
		this.endLAN = endLAN;
	}

	public boolean isShuttingdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public boolean isNM_Connected() {
		return NM_Connected;
	}

	public void setNM_Connected(boolean nM_Connected) {
		NM_Connected = nM_Connected;
	}
	
	

	
	
	
	


}
