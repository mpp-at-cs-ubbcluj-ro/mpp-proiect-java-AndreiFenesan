package org.example.validators;

import org.models.Donor;
import org.services.ValidationError;

public class DonorValidator implements Validator<Donor> {
    @Override
    public void validate(Donor donor) throws ValidationError {
        checkForNullnessAndEmptyStrings(donor);
        int phoneNumberLength = donor.getPhoneNumber().length();
        if (phoneNumberLength < 7 || phoneNumberLength > 18) {
            throw new ValidationError("Invalid phone number");
        }
        if (donor.getMailAddress().length() < 5 || !donor.getMailAddress().contains("@")) {
            throw new ValidationError("Invalid email address");
        }
    }

    private void checkForNullnessAndEmptyStrings(Donor donor) throws ValidationError {
        if (isNullOrEmpty(donor.getName())) {
            throw new ValidationError("Invalid name");
        }
        if (isNullOrEmpty(donor.getMailAddress())) {
            throw new ValidationError("Invalid email address");
        }
        if (isNullOrEmpty(donor.getPhoneNumber())) {
            throw new ValidationError("Invalid phone number");
        }
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
