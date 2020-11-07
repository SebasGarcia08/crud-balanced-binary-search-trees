package com.dalmatians.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.dalmatians.model.Database;
import com.dalmatians.model.Person;
import com.dalmatians.threads.PersonGeneratorService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class UserManagement implements Initializable {

	private static final int N_CORES = Runtime.getRuntime().availableProcessors();

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

	private PersonGeneratorService[] personGenerators;

	public UserManagement() {
		db = new Database();
		this.personGenerators = new PersonGeneratorService[N_CORES];
		
		for (int i = 0; i < personGenerators.length; i++) {
			personGenerators[i] = new PersonGeneratorService(this, db);
		}
	}

	/**
	 * Divides the progress of a progress bar into n Services concurrent tasks
	 * 
	 * task{0}.progressProperty().multiply(rate).add(
	 * 		task{1}.progressProperty().multiply(rate).add( 
	 * 			task{n-1}.progressProperty().add(0)
	 * 		)
	 * )
	 * 
	 * @param <T>, a Service subclass
	 * @param tasks
	 * @param i
	 * @return
	 */
	public <T extends Service<Void>> DoubleBinding getProgressBinding(T[] tasks, int i) {
		if (i < tasks.length) {
			return tasks[i].progressProperty().multiply(1.0/tasks.length).add(
						getProgressBinding(tasks, i + 1)
					);
		} else {
			return new DoubleBinding() {
				
				@Override
				protected double computeValue() {
					return 0;
				}
			};
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressGridpane.setVisible(false);
		searchingCriterion.getItems().add("Id");
		searchingCriterion.getItems().add("Name");
		searchingCriterion.getItems().add("Surname");
		searchingCriterion.getItems().add("Full Name");
		numUsersToGenerate.setText(100 + "");
		operationsProgressBar.progressProperty().bind(getProgressBinding(personGenerators, 0));
		progressIndicator.progressProperty().bind(getProgressBinding(personGenerators, 0));
	}

	@FXML
	void clearDatabase(ActionEvent event) {

	}

	@FXML
	void generateUsers(ActionEvent event) {
		try {
			progressGridpane.setVisible(true);
			int n = Integer.parseInt(numUsersToGenerate.getText());
			progressMessage.setText("Generating " + n + " persons...");
			db.preapteForGenerate(n);
			
			int start = 0;
			int amount = n / personGenerators.length;
			
			for(int i = 0; i < personGenerators.length-1; i++) {
				personGenerators[i].generate(start, start + amount);
				start += amount;
			}
			
			personGenerators[personGenerators.length-1].generate(start, n);
			
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
		for (Person p : db.getPeople()) {
			db.getIdTree().add(p.getId(), p);
			db.getFullnameTree().add(p.getFullName(), p);
		}
	}

	public void showProgressPane() {
		progressGridpane.setVisible(true);
	}

	public void hideProgressPane() {
		progressGridpane.setVisible(false);
	}

	public void setProgressMessage(String msg) {
		this.progressMessage.setText(msg);
	}

	public ProgressBar getOperationsProgressBar() {
		// TODO Auto-generated method stub
		return null;
	}

}