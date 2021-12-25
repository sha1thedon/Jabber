package com.bham.fsd.assignments.jabberserver;
//REMOVE ALL PRINT STATEMENTS AND RESET DATABASE FUNCTION
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection implements Runnable {
	
	Socket socket;
	JabberDatabase JD;
	int thisClientID;
	String thisClientName;
	boolean quit;
	
	
	public ClientConnection(Socket socket,JabberDatabase JD) {
		this.socket = socket;
		this.JD = JD;
		this.quit = false;
		thisClientID = -1;
		thisClientName = null;
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		JabberMessage request;
		String message, answer;
		
		while (quit==false) {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			request = (JabberMessage) ois.readObject();
			//System.out.println(request.getMessage());
			message = request.getMessage();
			
			if (message.startsWith("signin")) {
				//System.out.println("sign in request recieved");
				answer = message.substring(7,message.length());
				answer = signingIn(answer);
				JabberMessage toSend = new JabberMessage(answer);
				respond(toSend);
			}
			else if(message.startsWith("register")) {
				answer = registering(message.substring(9,message.length()));
				JabberMessage toSend = new JabberMessage(answer);
				respond(toSend);
			}
			else if (message.equals("signout")) {
				//System.out.println("kill connection");
				quit = true;//possibly remove this feature
				socket.close();
			}
			else if (message.equals("reset")) {
				//JD.resetDatabase();//remove feature after
			}
			else if (message.equals("timeline")) {
				ArrayList<ArrayList<String>> data = getTimeline();
				JabberMessage toSend = new JabberMessage("timeline",data);
				respond(toSend);
			}
			else if (message.startsWith("like")) {
				addingLike(message.substring(5,message.length()));
				
				JabberMessage toSend = new JabberMessage("posted");
				respond(toSend);
			}
			else if (message.equals("users")) {
				ArrayList<ArrayList<String>> data = getNotFollowed();
				JabberMessage toSend = new JabberMessage("users",data);
				respond(toSend);
			}
			else if (message.startsWith("follow")) {
				
				ArrayList<ArrayList<String>> data = addingFollow(message.substring(7,message.length()));
				JabberMessage toSend = new JabberMessage("posted",data);
				respond(toSend);
				
			}
			else if (message.startsWith("post")) {
				
				ArrayList<ArrayList<String>> data = postingJab(message.substring(5,message.length()));
				JabberMessage toSend = new JabberMessage("posted",data);
				respond(toSend);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
	}
	
	public void respond(JabberMessage reply) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(reply);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String signingIn(String user) {
		
		int ID = JD.getUserID(user);
		thisClientID = ID;
		if (ID == -1) {
			thisClientName = null;
			return "unknown-user";
		}
		else {
			thisClientName = user;
			return "signedin";
		}
		
	}
	
	public String registering(String username) {
		
		
		String email = username + "@gmail.com";
		
		JD.addUser(username, email);
		return signingIn(username);
		
	}
	
	public ArrayList<ArrayList<String>> getTimeline() {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		result = JD.getTimelineOfUserEx(thisClientID);
		return result;
		
	}
	
	public void addingLike(String toLike) {
		int jabID = Integer.parseInt(toLike);
		JD.addLike(thisClientID, jabID);
	}
	
	public ArrayList<ArrayList<String>> getNotFollowed(){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		result = JD.getUsersNotFollowed(thisClientID);
		return result;
	}
	
	public ArrayList<ArrayList<String>> addingFollow(String toFollow) {
		JD.addFollower(thisClientID, toFollow);
		return getTimeline();
	}
	
	public ArrayList<ArrayList<String>> postingJab(String jab) {
		JD.addJab(thisClientName, jab);
		return getTimeline();
	}
	
	

}
