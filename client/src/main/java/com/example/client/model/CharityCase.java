package com.example.client.model;

import java.io.Serializable;

public class CharityCase extends Entity<Long> implements Serializable{
    private String caseName;
    private String description;

    public CharityCase(Long aLong, String caseName, String description) {
        super(aLong);
        this.caseName = caseName;
        this.description = description;
    }

    public CharityCase(String caseName, String description) {
        super(null);
        this.caseName = caseName;
        this.description = description;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CharityCase{" +
                "caseName='" + caseName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
