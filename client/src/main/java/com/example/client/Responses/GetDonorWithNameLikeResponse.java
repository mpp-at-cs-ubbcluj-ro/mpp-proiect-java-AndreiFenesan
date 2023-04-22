package com.example.client.Responses;



import com.example.client.model.dtos.DonorDto;


public class GetDonorWithNameLikeResponse implements Response{
    Iterable<DonorDto> donors;

    public GetDonorWithNameLikeResponse(Iterable<DonorDto> donors) {
        this.donors = donors;
    }

    public Iterable<DonorDto> getDonors() {
        return donors;
    }
}
