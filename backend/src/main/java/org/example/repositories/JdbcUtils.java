package org.example.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private final Properties properties;
    private static final Logger logger = LogManager.getLogger();
    private Connection connectionInstance;

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    private Connection getNewConnectionInstance() {
        logger.traceEntry("Trying to connect to db");
        String connectionString = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        logger.info("Trying to connect to {}", connectionString);
        Connection connection = null;
        try {
            if (username != null && password != null) {
                connection = DriverManager.getConnection(connectionString, username, password);
            } else {
                connection = DriverManager.getConnection(connectionString);
            }
        } catch (SQLException exception) {
            logger.fatal("Can't connect to database ", exception);
        }
        logger.info("Connected successfully to {}", connectionString);
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if(connectionInstance == null || connectionInstance.isClosed()){
                connectionInstance = getNewConnectionInstance();
            }
        }catch (SQLException exception){
            logger.fatal("Cannot get a new connection: {}",exception.getMessage());
        }
        logger.traceExit();
        return connectionInstance;
    }

}
