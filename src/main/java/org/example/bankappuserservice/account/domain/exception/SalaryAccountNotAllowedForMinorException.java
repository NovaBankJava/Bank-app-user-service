package org.example.bankappuserservice.account.domain.exception;

public class SalaryAccountNotAllowedForMinorException extends RuntimeException {

    public SalaryAccountNotAllowedForMinorException(String userId) {
        super("Minor user cannot have a salary account: " + userId);
    }
}