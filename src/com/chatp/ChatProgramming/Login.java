package com.chatp.ChatProgramming;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfIP;
	private JLabel label;
	private JLabel lblPort;
	private JTextField tfPort;
	private JLabel lblEx;
	private JLabel lblEx_1;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Login() {
		settingWindow();
	}
	
	private void settingWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			requestFocusInWindow();	 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(328,558);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfName = new JTextField();
		tfName.setBounds(69, 54, 186, 27);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP address");
		lblNewLabel.setBounds(132, 105, 60, 13);
		contentPane.add(lblNewLabel);
		
		tfIP = new JTextField();
		tfIP.setColumns(10);
		tfIP.setBounds(69, 130, 186, 27);
		contentPane.add(tfIP);
		
		label = new JLabel("Name");
		label.setBounds(143, 30, 37, 13);
		contentPane.add(label);
		
		lblPort = new JLabel("Port");
		lblPort.setBounds(143, 211, 37, 13);
		contentPane.add(lblPort);
		
		tfPort = new JTextField();
		tfPort.setColumns(10);
		tfPort.setBounds(69, 234, 186, 27);
		contentPane.add(tfPort);
		
		lblEx = new JLabel("Ex : 192.0.0.1");
		lblEx.setBounds(130, 167, 64, 13);
		contentPane.add(lblEx);

		
		lblEx_1 = new JLabel("Ex : 8888");
		lblEx_1.setBounds(135, 271, 53, 13);
		contentPane.add(lblEx_1);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					login(tfName.getText() , tfIP.getText() , Integer.parseInt(tfPort.getText()));					
				}
				catch(Exception ee) {
					ee.printStackTrace();
				}
			}
		}); 
		
		btnLogin.setBounds(111, 443, 102, 43);
		contentPane.add(btnLogin);
	}
	
	
	private void login(String name, String host, int port ) {	
		dispose();
		new ClientWindow(name, host, port);
	}
	
}
