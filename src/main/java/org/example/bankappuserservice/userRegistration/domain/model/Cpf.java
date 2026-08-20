package org.example.bankappuserservice.userRegistration.domain.model;

import org.example.bankappuserservice.userRegistration.domain.exception.InvalidCpfException;

public class Cpf {

    private final String value;

    public Cpf(String value) {
       if(value ==null || value.length() !=11 || value.isBlank()) {
           throw new InvalidCpfException("CPF must contain exactly 11 digits");}
       if (!value.chars().allMatch(c -> Character.isDigit(c))) {
           throw new InvalidCpfException("CPF must contain only digits");}

       this.value = value;

    }

    public String getValue() {
        return value;
    }
}
