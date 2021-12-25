package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController  {
	

	
	JabberUI JUI = new JabberUI();
	
	public MainController() throws IOException,ClassNotFoundException{
		JUI.setSocket();
	}
    @FXML private TextField T1;
    @FXML private Label lblStatus;
    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B4;

    
    public void login(ActionEvent event) throws IOException, ClassNotFoundException, InterruptedException {
        JabberMessage message = new JabberMessage("signin "+ T1.getText());
        String rep = JUI.dealMessage(message);

        signIn(rep,lblStatus);
    }
    
    public void register(ActionEvent event) throws ClassNotFoundException, IOException, InterruptedException {
    	JabberMessage message = new JabberMessage("register "+ T1.getText());
    	
        String rep = JUI.dealMessage(message);

        signIn(rep,lblStatus);
        
	    Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
    }
    
	public void signIn(String user,Label label) throws IOException, ClassNotFoundException, InterruptedException {
        if (user.equals("signedin")){
        	Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("JabberMain.fxml").openStream());
			PageController pc = (PageController)loader.getController();
			pc.setSocket(JUI,T1.getText());
			Scene scene = new Scene(root);
			stage.setScene(scene);
			pc.updateTimeline();
			pc.updateFollowList();
			stage.show();
			disconnect();
        }
        else if (user.equals("unknown-user")){
 		    Parent root = FXMLLoader.load(getClass().getResource("error.fxml"));
 			Scene scene = new Scene(root);
 			Stage stage = new Stage();
 			stage.setScene(scene);
 			stage.show();
        }

	}
	
	public void disconnect() throws IOException, ClassNotFoundException {
	    Stage stage = (Stage) B4.getScene().getWindow();
	    stage.close();
	}
	
	public void signingOut() throws ClassNotFoundException, IOException {
	    Stage stage = (Stage) B4.getScene().getWindow();
	    stage.close();
	    JUI.dealMessage(new JabberMessage("signout"));
	    JUI.closeSocket();
	}
	

    


}

