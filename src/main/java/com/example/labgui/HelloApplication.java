package com.example.labgui;

import com.example.labgui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.repositories.CharityCaseDbRepository;
import org.example.repositories.DonationDbRepository;
import org.example.repositories.DonorDbRepository;
import org.example.repositories.JdbcUtils;
import org.example.repositories.interfaces.*;
import org.example.services.CharityCaseService;
import org.example.services.DonationService;
import org.example.services.DonorService;
import org.example.services.VolunteerService;
import org.example.validators.DonationValidator;
import org.example.validators.DonorValidator;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Properties;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("./src/main/java/bd.properties"));
        } catch (NoSuchFileException e) {
            System.out.println(e.getMessage());
        }
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        VolunteerRepository volunteerRepository = new VolunteerDbRepository(jdbcUtils);
        VolunteerService volunteerService = new VolunteerService(volunteerRepository);

        DonorRepository donorRepository = new DonorDbRepository(jdbcUtils);
        DonorService donorService = new DonorService(donorRepository);

        CharityCaseRepository charityCaseRepository = new CharityCaseDbRepository(jdbcUtils);
        DonationRepository donationRepository = new DonationDbRepository(jdbcUtils);

        CharityCaseService charityCaseService = new CharityCaseService(charityCaseRepository, donationRepository);
        DonationService donationService = new DonationService(donationRepository, donorRepository, charityCaseRepository);

        initialiseWindows(3, volunteerService, charityCaseService, donorService, donationService);
    }

    public static void main(String[] args) {
        launch();
    }

    private void initialiseWindows(int numberOfWindows, VolunteerService volunteerService, CharityCaseService charityCaseService,
                                   DonorService donorService, DonationService donationService) throws IOException {
        for (int i = 0; i < numberOfWindows; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 320);
            Stage stage = new Stage();

            LoginController loginController = fxmlLoader.getController();
            loginController.setVolunteerService(volunteerService);
            loginController.setDonationService(donationService);
            loginController.setDonorService(donorService);
            loginController.setCharityCaseService(charityCaseService);
            loginController.setLoginStage(stage);

            stage.setScene(scene);
            stage.setTitle("Log in");
            stage.show();
        }
    }
}