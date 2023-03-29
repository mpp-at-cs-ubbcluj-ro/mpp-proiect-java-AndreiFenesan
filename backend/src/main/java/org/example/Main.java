package org.example;
import org.example.repositories.CharityCaseDbRepository;
import org.example.repositories.DonationDbRepository;
import org.example.repositories.DonorDbRepository;
import org.example.repositories.JdbcUtils;
import org.example.repositories.interfaces.*;
import org.example.services.CharityCaseService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
//        Properties jdbcProperties = new Properties();
//        try {
//            jdbcProperties.load(new FileReader("src/main/java/bd.properties"));
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//        JdbcUtils utils = new JdbcUtils(jdbcProperties);
//        DonorRepository donorRepository = new DonorDbRepository(utils);
//        CharityCaseRepository charityCaseRepository = new CharityCaseDbRepository(utils);
//        DonationRepository donationRepository = new DonationDbRepository(utils);
//        VolunteerRepository volunteerRepository = new VolunteerDbRepository(utils);
//        CharityCaseService charityCaseService = new CharityCaseService(charityCaseRepository,donationRepository);
        System.out.println("Mai");
    }
}