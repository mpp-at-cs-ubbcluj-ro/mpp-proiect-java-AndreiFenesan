package org.example.services;

import org.example.models.Donor;
import org.example.repositories.interfaces.DonorRepository;

public class DonorService {
    private DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Iterable<Donor> getDonorsWithNameContaining(String containsInName) {
        if (containsInName == null || containsInName.equals("")) {
            throw new ServiceException("Invalid string. The value should be not null and not empty");
        }
        String pattern = "%" + containsInName + "%";
        return donorRepository.findDonorByNameLike(pattern);
    }
}
