package org.example.bankappuserservice.userRegistration.domain.ports.in;


public record CreateUserInput(
        String name,
        String cpf,
        String email,
        String phone,
        String password) {

}


