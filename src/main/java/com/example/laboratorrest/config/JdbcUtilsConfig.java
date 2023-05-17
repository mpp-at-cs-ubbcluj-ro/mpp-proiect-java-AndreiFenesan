package com.example.laboratorrest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class JdbcUtilsConfig {
    @Bean
    public JdbcUtils jdbcUtils() {
        Properties bdProperties = new Properties();
        try {
            bdProperties.load(new FileReader("./src/main/java/bd.properties"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new JdbcUtils(bdProperties);
    }
}

