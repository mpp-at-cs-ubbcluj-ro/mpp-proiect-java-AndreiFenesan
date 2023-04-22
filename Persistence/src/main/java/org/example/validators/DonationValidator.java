package org.example.validators;

import org.models.Donation;
import org.services.ValidationError;

public class DonationValidator implements Validator<Donation> {
    @Override
    public void validate(Donation donation) throws ValidationError {
        if (donation.getDonor() == null) {
            throw new ValidationError("Donor is null");
        }
        if (donation.getAmount() < 1) {
            throw new ValidationError("Amount must be a value grater than 1");
        }
    }

}
