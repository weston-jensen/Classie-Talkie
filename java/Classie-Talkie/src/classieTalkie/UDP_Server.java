package classieTalkie;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

public class UDP_Server{
	public DatagramSocket welcomeSocket;
	
	public UDP_Server() throws IOException
	{
		 DatagramSocket s = new DatagramSocket(null);
	
		 InetSocketAddress address = null;
		 
		/*try{
			address = new InetSocketAddress(InetAddress.getByName("54.213.246.156"), 13002);
			s.bind(address);
		 }catch(BindException e){
			s = new DatagramSocket(null);
		 	address = new InetSocketAddress(InetAddress.getLocalHost(), 13002);
		 	s.bind(address);
		 }
		 */
		 address = new InetSocketAddress(InetAddress.getLocalHost(), 13002);
		 s.bind(address);
	
		 
		 //BindException, cannot assign requested address:cannot bind
	     
	     this.welcomeSocket = s;
	}
	
	
}
