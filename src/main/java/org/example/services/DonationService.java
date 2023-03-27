package org.example.services;

import org.example.models.Donation;
import org.example.models.Donor;
import org.example.repositories.interfaces.CharityCaseRepository;
import org.example.repositories.interfaces.DonationRepository;
import org.example.repositories.interfaces.DonorRepository;

public class DonationService {
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final CharityCaseRepository charityCaseRepository;

    public DonationService(DonationRepository donationRepository, DonorRepository donorRepository, CharityCaseRepository charityCaseRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.charityCaseRepository = charityCaseRepository;
    }

    public void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) throws ServiceException {
        if (charityCaseRepository.findOneById(charityCaseId).isEmpty()) {
            throw new ServiceException("No charity case found with id: " + charityCaseId);
        }
        Donor donor = new Donor(name, emailAddress, phoneNumber);
        Donation donation = new Donation(donationAmount, charityCaseId, donor);
        if (donorRepository.findDonorByPhoneNumber(phoneNumber).isEmpty()) {
            //the donor does not exist.
            donorRepository.save(donor);
        }
        donationRepository.save(donation);
    }
}
