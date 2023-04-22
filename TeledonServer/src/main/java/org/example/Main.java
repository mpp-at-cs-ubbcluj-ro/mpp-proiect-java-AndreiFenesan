package org.example;

import org.example.repositories.CharityCaseDbRepository;
import org.example.repositories.DonationDbRepository;
import org.example.repositories.DonorDbRepository;
import org.example.repositories.JdbcUtils;
import org.example.repositories.interfaces.*;
import org.example.servers.AbstractServer;
import org.example.servers.ConcurrentServer;
import org.example.services.TeledonService;
import org.example.validators.DonationValidator;
import org.example.validators.DonorValidator;
import org.services.ITeledonService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties bdProperties = new Properties();
        Properties serverProperties = new Properties();
        try {
            bdProperties.load(new FileReader("./src/main/java/bd.properties"));
            serverProperties.load(new FileReader("./src/main/java/server.properties"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        JdbcUtils jdbcUtils = new JdbcUtils(bdProperties);

        VolunteerRepository volunteerRepository = new VolunteerDbRepository(jdbcUtils);
        CharityCaseRepository charityCaseRepository = new CharityCaseDbRepository(jdbcUtils);
        DonationRepository donationRepository = new DonationDbRepository(jdbcUtils, new DonationValidator());
        DonorRepository donorRepository = new DonorDbRepository(jdbcUtils, new DonorValidator());

        ITeledonService teledonService = new TeledonService(charityCaseRepository, donationRepository, donorRepository, volunteerRepository);

        AbstractServer server = new ConcurrentServer(serverProperties, teledonService);
        server.start();
    }
}