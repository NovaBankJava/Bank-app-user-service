package org.example.bankappuserservice.userRegistration.domain.exception;

public class InvalidCpfException extends RuntimeException {

    public InvalidCpfException (String message) {
        super(message);
    }
}
