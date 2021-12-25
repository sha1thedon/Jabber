package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class followController {
	
	@FXML private HBox person;
	@FXML private Label name;
	@FXML private ImageView imgF;
	String user;
	
	Image icon = new Image(getClass().getResourceAsStream("plus.png"));
	
	JabberUI JUI;
	public void setSocket(JabberUI JUI) {
		this.JUI = JUI;
	}
	
	public void setContent(String name) {
		imgF.setImage(icon);
		this.name.setText(name);
		user = name;
	}
	
	public void followed(MouseEvent mouseEvent ) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("followed");
				JabberMessage message = new JabberMessage("follow " + user);
				try {
					JUI.dealMessage(message);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		
	}
	

}
