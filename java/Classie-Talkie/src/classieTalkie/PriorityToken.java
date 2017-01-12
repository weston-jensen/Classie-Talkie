package classieTalkie;

import java.util.logging.Logger;

public class PriorityToken {
	private final static Logger LOG = Logger.getLogger("Server_Log");
	private volatile boolean inUse = false;
	private volatile int holderID = -1;
	
	public PriorityToken()
	{
		
	}
	
	public void setPriorityToken(boolean status, int newID)
	{
		LOG.info("Changed priority token");
		inUse = status;
		holderID = newID;
	}

	public boolean isInUse() {
		return this.inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public int getHolderID() {
		return holderID;
	}

	public void setHolderID(int holderID) {
		this.holderID = holderID;
	}
	

}
