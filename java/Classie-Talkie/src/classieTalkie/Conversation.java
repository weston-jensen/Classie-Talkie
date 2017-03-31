package classieTalkie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Conversation extends Thread{
	private Socket socket;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private Registrar registrar;
	private ResourceManager resourceManager;

	public Conversation(Socket socket, ObjectInputStream inFromClient, ObjectOutputStream outToClient, Registrar reg, ResourceManager rm)
	{
		this.socket = socket;
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.registrar = reg;
		this.resourceManager = rm;
		
	}
	
	public void writeObjectToClient(String output)
	{
		try {
			this.outToClient.writeObject(output);
			this.outToClient.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Registrar getRegistrar()
	{
		return this.registrar;
	}

	public void setRegistrar(Registrar registrar) {
		this.registrar = registrar;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


	public ObjectInputStream getInFromClient() {
		return inFromClient;
	}


	public void setInFromClient(ObjectInputStream inFromClient) {
		this.inFromClient = inFromClient;
	}


	public ObjectOutputStream getOutToClient() {
		return outToClient;
	}


	public void setOutToClient(ObjectOutputStream outToClient) {
		this.outToClient = outToClient;
	}
	
	
	
	
}
