package com.example.laboratorrest.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private final Properties properties;
    private Connection connectionInstance;

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    private Connection getNewConnectionInstance() {
        String connectionString = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        Connection connection = null;
        try {
            if (username != null && password != null) {
                connection = DriverManager.getConnection(connectionString, username, password);
            } else {
                connection = DriverManager.getConnection(connectionString);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return connection;
    }

    public Connection getConnection() {
        try {
            if (connectionInstance == null || connectionInstance.isClosed()) {
                connectionInstance = getNewConnectionInstance();
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return connectionInstance;
    }

}
