package com.example.client.model.dtos;
import com.example.client.model.Donor;

public class DonorDtoBuilder {
    public DonorDto buildFromDonor(Donor donor) {
        return new DonorDto(donor.getName(), donor.getMailAddress(), donor.getPhoneNumber());
    }
}
