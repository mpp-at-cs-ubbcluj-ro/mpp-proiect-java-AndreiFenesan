package org.example;

import jakarta.persistence.EntityManager;
import org.example.repositories.*;
import org.example.repositories.interfaces.*;
import org.example.servers.AbstractServer;
import org.example.servers.ConcurrentServer;
import org.example.services.TeledonService;
import org.example.validators.DonationValidator;
import org.example.validators.DonorValidator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.SessionFactoryBuilderImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.models.Donor;
import org.services.ITeledonService;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        EntityManager entityManager = null;
        try {
            SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            entityManager = factory.createEntityManager();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        DonorRepository donorRepository = new DonorDbHibernateRepository(new DonorValidator(), entityManager);
        VolunteerRepository volunteerRepository = new VolunteerDbRepository(jdbcUtils);
        CharityCaseRepository charityCaseRepository = new CharityCaseDbRepository(jdbcUtils);
        DonationRepository donationRepository = new DonationDbRepository(jdbcUtils, new DonationValidator());
//        DonorRepository donorRepository = new DonorDbRepository(jdbcUtils, new DonorValidator());

        ITeledonService teledonService = new TeledonService(charityCaseRepository, donationRepository, donorRepository, volunteerRepository);

        AbstractServer server = new ConcurrentServer(serverProperties, teledonService);
        server.start();


    }
}