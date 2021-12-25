package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class HBoxController {
	
	@FXML private HBox jab;
	@FXML private Label UN;
	@FXML private Label JT;
	@FXML private Label likeNum;
	@FXML private ImageView imgH;
	String jabid;
	JabberUI JUI;
	PageController pc;
	
	Image icon = new Image(getClass().getResourceAsStream("heart.jpg"));
	
	public void setContent(String UN,String JT,String jabid,String likeNum) throws ClassNotFoundException, IOException {
		imgH.setImage(icon);
		this.UN.setText(UN);
		this.JT.setText(JT);
		this.jabid = jabid;
		this.likeNum.setText(likeNum);

		pc = new PageController();
		pc.setSocket(JUI);
	}
	
	public void liked(MouseEvent mouseEvent) {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("liked");
				JabberMessage message = new JabberMessage("like " + jabid);
				try {
					JUI.dealMessage(message);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}
		
	
	public void setSocket(JabberUI JUI) {
		this.JUI = JUI;
	}
	
	

}
