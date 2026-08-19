package org.example.bankappuserservice.account.domain.exception;

public class OwnershipMismatchException extends RuntimeException {

    public OwnershipMismatchException(String userId) {
        super("Informed CPF does not match the user's CPF: " + userId);
    }
}