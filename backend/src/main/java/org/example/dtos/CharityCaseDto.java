package org.example.dtos;

import org.example.models.CharityCase;
import org.example.models.Donation;

public class CharityCaseDto {
    private Long id;
    private String caseName;
    private String description;
    private Double totalAmountCollected;

    public CharityCaseDto(Long id, String caseName, String description, Double totalAmountCollected) {
        this.id = id;
        this.caseName = caseName;
        this.description = description;
        this.totalAmountCollected = totalAmountCollected;
    }

    public Double getTotalAmountCollected() {
        return totalAmountCollected;
    }

    public void setTotalAmountCollected(Double totalAmountCollected) {
        this.totalAmountCollected = totalAmountCollected;
    }

    public Long getId() {
        return id;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getDescription() {
        return description;
    }
}
