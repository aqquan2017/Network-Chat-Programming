package com.chatp.ChatProgramming;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	private int ID;
	private String name;
	private String host;
	private int port;

	private DatagramSocket socket;
	private InetAddress ip;

	private Thread send;
	private static final long serialVersionUID = 1L;

	public Client(String name, String host, int port) {
		this.setName(name);
		this.setHost(host);
		this.setPort(port);
	}

	public boolean openConnection(String host) {
		try {
			ip = InetAddress.getByName(host);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String receiveMessfromServer() {
		byte[] data = new byte[1024];
		DatagramPacket dSocket = new DatagramPacket(data, data.length);

		try {
			socket.receive(dSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mess = new String(dSocket.getData());
		return mess;
	}
	
	public void sendMessToServer(String s) {
		s += "/e/";
		sendMessToServer(s.getBytes());
	}

	public void sendMessToServer(final byte[] data) {
		send = new Thread("send") {
			public void start() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public void close() {
		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
