package com.dalmatians.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.dalmatians.model.Database;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class UserManagement implements Initializable {

	@FXML
	private JFXTextField numUsersToGenerate;

	@FXML
	private JFXComboBox<String> searchingCriterion;

	@FXML
	private JFXTextField searchText;

	@FXML
	private ImageView userImage;

	@FXML
	private Label idLabel;

	@FXML
	private JFXTextField nameTxt;

	@FXML
	private JFXTextField lastNameTxt;

	@FXML
	private JFXTextField heightTxt;

	@FXML
	private JFXTextField sexTxt;

	@FXML
	private JFXTextField nationalityTxt;

	@FXML
	private JFXButton deleteCurrentUserBtn;

	@FXML
	private GridPane progressGridpane;

	@FXML
	private JFXProgressBar operationsProgressBar;

	@FXML
	private Label progressMessage;

	@FXML
	private ProgressIndicator progressIndicator;

	private Database db;

	public UserManagement() {
		db = new Database();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressGridpane.setVisible(false);
		searchingCriterion.getItems().add("Id");
		searchingCriterion.getItems().add("Name");
		searchingCriterion.getItems().add("Surname");
		searchingCriterion.getItems().add("Full Name");
		numUsersToGenerate.setText(Integer.MAX_VALUE + "");
	}

	@FXML
	void clearDatabase(ActionEvent event) {

	}

	@FXML
	void generateUsers(ActionEvent event) {
		try {
			progressGridpane.setVisible(true);
			int n = Integer.parseInt(numUsersToGenerate.getText());
			progressMessage.setText("Adding " + n + " persons");
			db.preapteForGenerate(n);
			for (int i = 0; i < n; i++) {
				db.getPeople()[i] = db.createRandomPerson(i);
				final double progress = (double) i / (double) n;
				operationsProgressBar.setProgress(progress);
				progressIndicator.setProgress(progress);
			}
//			Platform.runLater(() -> progressGridpane.setVisible(false));
		} catch (NumberFormatException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void searchPerson(ActionEvent event) {

	}

	@FXML
	void deleteCurrentUser(ActionEvent event) {

	}

	@FXML
	void addUsers(ActionEvent event) {

	}

}