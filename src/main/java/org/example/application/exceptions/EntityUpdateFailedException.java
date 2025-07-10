package org.example.application.exceptions;

public class EntityUpdateFailedException extends RuntimeException {
    public EntityUpdateFailedException(String meesage) {
        super(meesage);
    }
}
