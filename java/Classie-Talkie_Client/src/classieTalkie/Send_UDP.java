package classieTalkie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Send_UDP extends Thread{
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private DatagramSocket socket;
	private InetAddress addr;
	private int port;
	private volatile boolean running = true;
	
	public Send_UDP(DatagramSocket s, InetAddress addr, int port) throws IOException
	{
		this.socket = s;
		this.addr = addr;
		this.port = port;
	}
	
	public void run()
	{
		while(running)
		{
			runSender(this.socket);
		}
	}
	
AudioFormat format = getAudioFormat();
	
	private AudioFormat getAudioFormat() {
        float sampleRate = 44100.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
    }
	
	private void runSender(DatagramSocket socket){
        boolean sendAudio = true;
		try{
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class,format);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.open(format);
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
      
            mic.start();
            while(sendAudio)
            {
                int count = mic.read(tmpBuff,0,tmpBuff.length);
                System.out.println(count);
                
                
                Filter filter2 = new Filter(85,44100, Filter.PassType.Highpass,1);
                for (int i = 0; i < tmpBuff.length; i++)
                {
                    filter2.Update(tmpBuff[i]);
                    tmpBuff[i] = (byte) filter2.getValue();
                }
                
                
                Filter filter = new Filter(4500,44100, Filter.PassType.Lowpass,1);
                for (int i = 0; i < tmpBuff.length; i++)
                {
                    filter.Update(tmpBuff[i]);
                    tmpBuff[i] = (byte) filter.getValue();
                }
                /**/
                
                
                
                
                if ((count > 0)&&(!this.socket.isClosed()))
                {
                    DatagramPacket output = new DatagramPacket(tmpBuff,tmpBuff.length,this.addr,this.port);
					this.socket.send(output);
                }   

                if(this.socket.isClosed())
                {
                	sendAudio = false;//end loop
                	this.running = false;//end run loop
                	mic.close();//close mic
                	LOG.info("->Ending UDP Server Connection");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
		this.socket.close();
    }

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	

}
