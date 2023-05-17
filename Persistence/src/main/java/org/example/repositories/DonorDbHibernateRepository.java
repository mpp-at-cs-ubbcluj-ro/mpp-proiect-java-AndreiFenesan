package org.example.repositories;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.repositories.interfaces.DonorRepository;
import org.example.validators.Validator;
import org.models.Donor;

import java.util.*;

public class DonorDbHibernateRepository implements DonorRepository {
    private Validator<Donor> validator;
    private static final Logger logger = LogManager.getLogger();
    private final EntityManager donorEntityManager;

    public DonorDbHibernateRepository(Validator<Donor> validator, EntityManager donorEntityManager) {
        this.validator = validator;
        this.donorEntityManager = donorEntityManager;
    }

    @Override
    public Iterable<Donor> findDonorByNameLike(String pattern) {
        logger.info("searching for donor with name pattern: {}", pattern);
        try {
            return donorEntityManager.createQuery("from Donor where name ilike :likePattern", Donor.class)
                    .setParameter("likePattern", pattern)
                    .getResultList();
        } catch (Exception e) {
            logger.error(e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Donor> findDonorByPhoneNumber(String phoneNumber) {
        logger.info("searching for donor with phone number: {}", phoneNumber);
        Donor donor = null;
        try {
            donor = donorEntityManager.createQuery("from Donor where phoneNumber=:phone", Donor.class)
                    .setParameter("phone", phoneNumber)
                    .getSingleResult();
        } catch (Exception e) {
            logger.error(e);
        }
        if (donor != null) {
            return Optional.of(donor);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Donor> save(Donor entity) {
        logger.info("Saving {}", entity);
        logger.info("Validating {}", entity);
        this.validator.validate(entity);
        try {
            donorEntityManager.getTransaction().begin();
            donorEntityManager.persist(entity);
            donorEntityManager.getTransaction().commit();
            Optional<Donor> donor = findDonorByPhoneNumber(entity.getPhoneNumber());
            if (donor.isPresent()) {
                return donor;
            }
        } catch (Exception e) {
            logger.error("Error in inserting donor {}", entity);
            donorEntityManager.getTransaction().rollback();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Donor> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Donor> update(Donor entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Donor> findAll() {
        return null;
    }

    @Override
    public Optional<Donor> findOneById(Long aLong) {
        logger.info("Searching for donor with id {}", aLong);
        Donor donor = donorEntityManager.find(Donor.class, aLong);
        if (donor != null) {
            return Optional.of(donor);
        }
        return Optional.empty();
    }
}
