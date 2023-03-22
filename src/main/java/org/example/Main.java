package org.example;

import org.example.models.Donation;
import org.example.models.Donor;
import org.example.repositories.CharityCaseDbRepository;
import org.example.repositories.DonationDbRepository;
import org.example.repositories.DonorDbRepository;
import org.example.repositories.JdbcUtils;
import org.example.repositories.interfaces.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties jdbcProperties = new Properties();
        try {
            jdbcProperties.load(new FileReader("src/main/java/bd.properties"));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        JdbcUtils utils = new JdbcUtils(jdbcProperties);
        DonorRepository donorRepository = new DonorDbRepository(utils);
        CharityCaseRepository charityCaseRepository = new CharityCaseDbRepository(utils);
        DonationRepository donationRepository = new DonationDbRepository(utils);
        //System.out.println(donationRepository.getTotalAmountOfRaisedMoney(1L));
//        donorRepository.save(new Donor("Ion Iliescu","IonIliescu@gmail.com","0741784521"));
        donorRepository.findDonorByNameLike("%ion%").forEach(System.out::println);
        VolunteerRepository volunteerRepository = new VolunteerDbRepository(utils);

    }
}