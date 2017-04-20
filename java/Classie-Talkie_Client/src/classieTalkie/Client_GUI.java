package classieTalkie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client_GUI {
	private final static Logger LOG = Logger.getLogger("Client_Log");
	private Message_Encoder encode;
	private Queue<String> sendQueue;

	private JFrame frame;
	private String[] serverInfo;
	private String[] userInput;
	private boolean inputEntered = false;
	private JTextField fname_tf;
	private JTextField lname_tf;
	private JTextField anum_tf;
	private JTextField netPass_tf;
	private JTextField serverIP_tf;
	private Client_Thread ct;
	private volatile boolean running = true;

	private String status = "...";
	private String pptMessage = "...";
	private String pttState = "Push to Talk";
	private int pttToggle = 1;

	public Client_GUI(Client_Thread ct, Queue<String> sendQueue) {
		this.ct = ct;
		this.sendQueue = sendQueue;
		this.encode = new Message_Encoder();
		this.serverInfo = new String[2];
		this.userInput = new String[4];
		this.frame = new JFrame();
		// this.frame = Client_Info_GUI();
		Connect_To_Server_GUI();
		//ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
		this.frame.setVisible(true);
	}

	public void Connect_To_Server_GUI() {
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
		window_label.setFont(new Font("SansSerif", Font.PLAIN, 24));
		window_label.setBounds(170, 10, 300, 25);
		frame.add(window_label);

		JLabel serverIP_label = new JLabel("Server IP");
		serverIP_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		serverIP_label.setBounds(10, 10, 125, 25);
		panel.add(serverIP_label);

		JTextField serverIP_tf = new JTextField();
		serverIP_tf.setBounds(140, 10, 125, 25);
		panel.add(serverIP_tf);
		serverIP_tf.setColumns(10);
		serverIP_tf.setText("192.168.0.33");

		JLabel serverPort_label = new JLabel("Server Port");
		serverPort_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		serverPort_label.setBounds(10, 50, 125, 25);
		panel.add(serverPort_label);

		JTextField serverPort_tf = new JTextField();
		serverPort_tf.setBounds(140, 50, 125, 25);
		panel.add(serverPort_tf);
		serverPort_tf.setColumns(10);
		serverPort_tf.setText("12001");

		JButton connect_btn = new JButton("Connect");
		connect_btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
		connect_btn.setBounds(140, 100, 125, 25);
		connect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setServerInfo(serverIP_tf.getText(),serverPort_tf.getText());
				setInputEnteredTrue();
			}
		});
		panel.add(connect_btn);
	}

	public JFrame Client_Info_GUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 551, 429);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 515, 345);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Client Info");
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
		lblNewLabel.setBounds(186, 0, 161, 47);
		panel.add(lblNewLabel);

		JLabel fname_label = new JLabel("First Name:");
		fname_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		fname_label.setBounds(72, 73, 124, 25);
		panel.add(fname_label);

		JLabel lname_label = new JLabel("Last Name:");
		lname_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lname_label.setBounds(72, 110, 124, 25);
		panel.add(lname_label);

		JLabel anum_label = new JLabel("A-Number");
		anum_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		anum_label.setBounds(72, 145, 124, 25);
		panel.add(anum_label);

		JLabel netPass_label = new JLabel("Network Password:");
		netPass_label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		netPass_label.setBounds(72, 184, 124, 25);
		panel.add(netPass_label);

		JTextField fname_tf = new JTextField();
		fname_tf.setBounds(206, 75, 150, 25);
		panel.add(fname_tf);
		fname_tf.setColumns(10);

		JTextField lname_tf = new JTextField();
		lname_tf.setBounds(206, 112, 150, 25);
		panel.add(lname_tf);
		lname_tf.setColumns(10);

		JTextField anum_tf = new JTextField();
		anum_tf.setBounds(206, 147, 150, 25);
		panel.add(anum_tf);
		anum_tf.setColumns(10);

		JTextField netPass_tf = new JTextField();
		netPass_tf.setBounds(206, 186, 150, 25);
		panel.add(netPass_tf);
		netPass_tf.setColumns(10);

		JLabel status_label = new JLabel(this.status);
		status_label.setBounds(72, 244, 150, 25);
		panel.add(status_label);

		JButton connect_btn = new JButton("Connect");
		connect_btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
		connect_btn.setBounds(379, 291, 89, 23);
		connect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncode().AuthenticateClient(fname_tf.getText(), lname_tf.getText(),
						anum_tf.getText(), netPass_tf.getText(), -1, -1));
			}
		});
		panel.add(connect_btn);

		/* my info for testing */
		fname_tf.setText("FirstName");
		lname_tf.setText("LastName");
		anum_tf.setText("A00000000");
		netPass_tf.setText("password");

		return frame;
	}

	/* Setter Functions */
	public void setInputEntered(boolean val) {
		this.inputEntered = val;
	}

	public void setInputEnteredTrue() {
		this.inputEntered = true;
	}

	public void setTextFields(String fname, String lname, String anum, String pass, String ip) {
		fname_tf.setText(fname);
		lname_tf.setText(lname);
		anum_tf.setText(anum);
		netPass_tf.setText(pass);
		serverIP_tf.setText(ip);
	}

	public void setUserInput(String fname, String lname, String anum, String pass, String ip) {
		this.userInput[0] = fname;
		this.userInput[1] = lname;
		this.userInput[2] = anum;
		this.userInput[3] = pass;
	}

	public void changeFrameTo_UserInfo() {
		Point p = this.frame.getLocation();
		//ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//this.frame.setIconImage(img.getImage());
		this.frame.dispose();
		this.frame = Client_Info_GUI();
		this.frame.setLocation(p);
		this.frame.setVisible(true);
	}

	public void changeFrameTo_PTT() {
		Point p = this.frame.getLocation();
		//ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//this.frame.setIconImage(img.getImage());
		this.frame.dispose();
		this.frame = PTT_GUI();
		this.frame.setLocation(p);
		this.frame.setVisible(true);
	}

	public JFrame PTT_GUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//ImageIcon img = new ImageIcon(Main.class.getResource("Images/classieTalkieIcon.png"));
		//frame.setIconImage(img.getImage());
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 239);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel message = new JLabel(pptMessage);
		message.setBackground(Color.RED);
		message.setFont(new Font("Tahoma", Font.PLAIN, 18));
		message.setBounds(100, 150, 300, 25);
		panel.add(message);

		JButton ppt_btn = new JButton(pttState);
		ppt_btn.setBackground(Color.BLUE);
		ppt_btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
		ppt_btn.setBounds(145, 85, 131, 27);
		ppt_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getPttToggle()==1)
				{
					//Request to begin transmitting
					getSendQueue().add(getEncode().RequestPriorityToken(ct.getClient_ID(), -1));
				}
				else
				{
					//Request to finish transmittingy
					disconnectFromUDP();//Close UDP Socket
					getSendQueue().add(getEncode().ReleasePriorityToken(ct.getClient_ID(), -1));
					
				}
			}
		});
		panel.add(ppt_btn);

		JButton endLAN_btn = new JButton("Leave LAN");
		endLAN_btn.setBackground(Color.RED);
		endLAN_btn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		endLAN_btn.setBounds(258, 205, 131, 23);
		endLAN_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSendQueue().add(getEncode().ClientDisconnect(ct.getClient_ID(), -1));
				getFrame().dispose();
				getCt().killThreads();
			}
		});
		panel.add(endLAN_btn);

		JLabel classie_label = new JLabel("Classie-Talkie ");
		classie_label.setFont(new Font("SansSerif", Font.PLAIN, 24));
		classie_label.setBounds(120, 11, 156, 27);
		panel.add(classie_label);

		return frame;
	}

	/* Getter Functions */
	public boolean getInputEntered() {
		return this.inputEntered;
	}

	public String[] getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String ip, String port) {
		this.serverInfo[0] = ip;
		this.serverInfo[1] = port;
	}

	public String[] getUserInput() {
		return this.userInput;
	}

	public Client_Thread getCt() {
		return ct;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPptMessage() {
		return pptMessage;
	}

	public void setPptMessage(String pptMessage) {
		this.pptMessage = pptMessage;
	}

	public Message_Encoder getEncode() {
		return encode;
	}

	public void setEncode(Message_Encoder encode) {
		this.encode = encode;
	}

	public Queue<String> getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(Queue<String> sendQueue) {
		this.sendQueue = sendQueue;
	}

	public String getPttState() {
		return pttState;
	}

	public void setPttState(String pttState) {
		this.pttState = pttState;
	}

	public int getPttToggle() {
		return pttToggle;
	}

	public void setPttToggle(int pttToggle) {
		this.pttToggle = pttToggle;
	}
	
	public void disconnectFromUDP()
	{
		if(this.ct.getUdp_socket().isConnected()){
			//Close UDP Socket, End Send_UDP Thread
			this.ct.getUdp_socket().close();
			this.ct.getUdp_sender().setRunning(false);
		}
	}

}
