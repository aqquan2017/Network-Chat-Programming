package com.chatp.ChatProgramming;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable{

	private ArrayList<ClientEntity> ClientList = new ArrayList<>();
	
	private final int MAX_ATTEMP = 5;
	private int port;
	private boolean running = false;
	
	private DatagramSocket socket;
	private Thread run , receive, send, manage;
	
	public Server(int port) {
		this.port = port;
		System.out.println("Server running in "+port);
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		run = new Thread(this, "run");
		
		run.start();
	}

	@Override
	public void run() {
		running = true;
		manage();
		receive();
	}
	
	private void manage() {
		manage = new Thread("manage") {
			public void run() {
				while(running) {
					manageProcess();
				}
			}
		};
		manage.start();
	}
	
	private void manageProcess() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendtoAll("/t/");
		for(int i=0 ; i<ClientList.size() ; i++) {
			ClientEntity c = ClientList.get(i); 
			c.attemp ++;
			if(c.attemp >= MAX_ATTEMP) {
				disconnect(c.getID(), false);
			}
		}
	}
	
	private void receive() {
		receive = new Thread("receive") {
			public void run() {
				while(running) {
					receiveProcess();
				}
			}
		};
		receive.start();
	}
	
	private void receiveProcess() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
			String string = new String(packet.getData());
			
			if(string.startsWith("/c/")) {
				int ID = UniqueID.getIdentifier();
				String name = string.split("/c/|/e/")[1];
				ClientList.add(new ClientEntity(name, packet.getAddress(), packet.getPort(), ID));
				System.out.println("Client " +name +" joined !!!");
				String s = "/c/"+ID;
				sendMess(s, packet.getAddress(), packet.getPort());
				sendtoAll("/m/" + name + " has joined this conversation !!!");
			}
			else if(string.startsWith("/m/")){
				String name = string.split("/m/|/e/")[1];
				System.out.println(name);
				sendtoAll(string);
			}
			else if(string.startsWith("/d/")) {
				String s = string.split("/d/|/e/")[1];
				int id = Integer.parseInt(s);
				disconnect(id, true);
			}
			else if(string.startsWith("/t/")) {
				String name = string.split("/t/|/e/")[1];
				System.out.println("DA NHAN MANAGECLIENT "+name);
				for(int i=0 ; i<ClientList.size() ; i++) {
					ClientEntity c = ClientList.get(i);

					if(c.name.equals(name)) {
						c.attemp = 0;
					}
					
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} 
	}
	
	private void disconnect(int id , boolean state) {
		//true is closing, false is timeout
		ClientEntity c = null;
				
		for(int i=0 ; i<ClientList.size() ; i++) {
			if(id == ClientList.get(i).getID()) {
				c = ClientList.get(i);
				ClientList.remove(i);
				sendtoAll(c.name + " is disconnected in the conversation !!!");
				break;
			}
		}
		
		String s = "";
		if(state) {
			s = "Remove '"+ c.name+ "' with ("+c.ip +") out of the list due to closing application";
		}
		else {
			s = "Remove "+ c.name +" with "+c.ip +" out of the list due to timeout ";
		}
		System.out.println(s);
	}
	
	private void sendtoAll(String mess) {
		for(int i=0 ; i<ClientList.size() ; i++) {
			sendMess(mess, ClientList.get(i).ip, ClientList.get(i).port);
		}
	}
	
	private void sendMess(String mess, InetAddress ip,int port) {
		mess += "/e/";
		sendMess(mess.getBytes(), ip, port);
	}
	
	private void sendMess(final byte[] data ,final InetAddress ip,final int port) { 
		send = new Thread("send") {
			public void start() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
}
