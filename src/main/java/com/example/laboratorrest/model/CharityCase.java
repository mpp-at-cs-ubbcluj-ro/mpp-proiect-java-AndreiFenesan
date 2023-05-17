package com.example.laboratorrest.model;

//@Entity
public class CharityCase {
//    @Id
    private Long id;
    private String caseName;
    private String description;

    public CharityCase(Long id, String caseName, String description) {
        this.id = id;
        this.caseName = caseName;
        this.description = description;
    }

    public CharityCase(String caseName, String description) {
        this.id = -1L;
        this.caseName = caseName;
        this.description = description;
    }

    public CharityCase() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}