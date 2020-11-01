package com.dalmatians.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu implements Initializable {

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private StackPane parentContainer;

    @FXML
    private AnchorPane currentSection;

    @FXML
    private VBox mainMenu;

    @FXML
    private JFXButton userOperationsBtn;

    @FXML
    private JFXButton exitButton;
    
	private Pane usersManagementScene;

    public MainMenu() {
    	try {
			this.usersManagementScene = (Pane) FXMLLoader.load(getClass().getResource("/fxml/UserManagement.fxml"));
			userOperationsBtn.setStyle("-fx-background-color:grey;");
    	} catch(Exception e) {
    		
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.parentContainer.getChildren().add(usersManagementScene);
	}
    
    @FXML
    void exit(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void maximizeWindow(ActionEvent event) {
		Stage stage = (Stage) backgroundAnchorPane.getScene().getWindow();
		if (stage.isFullScreen())
			stage.setFullScreen(false);
		else
			stage.setFullScreen(true);
    }

    @FXML
    void minimizeWindow(ActionEvent event) {
		Stage stage = (Stage) backgroundAnchorPane.getScene().getWindow();
		stage.setIconified(true);
    }

    @FXML
    void switch2UserOperations(ActionEvent event) {

    }

}
