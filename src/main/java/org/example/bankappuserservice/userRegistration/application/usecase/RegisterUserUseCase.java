package org.example.bankappuserservice.userRegistration.application.usecase;

import org.example.bankappuserservice.userRegistration.domain.model.User;

import java.time.LocalDateTime;

public class RegisterUserUseCase {

    public User execute(String name, String phone, String email, String cpf, String passwordHash) {
        return new User(name, phone, email, cpf, passwordHash, LocalDateTime.now()
        );
    }
}
