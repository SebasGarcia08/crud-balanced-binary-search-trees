package com.dalmatians.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;

public class UserManagement {

    @FXML
    private JFXTextField numUsersToGenerate;

    @FXML
    private JFXComboBox<?> searchingCriterion;

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
    private JFXProgressBar operationsProgressBar;

    @FXML
    private Label progressMessage;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    void clearDatabase(ActionEvent event) {

    }

    @FXML
    void generateUsers(ActionEvent event) {

    }

    @FXML
    void searchPerson(ActionEvent event) {

    }

}