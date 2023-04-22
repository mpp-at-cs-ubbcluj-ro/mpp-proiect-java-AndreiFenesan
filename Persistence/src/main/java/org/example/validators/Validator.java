package org.example.validators;

import org.services.ValidationError;

public interface Validator<E> {
    public void validate(E entity) throws ValidationError;
}
