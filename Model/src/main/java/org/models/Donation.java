package org.models;

public class Donation extends Entity<Long> {
    private double amount;
    private Long charityCaseId;
    private Donor donor;

    public Donation(Long aLong, double amount, Long charityCaseId, Donor donor) {
        super(aLong);
        this.amount = amount;
        this.charityCaseId = charityCaseId;
        this.donor = donor;
    }

    public Donation(double amount, Long charityCaseId, Donor donor) {
        super(null);
        this.amount = amount;
        this.charityCaseId = charityCaseId;
        this.donor = donor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Long getCharityCaseId() {
        return charityCaseId;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "amount=" + amount +
                ", charityCaseId=" + charityCaseId +
                ", donor=" + donor +
                ", id=" + id +
                '}';
    }

    public void setCharityCaseId(Long charityCaseId) {
        this.charityCaseId = charityCaseId;
    }
}
