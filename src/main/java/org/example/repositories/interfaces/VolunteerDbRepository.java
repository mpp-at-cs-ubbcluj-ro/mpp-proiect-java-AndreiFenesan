package org.example.repositories.interfaces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.Volunteer;
import org.example.repositories.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class VolunteerDbRepository implements VolunteerRepository {
    private JdbcUtils jdbcUtils;
    private static Logger logger = LogManager.getLogger();

    public VolunteerDbRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Optional<Volunteer> save(Volunteer entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Volunteer> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Volunteer> update(Volunteer entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Volunteer> findAll() {
        return null;
    }

    @Override
    public Optional<Volunteer> findOneById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Volunteer> findVolunteerByUsername(String username) {
        String query = "SELECT * FROM tables.volunteer where username = ?";
        Connection connection = jdbcUtils.getConnection();
        logger.info("Finding volunteer with username {}", username);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractVolunteerFromResultSet(resultSet));
                }
            }
        } catch (SecurityException | SQLException exception) {
            logger.error("Error in finding volunteer with username {}", username);
        }
        logger.info("No volunteer found with username {}", username);
        return Optional.empty();
    }

    private Volunteer extractVolunteerFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        return new Volunteer(id, name, username, password);
    }
}
