package org.example.bankappuserservice.userRegistration.application.port.in;


public record CreateUserInput(String name, String cpf, String email, String phone, String password) {

}


