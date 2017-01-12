package classieTalkie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Receive_UDP extends Thread {
	private DatagramSocket socket;
	
	public Receive_UDP(DatagramSocket s) throws IOException
	{
		this.socket = s;
	}
	
	public void run()
	{
		while(true)
		{
			byte[] buffer = new byte[1024];
			
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
			try{
				this.socket.receive(dp);
				ByteBuffer bb = ByteBuffer.wrap(dp.getData());
				bb.order( ByteOrder.BIG_ENDIAN);
				
			} catch (IOException ex){
				System.err.println(ex);
			}
			Thread.yield();
		}
		
	}

}
