package classieTalkie;

import java.security.Key;

public class Message_Encoder {
	
	public Message SwapPublicKeys(Key publicKey, int status)
	{
		Message m = new Message();
		m.setMesgID(11);
		m.setPublicKey(publicKey);
		m.setMesgStatus(status);
		return m;
	}
	
	public Message AuthenticateManager(byte[] password, String message, byte[] ID, int status)
	{
		Message m = new Message();
		m.setMesgID(0);
		m.setServerPass(password);
		m.setMessage(message);
		m.setManagerID(ID);
		m.setMesgStatus(status);
		
		return m;
	}
	
	public Message CreateLAN(byte[] ID, String netPassword, int status)
	{
		Message m = new Message();
		m.setMesgID(1);
		m.setManagerID(ID);
		m.setLANPass(netPassword);
		m.setMesgStatus(status);
		
		return m;
	}
	
	public Message EndLAN(byte[] ID, int status)
	{
		Message m = new Message();
		m.setMesgID((2));
		m.setManagerID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message AuthenticateClient(String fname, String lname, String anum, String pass, int ID, int status)
	{
		Message m = new Message();
		m.setMesgID((3));
		m.setaNum(anum);
		m.setFname(fname);
		m.setLname(lname);
		m.setLANPass(pass);
		m.setClientID((ID));
		m.setMesgStatus((status));
		return m;
	}
	
	public Message RequestPriorityToken(int ID, int status)
	{
		Message m = new Message();
		m.setMesgID((4));
		m.setClientID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message ReleasePriorityToken(int clientID, int status)
	{
		Message m = new Message();
		m.setMesgID((5));
		m.setClientID(clientID);
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message RequestAnalyticData(byte[] ID,  int numClients, String participation, int status)
	{
		Message m = new Message();
		m.setMesgID((6));
		m.setManagerID(ID);
		m.setClientNUM((numClients));
		m.setMesgStatus(status);
		m.setParticipation(participation);
		
		return m;
	}
	
	public Message ClientDisconnect(int ID, int status)
	{
		Message m = new Message();
		m.setMesgID((7));
		m.setClientID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message MuteComm(byte[] ID, int status)
	{
		Message m = new Message();
		m.setMesgID((8));
		m.setManagerID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message UnMuteComm(byte[] ID, int status)
	{
		Message m = new Message();
		m.setMesgID((9));
		m.setManagerID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	public Message GracefulShutdown(byte[] ID, int status)
	{
		Message m = new Message();
		m.setMesgID((10));
		m.setManagerID((ID));
		m.setMesgStatus((status));
		
		return m;
	}
	
	//the following is used only for testing purposes, not ever used in the program
	public Message ClearTheLine()
	{
		Message m = new Message();
		m.setMesgID(99);
		m.setMesgStatus(99);
		return m;
	}

}
