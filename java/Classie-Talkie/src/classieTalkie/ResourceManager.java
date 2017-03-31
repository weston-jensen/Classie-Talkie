package classieTalkie;

import java.util.logging.Logger;

public class ResourceManager {
	private final static Logger LOG = Logger.getLogger("Server_Log"); 
	private volatile String LAN_Password;
	private volatile boolean LAN_Status;// = false;;
	private volatile PriorityToken ptoken;
	private volatile boolean NMHasToken;// = false;
	
	public ResourceManager(String lan, boolean status, int ID)
	{
		setPtoken(status, ID);
		this.LAN_Status = false;
		this.NMHasToken = false;
		this.LAN_Password = "password";//= lan;
	}

	public String getLAN_Password() {
		return LAN_Password;
	}

	public void setLAN_Password(String lAN_Password) {
		LAN_Password = lAN_Password;
		LAN_Status = true;
	}

	public PriorityToken getPToken() {
		return ptoken;
	}

	public void setPtoken(boolean status, int ID) {
		PriorityToken temp = new PriorityToken();
		temp.setPriorityToken(status, ID);
		this.ptoken = temp;
	}

	//may need to add locking here.
	public boolean isLAN_Status() {
		return LAN_Status;
	}

	public void setLAN_Status(boolean lAN_Status) {
		LAN_Status = lAN_Status;
	}
	
	public boolean isNMHasToken() {
		return NMHasToken;
	}

	public void setNMHasToken(boolean nMHasToken) {
		NMHasToken = nMHasToken;
	}
	
	
	

	
}
