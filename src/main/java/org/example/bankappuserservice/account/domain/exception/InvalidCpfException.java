package org.example.bankappuserservice.account.domain.exception;

public class InvalidCpfException extends RuntimeException {

    public InvalidCpfException() {
        super("Invalid CPF format");
    }
}