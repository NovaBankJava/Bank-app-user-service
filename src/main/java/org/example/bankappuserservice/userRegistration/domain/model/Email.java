package org.example.bankappuserservice.userRegistration.domain.model;

import org.example.bankappuserservice.userRegistration.domain.exception.InvalidEmailException;

public class Email {

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email cannot be blank");
        }
        if (!isEmailValid(value)) {
            throw new InvalidEmailException("Provide a valid email address.");
        }

        this.value = value;

    }
     private boolean isEmailValid(String value){
         int arroba = value.indexOf("@");
         int ponto = value.indexOf(".", arroba + 1);
        return arroba != -1 && ponto != -1;
     }

    public String getValue() {
        return value;
    }
}
