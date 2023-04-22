package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.services.ITeledonService;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    private Stage loginStage;
    private ITeledonService teledonService;
    @FXML
    private Label invalidLabel;


    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setTeledonService(ITeledonService teledonService) {
        this.teledonService = teledonService;
    }


    @FXML
    public void onLoginClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("On login pressed");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (teledonService.authenticateVolunteer(username, password)) {
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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/teledon.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        TeledonController teledonController = fxmlLoader.getController();
        teledonController.setService(this.teledonService);
        teledonController.setMainStage(stage);
        this.teledonService.addObserver(teledonController);

        this.loginStage.close();
        stage.setScene(scene);
        stage.show();
    }
}
