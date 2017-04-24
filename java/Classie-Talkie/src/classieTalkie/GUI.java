package classieTalkie;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI implements Runnable  {

	private JFrame frame;
	private String localHost = "";
	private volatile boolean running = true;
	

	private Main m;

	private Thread thread;

	public GUI(Main m)
	{
		this.m = m;
		frame = new JFrame();
		makeFrame();
		
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {

		while (running) {
			Thread.yield();
		}
		
		System.out.println("ending GUI thread");
		getFrame().dispose();
	}
	
	public void getIpFrame()
	{
		frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 414, 239);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JLabel classie_label = new JLabel("Classie-Talkie ");
        classie_label.setFont(new Font("SansSerif", Font.PLAIN, 24));
        classie_label.setBounds(120, 11, 156, 27);
        panel.add(classie_label);
        
        
        JLabel message = new JLabel("Enter IP address");
        message.setBackground(Color.RED);
        message.setFont(new Font("Tahoma", Font.PLAIN, 18));
        message.setBounds(100, 150, 300, 25);
        panel.add(message);
        
        
        frame.setVisible(true);
	}
	
	public void makeFrame()
	{
		frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 414, 239);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JLabel classie_label = new JLabel("Classie-Talkie Server");
        classie_label.setFont(new Font("", Font.PLAIN, 24));
        classie_label.setBounds(100, 11, 400, 27);
        classie_label.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(classie_label);
        
        String ip = "....";
       
        try {
        	ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
        frame.setTitle("Server IP Addr: "+ip);

        JLabel message = new JLabel(localHost);
        message.setBackground(Color.RED);
        message.setFont(new Font("", Font.PLAIN, 18));
        message.setBounds(100, 150, 300, 25);
        panel.add(message);
        
        JButton end = new JButton("End Server");
        end.setBounds(150, 200, 125, 25);
        end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getM().killAllThreads();
			}
		});
        panel.add(end);
        
        //ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
        frame.setVisible(true);
        
	}
	
	public void updateGUI()
	{
		this.frame.dispose();
		makeFrame();
		this.frame.repaint();
		this.frame.revalidate();
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
		updateGUI();
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public Main getM() {
		return m;
	}
	
	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

}
