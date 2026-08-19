package org.example.bankappuserservice.account.domain.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountId) {
        super("Account not found or not accessible: " + accountId);
    }
}
