package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PageController{
	

	//Socket clientSocket;
	
	@FXML private VBox P1;
	@FXML private VBox P2;
	@FXML private Label Timeline;
	@FXML private Button B3;
	@FXML private Button B6;
	@FXML private TextField T2;
	@FXML private Button Refresh;
	
	
	JabberUI JUI;
	
	ArrayList<ArrayList<String>> timeline;
	ArrayList<ArrayList<String>> data;
	ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
	
	public PageController(){
		timer.scheduleAtFixedRate(TheRunnable, 1, 1, TimeUnit.SECONDS);
		
	}
	
	public void setSocket(JabberUI JUI,String name) {
		this.JUI = JUI;
		Timeline.setText(name+"'s Timeline");
	}
	
	public void setSocket(JabberUI JUI) {
		this.JUI = JUI;
		
	}
	
	
	public void updateTimeline() throws ClassNotFoundException, IOException {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JabberMessage message = new JabberMessage("timeline");
				if(!P1.getChildren().isEmpty()) {
					P1.getChildren().clear();
				}
				try {
					timeline = JUI.dealData(message);
					
					for(int i=0;i < timeline.size();i++) {
						String username;
						String jabtext;
						String jabid;
						String likes;
						
						username = timeline.get(i).get(0);
						jabtext = timeline.get(i).get(1);
						jabid = timeline.get(i).get(2);
						likes = timeline.get(i).get(3);
						
						
						FXMLLoader loader = new FXMLLoader();
						Node node  =  loader.load(getClass().getResource("HBoxTemplate.fxml").openStream());
						HBoxController controller = (HBoxController)loader.getController();
						controller.setSocket(JUI);
						HBox jab = (HBox)node;
						controller.setContent(username,jabtext,jabid,likes);
						
						
						P1.getChildren().add(jab);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});


	}

	public void updateFollowList() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				JabberMessage message = new JabberMessage("users");
				if(!P2.getChildren().isEmpty()) {
					P2.getChildren().clear();
				}
				try {
					data = JUI.dealData(message);
					
					for(int i=0;i < data.size();i++) {
						String username;
						
						username = data.get(i).get(0);
						FXMLLoader loader = new FXMLLoader();
						Node node  =  loader.load(getClass().getResource("followtemplate.fxml").openStream());
						followController controller = (followController)loader.getController();
						controller.setSocket(JUI);
						HBox person = (HBox)node;
						
						controller.setContent(username);

						P2.getChildren().add(person);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public void postJab(ActionEvent event) {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String text = T2.getText();
				JabberMessage message = new JabberMessage("post " + text);
				try {
					JUI.dealMessage(message);
					T2.clear();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		
	}
	
	public void signingOut(ActionEvent event) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				JabberMessage message = new JabberMessage("signout");
				try {
				    Stage stage = (Stage) B3.getScene().getWindow();
				    stage.close();
				   
					JUI.dealMessage(message);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});
	}
	
	public void refreshing(ActionEvent event) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
			
				try {
					updateTimeline();
					updateFollowList();

				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});
	}

	
	public void runUser() {
		
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					while(!JUI.getSocket().isClosed()) {
					try {
						if(!timeline.equals(getTimeline())) {
							//System.out.println("BEING RUN");
							updateTimeline();
							updateFollowList();
						}
						System.out.println("still being run");

					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}});
	
	}
	
	public ArrayList<ArrayList<String>> getTimeline() throws ClassNotFoundException, IOException{
		JabberMessage message = new JabberMessage("timeline");
		return JUI.dealData(message);
	}
	
	public ArrayList<ArrayList<String>> getFollowList() throws ClassNotFoundException, IOException{
		JabberMessage message = new JabberMessage("users");
		return JUI.dealData(message);
	}
	
	Runnable TheRunnable = new Runnable() {
	    public void run() {
			try {
				if(!timeline.equals(getTimeline()) || !data.equals(getFollowList())) {
					updateTimeline();
					updateFollowList();
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			  catch (IOException s) {
				// TODO Auto-generated catch block
				timer.shutdown();
			}
	    
	    }
	};
	




	
	

}
