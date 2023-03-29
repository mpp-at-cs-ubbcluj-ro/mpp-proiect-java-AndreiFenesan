package org.example.validators;

public class ValidationError extends RuntimeException{
    public ValidationError(String message) {
        super(message);
    }
}
