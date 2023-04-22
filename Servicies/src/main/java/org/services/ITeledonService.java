package org.services;


import org.models.Donor;
import org.models.dtos.CharityCaseDto;

public interface ITeledonService {
    Iterable<CharityCaseDto> getAllCharityCases();

    void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) throws ServiceException;

    Iterable<Donor> getDonorsWithNameContaining(String containsInName);

    boolean authenticateVolunteer(String username, String password);

    void logout();
}
