package org.example.services;

import org.example.dtos.CharityCaseDto;
import org.example.models.CharityCase;
import org.example.repositories.interfaces.CharityCaseRepository;
import org.example.repositories.interfaces.DonationRepository;

import java.util.ArrayList;
import java.util.List;

public class CharityCaseService {
    private final CharityCaseRepository charityCaseRepository;
    private final DonationRepository donationRepository;

    public CharityCaseService(CharityCaseRepository charityCaseRepository, DonationRepository donationRepository) {
        this.charityCaseRepository = charityCaseRepository;
        this.donationRepository = donationRepository;
    }

    public Iterable<CharityCaseDto> getAllCharityCases() {
        Iterable<CharityCase> charityCases = charityCaseRepository.findAll();
        List<CharityCaseDto> charityCasesDto = new ArrayList<>();
        charityCases.forEach(charityCase -> {
            Double totalAmount = donationRepository.getTotalAmountOfRaisedMoney(charityCase.getId());
            charityCasesDto.add(
                    new CharityCaseDto(charityCase.getId(), charityCase.getCaseName(), charityCase.getDescription(), totalAmount));
        });
        return charityCasesDto;
    }

}
