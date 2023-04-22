package org.example.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.repositories.interfaces.CharityCaseRepository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.models.CharityCase;
public class CharityCaseDbRepository implements CharityCaseRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public CharityCaseDbRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Optional<CharityCase> save(CharityCase entity) {
        return Optional.empty();
    }

    @Override
    public Optional<CharityCase> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<CharityCase> update(CharityCase entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<CharityCase> findAll() {
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

    @Override
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
