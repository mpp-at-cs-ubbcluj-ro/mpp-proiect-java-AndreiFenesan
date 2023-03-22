package org.example.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.Donation;
import org.example.repositories.interfaces.DonationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DonationDbRepository implements DonationRepository {
    private JdbcUtils jdbcUtils;
    private static Logger logger = LogManager.getLogger();

    public DonationDbRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public double getTotalAmountOfRaisedMoney(Long charityCaseId) {
        logger.info("Computing the total amount of money raised for charity with id {}", charityCaseId);
        String query = "select SUM(amount) as totalAmount from tables.donation where charitycaseid=?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, charityCaseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double totalAmount = resultSet.getDouble("totalAmount");
                    logger.info("Amount of money for charity with id {} is {}", charityCaseId, totalAmount);
                    return totalAmount;
                }
            }
        } catch (SQLException exception) {
            logger.error("Couldn't compute the total amount of money raised for charity with id {}", charityCaseId);
        }
        return -1;
    }

    @Override
    public Optional<Donation> save(Donation donation) {
        logger.info("Saving donation {}", donation);
        String query = "INSERT INTO tables.donation (amount,charityCaseId,donorId) values (?,?,?) returning *";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, donation.getAmount());
            preparedStatement.setLong(2, donation.getCharityCaseId());
            preparedStatement.setLong(3, donation.getDonor().getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("Donation {} saved successfully", donation);
                    Donation donationFromDb = extractDonationFromResultSet(resultSet);
                    return Optional.of(donationFromDb);
                }
            }
        } catch (SQLException exception) {
            logger.error("Couldn't save the donation. Error {}", exception.getMessage());
        }
        logger.info("Couldn't save donation {}", donation);
        return Optional.empty();
    }

    private Donation extractDonationFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        double amount = resultSet.getDouble("amount");
        long charityCaseId = resultSet.getLong("charityCaseId");
        return new Donation(id, amount, charityCaseId, null);
    }

    @Override
    public Optional<Donation> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Donation> update(Donation entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Donation> findAll() {
        return null;
    }

    @Override
    public Optional<Donation> findOneById(Long aLong) {
        return Optional.empty();
    }
}
