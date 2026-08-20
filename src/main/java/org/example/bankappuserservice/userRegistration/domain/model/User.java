package org.example.bankappuserservice.userRegistration.domain.model;

import org.example.bankappuserservice.userRegistration.domain.exception.InvalidUserException;

public class User {

    private String id;
    private String name;
    private String phone;
    private Email email;
    private Cpf cpf;
    private String passwordHash;
    private AccountStatus accountStatus;

    public User(String name, String phone, Email email, Cpf cpf, String passwordHash) {
        if(name ==null || name.isBlank()) {
            throw new InvalidUserException("Name cannot be blank");}
        this.name = name;

        if(phone ==null || phone.isBlank()) {
            throw new InvalidUserException("Phone cannot be blank");}
        this.phone = phone;

        if(email ==null) {
            throw new InvalidUserException("Email is required");}
        this.email = email;

        if(cpf ==null) {
            throw new InvalidUserException("CPF is required");}
        this.cpf = cpf;

        if(passwordHash ==null || passwordHash.isBlank()) {
            throw new InvalidUserException("Password hash cannot be blank");}
        this.passwordHash = passwordHash;

        this.accountStatus = AccountStatus.PENDING_VERIFICATION;

    }
}
