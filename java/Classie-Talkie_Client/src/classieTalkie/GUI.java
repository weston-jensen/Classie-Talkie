package classieTalkie;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {
	private JFrame frame;
	private boolean selectionMade;
	private int selectionType;
	static String[] userOptions = {"Network Manager", "Client"};
	
	public GUI()
	{
		this.selectionMade = false;
		this.selectionType = 0;
		
		this.frame = new JFrame();
		this.frame = firstFrame();
		this.frame.setVisible(true);
	}
	
	private JFrame firstFrame()
	{
		frame = new JFrame();
        frame.setBounds(100, 100, 551, 347);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
       
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 515, 286);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
       
        JLabel lblNewLabel = new JLabel("Welcome to Classie-Talkie");
        lblNewLabel.setBounds(77, 5, 327, 37);
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        panel.add(lblNewLabel);
       
        @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox comboBox = new JComboBox(userOptions);
        comboBox.setBounds(243, 127, 150, 25);
        panel.add(comboBox);
       
        JLabel lblNewLabel_1 = new JLabel("Pick User Type:");
        lblNewLabel_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(134, 125, 116, 20);
        panel.add(lblNewLabel_1);
       
        JButton continue_btn = new JButton("Continue");
        continue_btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        continue_btn.setBounds(334, 218, 100, 23);
        continue_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectionType(comboBox.getSelectedIndex());
				setSelectionState();
			}
		});
        panel.add(continue_btn);
        
        return frame;
	}
	
	private void setSelectionType(int value)
	{
		this.selectionType = value;
	}
	
	private void setSelectionState()
	{
		this.selectionMade = true;
		System.out.println("continue");
		this.frame.dispose();
	}
	
	/*Getters*/
	public boolean getSelectionState()
	{
		return this.selectionMade;
	}
	
	public int getSelectionType(){
		return this.selectionType;
	}
	

}
