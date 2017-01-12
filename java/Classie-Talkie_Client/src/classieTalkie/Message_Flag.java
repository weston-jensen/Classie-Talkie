package classieTalkie;

public class Message_Flag {
	private volatile boolean flag;
	private volatile int mesgID;
	
	public Message_Flag(boolean state, int id)
	{
		this.flag = state;
		this.mesgID = id;
	}

	public boolean isFlagSet() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getMesgID() {
		return mesgID;
	}

	public void setMesgID(int mesgID) {
		this.mesgID = mesgID;
	}
	

}
