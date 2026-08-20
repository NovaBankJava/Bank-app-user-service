package org.example.bankappuserservice.userRegistration.domain.model;

import org.example.bankappuserservice.userRegistration.domain.exception.InvalidPasswordException;

public class Password {

    private final String value;

    public String getValue() {
        return value;
    }

    public Password(String value) {
        if(value == null || value.isBlank() || value.length() < 8) {
            throw new InvalidPasswordException("Password must contain at least 8 caracters");
        }
        this.value = value;


    }
}
