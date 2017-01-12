package classieTalkie;

import java.net.Socket;

public class connectedClient {
	private int clientID;
	private String fname;
	private String lname;
	private String anum;
	boolean token = false;
	
	public connectedClient(int id, String fname, String lname, String anum)
	{
		this.clientID = id;
		this.fname = fname;
		this.lname = lname;
		this.anum = anum;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public boolean hasToken() {
		return token;
	}

	public void setToken(boolean token) {
		this.token = token;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getAnum() {
		return anum;
	}

	public void setAnum(String anum) {
		this.anum = anum;
	}
	
	
	
	
	
}
