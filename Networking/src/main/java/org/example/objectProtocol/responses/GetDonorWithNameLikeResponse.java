package org.example.objectProtocol.responses;

import org.models.Donor;
import org.models.dtos.DonorDto;

import java.util.List;

public class GetDonorWithNameLikeResponse implements Response{
    Iterable<DonorDto> donors;

    public GetDonorWithNameLikeResponse(Iterable<DonorDto> donors) {
        this.donors = donors;
    }

    public Iterable<DonorDto> getDonors() {
        return donors;
    }
}
