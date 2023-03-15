package org.example.models;

public class Donation extends Entity<Long> {
    private double amount;
    private Long donorId;

    public Donation(Long donorId, double amount) {
        super(null);
        this.amount = amount;
        this.donorId = donorId;
    }

    public Donation(Long id, Long donorId, double amount) {
        super(id);
        this.amount = amount;
        this.donorId = donorId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
}
