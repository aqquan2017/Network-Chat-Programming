package com.chatp.ChatProgramming;

import java.net.InetAddress;

public class ClientEntity {

	public String name;
	public InetAddress ip;
	public int port;
	private final int ID;
	public int attemp = 0;

	public int getID() {
		return ID;
	}

	public ClientEntity(String name , InetAddress ip , int port , int ID) {
		this.name = name ;
		this.ip = ip;
		this.port = port;
		this.ID = ID;
	}

}
