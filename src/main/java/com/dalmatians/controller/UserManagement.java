package com.dalmatians.controller;

import java.awt.event.InputMethodEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.textfield.TextFields;

import com.dalmatians.model.Database;
import com.dalmatians.model.Person;
import com.dalmatians.model.RandomPersonGenerator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class UserManagement implements Initializable {

	public static final int N_CORES = Runtime.getRuntime().availableProcessors();

	@FXML
	private Label numberOfPeopleLbl;

	@FXML
	private JFXDatePicker birthDatePicker;

	@FXML
	private JFXButton generateBtn;

	@FXML
	private JFXButton addBtn;

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
	private JFXButton Btn;

	@FXML
	private GridPane progressGridpane;

	@FXML
	private JFXProgressBar operationsProgressBar;

	@FXML
	private Label progressMessage;

	@FXML
	private ProgressIndicator progressIndicator;

	private Database db;

	Person currentPerson;

	private RandomPersonGenerator randomPersonGenerator;

	public UserManagement() {
		db = new Database();
		randomPersonGenerator = new RandomPersonGenerator(db);
		currentPerson = null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressGridpane.setVisible(false);
		searchingCriterion.getItems().add("Id");
		searchingCriterion.getItems().add("Name");
		searchingCriterion.getItems().add("Surname");
		searchingCriterion.getItems().add("Full Name");
		searchingCriterion.getSelectionModel().selectFirst();
		numUsersToGenerate.setText(Integer.MAX_VALUE + "");
		numberOfPeopleLbl.setText(db.getPeople().length + " people in database");
	}

	@FXML
	void clearDatabase(ActionEvent event) {
		db.clear();
		updatePeopleInDB();
	}

	@FXML
	void autoComplete(KeyEvent event) {

		String selectedCriterion = searchingCriterion.getValue();
		String key = searchText.getText();
		List<Person> values = new LinkedList<>();
		List<String> options = new ArrayList<>(100);

		if (selectedCriterion.equals("Id")) {
			values = db.getIdTree().autoComplete(key, 100);
			for (Person p : values)
				options.add(p.getId() + "");
		} else if (selectedCriterion.equals("Name")) {
			values = db.getFullnameTree().autoComplete(key, 100);
			for (Person p : values)
				options.add(p.getFullName() + "");
		} else if (selectedCriterion.equals("Surname")) {
			values = db.getSurnameTree().autoComplete(key, 100);
			for (Person p : values)
				options.add(p.getFullName() + "");
		} else if (selectedCriterion.equals("Full Name")) {
			values = db.getFullnameTree().autoComplete(key, 100);
			for (Person p : values)
				options.add(p.getFullName() + "");
		}

		TextFields.bindAutoCompletion(searchText, options);
	}

	@FXML
	void generateUsers(ActionEvent event) {
		try {
			progressGridpane.setVisible(true);
			int n = Integer.parseInt(numUsersToGenerate.getText());
			progressMessage.setText("Generating " + n + " persons...");
			db.preapteForGenerate(n);

			randomPersonGenerator.reset(0, n);
			Thread generatingThread = new Thread(() -> {
				addBtn.setDisable(true);
				generateBtn.setDisable(true);
				Person p;
				int workDone = 0;
				while ((p = randomPersonGenerator.getNextPerson()) != null) {
					db.getPeople()[workDone] = p;
					final int progress = workDone++;

					Platform.runLater(() -> {
						updateProgress(progress, n);
						numberOfPeopleLbl.setText(progress + 1 + " people in database");
					});

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			generatingThread.setDaemon(true);
			generatingThread.start();

			Thread visualThread = new Thread(() -> {
				try {
					generatingThread.join();
					Platform.runLater(() -> {
						addBtn.setDisable(false);
						generateBtn.setDisable(false);
						resetProgress();
						progressGridpane.setVisible(false);
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			visualThread.start();

			visualThread.join();
			db.addPeopleToTrees();

		} catch (NumberFormatException | IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void searchPerson(ActionEvent event) {
		String selectedCriterion = searchingCriterion.getValue();
		String key = searchText.getText();
		List<Person> found = null;
		System.out.println(key);

		switch (selectedCriterion) {
		case "Id":
			found = db.getIdTree().search(Integer.parseInt(key));
			break;
		case "Name":
			found = db.getFullnameTree().search(key);
			break;
		case "Surname":
			found = db.getSurnameTree().search(key);
			break;
		case "Full Name":
			found = db.getFullnameTree().search(key);
			break;
		default:
			break;
		}

		if (found != null) {
			Person person = found.get(0);
			currentPerson = person;
			idLabel.setText(person.getId() + "");
			nameTxt.setText(person.getName());
			lastNameTxt.setText(person.getSurname());
			nationalityTxt.setText(person.getNationality());
			birthDatePicker.setValue(person.getBirthdate());
			heightTxt.setText(person.getHeight() + "");
			sexTxt.setText(person.getSex().toString());

			getUserImage(); 

		} else {
			System.out.println(found);
			System.out.println(db.getFullnameTree());
		}
	}

	private void getUserImage() {

		BufferedImage image = null;
		try {

			URL url = new URL("https://thispersondoesnotexist.com/image");
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 
	    	httpcon.addRequestProperty("User-Agent", ""); 
	    	image = ImageIO.read(httpcon.getInputStream());
			userImage.setImage(SwingFXUtils.toFXImage(image, null));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void updateBirthday(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	@FXML
	void updateHeight(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	@FXML
	void updateLastName(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	@FXML
	void updateName(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	@FXML
	void updateNationality(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	@FXML
	void updateSex(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)) {
			//TODO
		}
	}

	
	@FXML
	void deleteCurrentUser(ActionEvent event) {

		if (currentPerson != null) {
			db.delete(currentPerson);
			currentPerson = null;
		}

		idLabel.setText("");
		nameTxt.setText("");
		lastNameTxt.setText("");
		nationalityTxt.setText("");
		birthDatePicker.setValue(null);
		heightTxt.setText("");
		sexTxt.setText("");
		userImage.setImage(null);

	}

	@FXML
	void addUsers(ActionEvent event) {
		try {
			progressGridpane.setVisible(true);
			addBtn.setDisable(true);
			generateBtn.setDisable(true);
			progressMessage.setText("Adding " + db.getPeople().length + " persons to tree...");
			Thread t = new Thread(() -> {
				int total = db.getPeople().length;
				int current = 0;
				for (Person p : db.getPeople()) {
					db.getIdTree().add(p.getId(), p);
					db.getFullnameTree().add(p.getFullName(), p);
					db.getSurnameTree().add(p.getSurname(), p);
					final int curr = current++;
					Platform.runLater(() -> {
						updateProgress(curr, total);
					});
				}
			});
			t.setDaemon(true);
			t.start();

			new Thread(() -> {
				try {
					t.join();
					Platform.runLater(() -> {
						addBtn.setDisable(false);
						generateBtn.setDisable(false);
						resetProgress();
						progressGridpane.setVisible(false);
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		} catch (NumberFormatException e) {
			e.printStackTrace();
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

	public void updateProgress(double progress, int n) {
		double prog = progress / (double) n;
		operationsProgressBar.setProgress(prog);
		progressIndicator.setProgress(prog);
	}

	public void resetProgress() {
		operationsProgressBar.setProgress(0);
		progressIndicator.setProgress(0);
		progressMessage.setText("");
	}

	public void updatePeopleInDB() {
		numberOfPeopleLbl.setText(db.getPeople().length + " people in database");
	}
}