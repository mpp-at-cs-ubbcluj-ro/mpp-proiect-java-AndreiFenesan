package org.example.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.models.Donor;
import org.example.repositories.interfaces.DonorRepository;
import org.example.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonorDbRepository implements DonorRepository {
    private final JdbcUtils jdbcUtils;
    private Validator<Donor> validator;
    private static final Logger logger = LogManager.getLogger();

    public DonorDbRepository(JdbcUtils jdbcUtils, Validator<Donor> validator) {
        this.jdbcUtils = jdbcUtils;
        this.validator = validator;
    }

    @Override
    public Iterable<Donor> findDonorByNameLike(String pattern) {
        logger.info("searching for donor with name pattern: {}", pattern);
        List<Donor> donors = new ArrayList<>();
        String query = "SELECT * from tables.donor where name ilike ?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, pattern);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    donors.add(extractDonorFromResultSet(resultSet));
                }
                return donors;
            }

        } catch (SQLException e) {
            logger.error("Couldn't find a user with pattern: {}. Error: {}", pattern, e);
        }
        logger.info("Returning found donors");
        return donors;
    }

    @Override
    public Optional<Donor> findDonorByPhoneNumber(String phoneNumber) {
        logger.info("searching for donor with phone number: {}", phoneNumber);
        String query = "SELECT * from tables.donor where phonenumber = ?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Optional<Donor> foundDonor = Optional.of(extractDonorFromResultSet(resultSet));
                    logger.info("Returning the found donor {}", foundDonor);
                    return foundDonor;
                }
            }

        } catch (SQLException e) {
            logger.error("Couldn't save the {}. Error: {}", phoneNumber, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Donor> save(Donor donor) {
        logger.info("Saving {}", donor);
        logger.info("Validating {}", donor);
        this.validator.validate(donor);
        String query = "INSERT INTO tables.donor (name,email,phoneNumber) values (?,?,?) returning *";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, donor.getName());
            preparedStatement.setString(2, donor.getMailAddress());
            preparedStatement.setString(3, donor.getPhoneNumber());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractDonorFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            logger.error("Couldn't save the {}. Error: {}", donor, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Donor> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Donor> update(Donor donor) {
        return Optional.empty();
    }

    @Override
    public Iterable<Donor> findAll() {
        return null;
    }

    @Override
    public Optional<Donor> findOneById(Long id) {
        logger.info("finding donor with id {}", id);
        String query = "SELECT * FROM tables.donor where id = ?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractDonorFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error("Error in finding donor with id: {}", id);
        }
        logger.info("No donnow ith id {} found", id);
        return Optional.empty();
    }

    private Donor extractDonorFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String emailAddress = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phoneNumber");
        return new Donor(id, name, emailAddress, phoneNumber);
    }
}
