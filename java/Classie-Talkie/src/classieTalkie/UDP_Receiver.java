package classieTalkie;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class UDP_Receiver extends Thread {
	private UDP_Server UDPserver;
	private volatile boolean running = true;

	AudioFormat format = getAudioFormat();
	InputStream is;
	
	

	public UDP_Receiver(UDP_Server server) {
		this.UDPserver = server;
	}

	public void run() {
		try {
			DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
			SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
			speaker.start();
			speaker.open(format);
			
			
			//FileOutputStream fos = new FileOutputStream("Audio.txt");
	
			byte[] buffer = new byte[44100];
			DatagramPacket dp;
			
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();

			while (running) {
				
				dp = new DatagramPacket(buffer, buffer.length);
				this.UDPserver.welcomeSocket.receive(dp);

				byte[] audioData = dp.getData();

				// Get an input stream on the byte array containing the data
				InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
				AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat,
						audioData.length / audioFormat.getFrameSize());
				
				try {
					int cnt;
					// Keep looping until the input read method returns -1 for empty stream
					while ((cnt = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
						if (cnt > 0) {
							// Write data to the internal buffer of the data
							// line where it will be delivered to the speaker.												
							sourceDataLine.write(buffer, 0, cnt); 
							
							//write audio to file for testing purposes
							//fos.write(buffer);
						}
					}
										
				} catch (Exception e) {
					// Block and wait for internal buffer of the data line to empty
					sourceDataLine.drain();
					sourceDataLine.close();
					//fos.close();
				}
				
				Thread.sleep(1);
			}
		} catch (Exception e) {
		}
		
		System.out.println("ending UDP Receiver thread");
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100.0F;
		//8000,11025,16000,22050,44100
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		//System.out.println("UDP Receiver is now false");
		this.currentThread();
		this.running = running;
	}
}
