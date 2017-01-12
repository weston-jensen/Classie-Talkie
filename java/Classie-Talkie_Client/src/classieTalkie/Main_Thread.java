package classieTalkie;

import java.util.logging.Logger;

public class Main_Thread extends Thread {
	private GUI g;
	private final static Logger LOG = Logger.getLogger("Client_Log");
	
	public Main_Thread()
	{
		this.g = new GUI();
	}
	
	public void run()
	{
		//Wait to s
		while(g.getSelectionState()==false){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(g.getSelectionType()==1)
		{
			Client_Thread ct = new Client_Thread();
			ct.start();
			LOG.info("Client Interface Started");
		}
		else if(g.getSelectionType()==0)
		{
			NM_Thread nmt = new NM_Thread();
			nmt.start();
			LOG.info("Network Manager Started");
		}
	}

	public GUI getG() {
		return g;
	}

	public void setG(GUI g) {
		this.g = g;
	}
	
	
	
	
}
