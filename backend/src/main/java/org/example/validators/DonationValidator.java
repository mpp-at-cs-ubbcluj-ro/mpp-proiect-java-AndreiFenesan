package org.example.validators;

import org.example.models.Donation;
import org.example.models.Donor;

public class DonationValidator implements Validator<Donation> {
    @Override
    public void validate(Donation donation) {
        if (donation.getDonor() == null) {
            throw new ValidationError("Donor is null");
        }
        if (donation.getAmount() < 1) {
            throw new ValidationError("Amount must be a value grater than 1");
        }
    }

}
