package org.models.dtos;

import org.models.Donor;

public class DonorDtoBuilder {
    public DonorDto buildFromDonor(Donor donor) {
        return new DonorDto(donor.getId(), donor.getName(), donor.getMailAddress(), donor.getPhoneNumber());
    }

    public Donor buildDonorFromDto(DonorDto donorDto) {
        return new Donor(donorDto.getId(), donorDto.getName(), donorDto.getEmail(), donorDto.getPhone());
    }
}
