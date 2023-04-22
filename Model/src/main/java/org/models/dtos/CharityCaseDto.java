package org.models.dtos;


import java.io.Serializable;

public class CharityCaseDto implements Serializable {
    private final String caseName;
    private String description;
    private Double totalAmountCollected;
    private Long id;

    public CharityCaseDto(Long id, String caseName, String description, Double totalAmountCollected) {
        this.caseName = caseName;
        this.totalAmountCollected = totalAmountCollected;
        this.description = description;
        this.id = id;
    }


    public Double getTotalAmountCollected() {
        return totalAmountCollected;
    }

    public void setTotalAmountCollected(Double totalAmountCollected) {
        this.totalAmountCollected = totalAmountCollected;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
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
