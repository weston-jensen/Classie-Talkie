package classieTalkie;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NM_GUI {
	private JFrame frame;
	private String[] NM_input;
	private boolean validated = false;
	private String status = "...";
	private String muteStatus = "Mute Comm.";
	private int mute = 1;
	private Message_Encoder encoder;
	private Queue<String> sendQueue;
	private NM_Thread nmt;
	private boolean encryptionReady = false;
	private RSA_Encrypt rsa;
	
	public NM_GUI(NM_Thread nmt, Queue<String> sendQueue)
	{
		this.NM_input = new String[2];
		this.encoder = new Message_Encoder();
		this.rsa = new RSA_Encrypt();
		this.nmt = nmt;
		this.sendQueue = sendQueue;
		this.frame = new JFrame();
		Connect_To_Server_GUI();
		this.frame.setVisible(true);
	}
	
	public void Connect_To_Server_GUI()
	{
		frame = new JFrame();
        frame.setBounds(100, 100, 551, 347);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        //ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
       
        JPanel panel = new JPanel();
        panel.setBounds(130, 80, 400, 300);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JLabel window_label = new JLabel("Connect To Server");
		window_label.setFont(new Font("", Font.PLAIN, 24));
		window_label.setBounds(170, 10, 300, 25);
		frame.add(window_label);
        
        JLabel serverIP_label = new JLabel("Server IP");
        serverIP_label.setFont(new Font("", Font.PLAIN, 14));
        serverIP_label.setBounds(10, 10, 125, 25);
        panel.add(serverIP_label);
       
        JTextField serverIP_tf = new JTextField();
        serverIP_tf.setBounds(140, 10, 125, 25);
        panel.add(serverIP_tf);
        serverIP_tf.setColumns(10);
        serverIP_tf.setText("");
        
        JLabel serverPort_label = new JLabel("Server Port");
        serverPort_label.setFont(new Font("", Font.PLAIN, 14));
        serverPort_label.setBounds(10, 50, 125, 25);
        panel.add(serverPort_label);
       
        JTextField serverPort_tf = new JTextField();
        serverPort_tf.setBounds(140, 50, 125, 25);
        panel.add(serverPort_tf);
        serverPort_tf.setColumns(10);
        serverPort_tf.setText("12001");
        
        JButton connect_btn = new JButton("Connect");
        connect_btn.setFont(new Font("", Font.PLAIN, 14));
        connect_btn.setBounds(140, 100, 125, 25);
        connect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getNMInput()[0] = serverIP_tf.getText();
				getNMInput()[1] = serverIP_tf.getText();
				setValidatedTrue();
			}
		});
        panel.add(connect_btn);
	}
	
	private JFrame validateNM()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 551, 347);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        //ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 517, 293);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
       
        JLabel lblNewLabel = new JLabel("Network Manager");
        lblNewLabel.setBounds(164, 5, 188, 32);
        lblNewLabel.setFont(new Font("", Font.PLAIN, 24));
        panel.add(lblNewLabel);
                
        JLabel serverPass_label = new JLabel("Server Password:");
        serverPass_label.setFont(new Font("", Font.PLAIN, 14));
        serverPass_label.setBounds(74, 158, 117, 14);
        panel.add(serverPass_label);
       
        JTextField serverPass_tf = new JTextField();
        serverPass_tf.setBounds(201, 158, 151, 20);
        panel.add(serverPass_tf);
        serverPass_tf.setColumns(10);
       
        
        JLabel status_label = new JLabel(this.status);
        status_label.setBounds(74, 190, 350, 20);
        panel.add(status_label);
       
        JButton connect_btn = new JButton("Connect");
        connect_btn.setFont(new Font("", Font.PLAIN, 14));
        connect_btn.setBounds(377, 236, 89, 23);
        connect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncoder().AuthenticateManager(serverPass_tf.getText(), " ", -1, -1));
			}
		});
        panel.add(connect_btn);
        
        return frame;
	}

	
	private JFrame startLAN()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 551, 347);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        //ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 554, 242);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
       
        JLabel lblSetupLanPassword = new JLabel("Setup LAN Password");
        lblSetupLanPassword.setBounds(170, 5, 228, 32);
        lblSetupLanPassword.setFont(new Font("", Font.PLAIN, 24));
        panel.add(lblSetupLanPassword);
       
        JLabel lblNewLabel = new JLabel("Network Password:");
        lblNewLabel.setFont(new Font("", Font.PLAIN, 14));
        lblNewLabel.setBounds(92, 117, 139, 19);
        panel.add(lblNewLabel);
       
        JTextField LAN_Password_tf = new JTextField();
        LAN_Password_tf.setBounds(241, 117, 238, 20);
        panel.add(LAN_Password_tf);
        ((JTextField) LAN_Password_tf).setColumns(10);
        //for testing
        LAN_Password_tf.setText("Password");
        
        JLabel status_label = new JLabel(this.status);
        status_label.setBounds(92, 150, 350, 20);
        panel.add(status_label);
       
        JButton startLAN_btn = new JButton("Start LAN");
        startLAN_btn.setFont(new Font("", Font.PLAIN, 14));
        startLAN_btn.setBounds(362, 196, 117, 23);
        startLAN_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncoder().CreateLAN(getNmt().getManagerID(), LAN_Password_tf.getText(), -1));
			}
		});
        panel.add(startLAN_btn);
        return frame;
	}
	
	public void changeToValidateNM()
	{
		Point p = this.frame.getLocation();
		this.frame.dispose();
		validateNM();
		this.frame.setLocation(p);
		this.frame.setVisible(true);
	}

	public void setValidatedTrue()
	{
		this.validated = true;
		
	}
	private void setNMInput(String password, String addr)
	{
		this.NM_input[0] = password;
		this.NM_input[1] = addr;
	}
	
	public String[] getNMInput()
	{
		return this.NM_input;
	}
	
	public void changeFrame()
	{
		Point p = this.frame.getLocation();
		this.frame.dispose();
		this.frame = startLAN();
		this.frame.setLocation(p);
		this.frame.setVisible(true);
	}
	
	public boolean getValidated()
	{
		return this.validated;
	}
	
	public void changeToControlWindow()
	{
		Point p = this.frame.getLocation();
		this.frame.dispose();
		this.frame = NM_controlWindow();
		this.frame.setLocation(p);
		this.frame.setVisible(true);
	}
	
	private JFrame NM_controlWindow()
	{
		frame = new JFrame();
        frame.setBounds(100, 100, 590, 303);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        //ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 554, 242);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
       
        JLabel lblSetupLanPassword = new JLabel("LAN Network Active");
        lblSetupLanPassword.setBounds(170, 5, 228, 32);
        lblSetupLanPassword.setFont(new Font("", Font.PLAIN, 24));
        panel.add(lblSetupLanPassword);
       
        JButton getData_btn = new JButton("Get Client Data");
        getData_btn.setFont(new Font("", Font.PLAIN, 14));
        getData_btn.setBounds(100, 100, 150, 45);
        getData_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncoder().RequestAnalyticData(getNmt().getManagerID(), 0, " ",-1));
			}
		});
        panel.add(getData_btn);
        
        JButton mute_btn = new JButton(muteStatus);
        mute_btn.setFont(new Font("", Font.PLAIN, 14));
        mute_btn.setBounds(300, 100, 150, 45);
        mute_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getMute()==1)
				{
					//Mute Comm Request
					getSendQueue().add(getEncoder().MuteComm(getNmt().getManagerID(), -1));
				}
				else
				{
					//Un-Mute Comm Request
					getSendQueue().add(getEncoder().UnMuteComm(getNmt().getManagerID(), -1));
				}
			}
		});
        panel.add(mute_btn);
        
        
        JLabel status_label = new JLabel(this.status);
        status_label.setBounds(100, 150, 350, 20);
        panel.add(status_label);
        
        JButton end_btn = new JButton("End LAN");
        end_btn.setFont(new Font("", Font.PLAIN, 14));
        end_btn.setBounds(200, 200, 150, 30);
        end_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncoder().EndLAN(getNmt().getManagerID(),-1));
			}
		});
        panel.add(end_btn);
        
        JButton shutdown_btn = new JButton("Shutdown");
        shutdown_btn.setFont(new Font("", Font.PLAIN, 14));
        shutdown_btn.setBounds(400, 200, 150, 30);
        shutdown_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncoder().GracefulShutdown(getNmt().getManagerID(),-1));
			}
		});
        panel.add(shutdown_btn);
        
        
        return frame;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setValidated(boolean b) {
		// TODO Auto-generated method stub
		this.validated = b;
	}

	public Message_Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Message_Encoder encoder) {
		this.encoder = encoder;
	}

	public Queue<String> getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(Queue<String> sendQueue) {
		this.sendQueue = sendQueue;
	}

	public NM_Thread getNmt() {
		return nmt;
	}

	public String getMuteStatus() {
		return muteStatus;
	}

	public void setMuteStatus(String muteStatus) {
		this.muteStatus = muteStatus;
	}

	public int getMute() {
		return mute;
	}

	public void setMute(int mute) {
		this.mute = mute;
	}

	public boolean isEncryptionReady() {
		return encryptionReady;
	}

	public void setEncryptionReady(boolean encryptionReady) {
		this.encryptionReady = encryptionReady;
	}
	
	
	

	

	

}
