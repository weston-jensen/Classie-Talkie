package classieTalkie;

public class UDP_Sender extends Thread {
	private UDP_Server UDPserver;
	
	public UDP_Sender(UDP_Server server){
		this.UDPserver = server;
	}

	public void run(){
		//we shouldn't be broadcasting anything really
		//but just incase
		  /*try{
			  while(true)
			  {				
					System.out.println("in run function");
						
					//put Bytes into Little Endian order
					ByteBuffer bb = ByteBuffer.wrap(send_packet);
					bb.order( ByteOrder.LITTLE_ENDIAN);
					
					DatagramPacket output = new DatagramPacket(bb.array(),bb.array().length,this.UDPserver.welcomeSocket.getInetAddress(),this.UDPserver.welcomeSocket.getPort());
					this.UDPserver.welcomeSocket.send(output);
					Thread.yield();
				 
			 }
		  }
		  catch (IOException ex){
			  System.err.println(ex);
		  }
		  */
	  }
}
