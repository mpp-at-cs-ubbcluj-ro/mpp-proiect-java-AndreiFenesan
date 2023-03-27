package org.example.dtos;

import org.example.models.CharityCase;
import org.example.models.Donation;

public class CharityCaseDto {
    private CharityCase charityCase;
    private Double totalAmountCollected;

    public CharityCaseDto(CharityCase charityCase, Double totalAmountCollected) {
        this.charityCase = charityCase;
        this.totalAmountCollected = totalAmountCollected;
    }

    public CharityCase getCharityCase() {
        return charityCase;
    }

    public void setCharityCase(CharityCase charityCase) {
        this.charityCase = charityCase;
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
                "charityCase=" + charityCase +
                ", totalAmountCollected=" + totalAmountCollected +
                '}';
    }
}
