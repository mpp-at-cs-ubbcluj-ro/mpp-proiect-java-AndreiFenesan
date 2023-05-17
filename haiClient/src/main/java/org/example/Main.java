package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.jsonProtocol.TeledonJsonProxy;
import org.example.objectProtocol.TeledonServiceProxy;
import org.services.ITeledonService;


import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        Stage loginStage = new Stage();
//        ITeledonService teledonProxy = new TeledonJsonProxy(10102, "127.0.0.1");
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