package com.example.labgui.controllers;

import com.example.labgui.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.CharityCaseService;
import org.example.services.DonationService;
import org.example.services.DonorService;
import org.example.services.VolunteerService;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    private Stage loginStage;
    @FXML
    private Label invalidLabel;
    private VolunteerService volunteerService;
    private CharityCaseService charityCaseService;
    private DonationService donationService;
    private DonorService donorService;

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setCharityCaseService(CharityCaseService charityCaseService) {
        this.charityCaseService = charityCaseService;
    }

    public void setDonationService(DonationService donationService) {
        this.donationService = donationService;
    }

    public void setDonorService(DonorService donorService) {
        this.donorService = donorService;
    }

    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }


    @FXML
    public void onLoginClicked(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (volunteerService.authenticateVolunteer(username, password)) {
            loadMainScene();
        } else {
            displayInvalidCredentials();
        }
    }

    private void displayInvalidCredentials() {
        this.invalidLabel.setOpacity(1);
    }

    private void loadMainScene() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teledon.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        TeledonController teledonController = (TeledonController) fxmlLoader.getController();
        teledonController.setCharityCaseService(this.charityCaseService);
        teledonController.setDonationService(donationService);
        teledonController.setDonorService(donorService);
        teledonController.setMainStage(stage);

        this.loginStage.close();
        stage.setScene(scene);
        stage.show();
    }

}