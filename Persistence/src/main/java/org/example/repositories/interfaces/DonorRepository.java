package org.example.repositories.interfaces;

import org.models.Donor;

import java.util.Optional;

public interface DonorRepository extends Repository<Long, Donor> {
    Iterable<Donor> findDonorByNameLike(String pattern);

    Optional<Donor> findDonorByPhoneNumber(String phoneNumber);
}
