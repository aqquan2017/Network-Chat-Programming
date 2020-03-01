package com.chatp.ChatProgramming;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame implements Runnable{

	/**
	 * 
	 */
	private final String userName ;
	private static final long serialVersionUID = 1L;

	private Client client;
	 
	private DefaultCaret caret;
	private JPanel contentPane;
	private JTextField textMess;
	private JTextArea textHistory = new JTextArea();
	private Thread receive;
	private boolean running;
	
	
	
	public ClientWindow(String name , String host , int port) {
		this.userName = name;
		setTitle("Chat");
		client = new Client(name , host , port);
		settingWindow();
		
		if(!client.openConnection(host)) {
			System.out.println("Failed to connect");
			return;
		}
		
		String string = "/c/"+ name;
		client.sendMessToServer(string);
		running = true;
		receive = new Thread(this, "receive");
		receive.start();
	}
	
	
	private void receive() {
		receive = new Thread("receive");
		while(running) {
			String s = client.receiveMessfromServer();
			if(s.startsWith("/c/")) {
				client.setID(Integer.parseInt(s.split("/c/|/e/")[1]));
				printMess("Connection Succesfully !!! "+ "ID : "+client.getID());
			}
			else if(s.startsWith("/m/")){
				String mess = s.split("/m/|/e/")[1];
				printMess(mess);
			}
			else if(s.startsWith("/t/")) {
				client.sendMessToServer("/t/"+this.userName);
			}
			else if(s.startsWith("/k/")) {
				sendMessToScreen("Unavailable name or is kicked !Quit app in 3s..");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		receive.start();
	}
	
	private void settingWindow() {
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(550, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20 , 450 , 40 };
		gbl_contentPane.rowHeights = new int[]{20 , 560 , 20 };
		gbl_contentPane.columnWeights = new double[]{0, 1.0};
		gbl_contentPane.rowWeights = new double[]{ 0,Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scroll = new JScrollPane(textHistory);
		
		caret = (DefaultCaret) textHistory.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		textHistory.setEditable(false);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.gridwidth = 2;

		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 1;
		scrollConstraints.gridy = 1;
		contentPane.add(scroll, scrollConstraints);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stringValid(textMess.getText())) {	
					String s = "/m/" + client.getName() + " : "+ textMess.getText();
					client.sendMessToServer(s);
					textMess.setText("");
				}
			}
		});
		
		textMess = new JTextField();
		textMess.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(stringValid(textMess.getText())) {
						String s = "/m/" + client.getName() +" : "+ textMess.getText();
						client.sendMessToServer(s);
						textMess.setText("");
					}		
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String mess = "/d/" +client.getID();
				client.sendMessToServer(mess);
				running = false;
				client.close();
				dispose();
			}
			
		});

		GridBagConstraints gbc_textMess = new GridBagConstraints();
		gbc_textMess.insets = new Insets(0, 0, 5, 5);
		gbc_textMess.fill = GridBagConstraints.HORIZONTAL;
		gbc_textMess.gridx = 1;
		gbc_textMess.gridy = 2;
		contentPane.add(textMess, gbc_textMess);
		textMess.setColumns(10);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		textMess.setColumns(10);
		
		
		textMess.requestFocusInWindow();
	}
	
	private boolean stringValid(String s) {
		for(int i=0 ; i<s.length() ; i++) {
			if(s.charAt(i) != ' '){
				return true;
			}
		}
		return false;
	}
	
	private void sendMessToScreen(String mess) {
		textMess.setText("");
		
		printMess(mess);
	}
	
	private void printMess(String mess) {
		textHistory.setCaretPosition(textHistory.getDocument().getLength());
		textHistory.append(mess + "\n");
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		receive();
	}
}
