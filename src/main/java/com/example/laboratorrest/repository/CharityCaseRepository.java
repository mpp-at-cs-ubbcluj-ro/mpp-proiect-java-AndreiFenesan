package com.example.laboratorrest.repository;

import com.example.laboratorrest.config.JdbcUtils;
import com.example.laboratorrest.model.CharityCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CharityCaseRepository {
    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public CharityCaseRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    public Optional<CharityCase> save(CharityCase entity) {
        String query = "INSERT INTO tables.charitycase (casename, description) values (?,?) returning *";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getCaseName());
            preparedStatement.setString(2, entity.getDescription());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractCharityCaseFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return Optional.empty();
    }

    public Optional<CharityCase> delete(Long aLong) {
        String query = "DELETE FROM tables.charitycase WHERE id = ? returning *";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractCharityCaseFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error in getting all CharityCases " + exception.getMessage());
        }
        return Optional.empty();
    }

    public boolean update(CharityCase entity) {
        String query = "UPDATE tables.charitycase SET casename=?, description=? WHERE id = ?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getCaseName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setLong(3, entity.getId());
            int rowChanged = preparedStatement.executeUpdate();
            return rowChanged == 1;
        } catch (SQLException exception) {
            System.out.println("Error in getting all CharityCases " + exception.getMessage());
        }
        return false;
    }

    public List<CharityCase> findAll() {
        logger.info("Finding all charity cases");
        List<CharityCase> charityCases = new ArrayList<>();
        String query = "SELECT * FROM tables.charitycase";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    charityCases.add(extractCharityCaseFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("Error in getting all CharityCases: {}", exception.getMessage());
        }
        logger.info("Found {} charity cases", charityCases.size());
        return charityCases;
    }

    public Optional<CharityCase> findOneById(Long id) {
        logger.info("Finding charity case by id {}", id);
        String query = "SELECT * FROM tables.charitycase where id = ?";
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractCharityCaseFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("Error in getting all CharityCases: {}", exception.getMessage());
        }
        logger.info("No charity case found with id {}", id);
        return Optional.empty();
    }

    private CharityCase extractCharityCaseFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String caseName = resultSet.getString("caseName");
        String description = resultSet.getString("description");
        return new CharityCase(id, caseName, description);
    }
}
