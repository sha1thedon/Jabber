package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class JabberServer implements Runnable {
	
	JabberDatabase JD = new JabberDatabase();
	
	private static final int PORT_NUMBER = 44444;
	private ServerSocket serverSocket;
	
	public JabberServer() throws IOException {
		serverSocket = new ServerSocket(PORT_NUMBER);
		//serverSocket.setSoTimeout();
		new Thread(this).start();

	}

	public static void main(String[] args) throws IOException {
		JabberServer JS = new JabberServer();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Server Running");
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				ClientConnection client = new ClientConnection(clientSocket, JD);
				//System.out.println("Connected!");
				Thread.sleep(10000);//sleeps for 10 seconds 
			    
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
