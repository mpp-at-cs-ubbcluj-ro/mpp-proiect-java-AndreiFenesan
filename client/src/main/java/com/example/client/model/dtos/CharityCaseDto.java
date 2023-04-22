package com.example.client.model.dtos;


import java.io.Serializable;

public class CharityCaseDto implements Serializable {
    private final String caseName;
    private String description;
    private Double totalAmountCollected;

    public CharityCaseDto(String caseName, String description, Double totalAmountCollected) {
        this.caseName = caseName;
        this.totalAmountCollected = totalAmountCollected;
        this.description = description;
    }


    public Double getTotalAmountCollected() {
        return totalAmountCollected;
    }

    public void setTotalAmountCollected(Double totalAmountCollected) {
        this.totalAmountCollected = totalAmountCollected;
    }

    @Override
    public String toString() {
        return "CharityCaseDto{" +
                "caseName='" + caseName + '\'' +
                ", description='" + description + '\'' +
                ", totalAmountCollected=" + totalAmountCollected +
                '}';
    }
}
