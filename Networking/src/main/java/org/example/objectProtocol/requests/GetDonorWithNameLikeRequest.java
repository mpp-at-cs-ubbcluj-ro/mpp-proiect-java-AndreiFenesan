package org.example.objectProtocol.requests;

public class GetDonorWithNameLikeRequest implements Request {
    String nameContains;

    public GetDonorWithNameLikeRequest(String nameLike) {
        this.nameContains = nameLike;
    }

    public String getNameContains() {
        return nameContains;
    }
}
