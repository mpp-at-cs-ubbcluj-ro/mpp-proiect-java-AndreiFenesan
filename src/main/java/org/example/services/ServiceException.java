package org.example.services;

public class ServiceException extends RuntimeException{
    public ServiceException(String message) {
        super(message);
    }
}
