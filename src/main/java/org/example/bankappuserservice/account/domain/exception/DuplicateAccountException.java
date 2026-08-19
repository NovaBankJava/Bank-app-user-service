package org.example.bankappuserservice.account.domain.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException() {
        super("Account already registered for this user");
    }
}