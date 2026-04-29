package org.example.springbootweb.exceptionHandler.exceptions;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(String message) {
        super(message);
    }
}
