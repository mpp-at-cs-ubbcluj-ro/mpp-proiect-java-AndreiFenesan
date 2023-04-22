package com.example.client;

import com.example.client.servicies.ITeledonService;
import com.example.client.servicies.TeledonServiceProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        Stage loginStage = new Stage();
        ITeledonService teledonProxy = new TeledonServiceProxy(34999, "127.0.0.1");
        LoginController loginController = fxmlLoader.getController();
        loginController.setTeledonService(teledonProxy);
        loginController.setLoginStage(loginStage);
        loginStage.setTitle("Login");
        loginStage.setScene(scene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}