package org.example.bankappuserservice.userRegistration.domain.exception;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }
}
