package org.example.bankappuserservice.userRegistration.application.service;

import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.port.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.application.port.in.CreateUserUseCase;
import org.example.bankappuserservice.userRegistration.application.port.out.PasswordHasherPort;
import org.example.bankappuserservice.userRegistration.application.port.out.UserRepositoryPort;
import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.springframework.stereotype.Service;


import java.time.Instant;


@Service
public class CreateUserService implements CreateUserUseCase {

    private final PasswordHasherPort passwordHasherPort;
    private final UserRepositoryPort userRepositoryPort;

    public CreateUserService(

            PasswordHasherPort passwordHasherPort,
            UserRepositoryPort userRepositoryPort
    ) {
        this.passwordHasherPort = passwordHasherPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User create(CreateUserInput input) {


        if (userRepositoryPort.existsByCpf(input.cpf())) {
            throw new CpfAlreadyExistsException("This CPF is already registered.");
        }

        if (userRepositoryPort.existsByEmail(input.email())) {
            throw new EmailAlreadyExistsException("This email is already registered.");
        }

        String passwordHash = passwordHasherPort.hash(input.password());

        Instant createdAt = Instant.now();

        User user = new User( input.name(),input.phone(),input.email(),input.cpf(),passwordHash,createdAt);

        return userRepositoryPort.save(user);

    }

}
