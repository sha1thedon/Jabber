package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class JabberUI {
	
	Socket clientSocket;
	
	public void setSocket() throws UnknownHostException, IOException {
		 clientSocket = new Socket("localhost", 44444);
	}
	
	public Socket getSocket() {
		return clientSocket;
	}
	
	public void closeSocket() throws IOException {
		clientSocket.close();
	}
	
	
    public String dealMessage(JabberMessage message)throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
        
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        JabberMessage request = (JabberMessage) ois.readObject();
        
        return request.getMessage();
    }
    
    public ArrayList<ArrayList<String>> dealData(JabberMessage message) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
    
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        JabberMessage request = (JabberMessage) ois.readObject();
        
        return request.getData();
    }
	
	

}
