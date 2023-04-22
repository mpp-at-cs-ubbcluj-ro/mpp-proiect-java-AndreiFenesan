package com.example.client.servicies;

import com.example.client.model.Donor;
import com.example.client.model.dtos.CharityCaseDto;

public interface ITeledonService {
    Iterable<CharityCaseDto> getAllCharityCases();

    void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount);

    Iterable<Donor> getDonorsWithNameContaining(String containsInName);

    boolean authenticateVolunteer(String username, String password);
}
