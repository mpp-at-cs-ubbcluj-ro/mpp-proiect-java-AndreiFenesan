package com.example.client.model;


public class Donor extends Entity<Long> {
    private String name;
    private String mailAddress;
    private String phoneNumber;

    public Donor(String name, String mailAddress, String phoneNumber) {
        super(null);
        this.name = name;
        this.mailAddress = mailAddress;
        this.phoneNumber = phoneNumber;
    }

    public Donor(Long id, String name, String mailAddress, String phoneNumber) {
        super(id);
        this.name = name;
        this.mailAddress = mailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Donor{" +
                "name='" + name + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id=" + id +
                '}';
    }
}
