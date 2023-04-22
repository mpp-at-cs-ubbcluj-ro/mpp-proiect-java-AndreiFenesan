package com.example.client.Requests;

public class GetDonorWithNameLikeRequest implements Request {
    String nameContains;

    public GetDonorWithNameLikeRequest(String nameLike) {
        this.nameContains = nameLike;
    }

    public String getNameContains() {
        return nameContains;
    }
}
