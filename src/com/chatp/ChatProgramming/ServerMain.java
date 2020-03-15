package com.chatp.ChatProgramming;

public class ServerMain {

	private int port;
	private Server server;
	
	public ServerMain(int port) {
		this.port = port;
		server = new Server(this.port);
	}

	
	public static void main(String[] args) {
		int port = 8888;
		
		if(args.length > 0) 
			port = Integer.parseInt(args[0]);
		
		new ServerMain(port);
	}
}
