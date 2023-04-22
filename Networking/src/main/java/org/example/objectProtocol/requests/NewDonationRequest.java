package org.example.objectProtocol.requests;

public class NewDonationRequest implements Request {
    private final Long charityCaseId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final Double amount;

    public NewDonationRequest(Long charityCaseId, String name, String email, String phoneNumber, Double amount) {
        this.charityCaseId = charityCaseId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }

    public Long getCharityCaseId() {
        return charityCaseId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getAmount() {
        return amount;
    }
}
