package org.example.bankappuserservice.userRegistration.application.service;

import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserUseCase;
import org.example.bankappuserservice.userRegistration.domain.ports.out.AccountConfirmationEmailPort;
import org.example.bankappuserservice.userRegistration.domain.ports.out.IdGeneratorPort;
import org.example.bankappuserservice.userRegistration.domain.ports.out.PasswordHasherPort;
import org.example.bankappuserservice.userRegistration.domain.ports.out.UserRepositoryPort;
import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.springframework.stereotype.Service;


import java.time.Instant;


@Service
public class CreateUserService implements CreateUserUseCase {

    private final PasswordHasherPort passwordHasherPort;
    private final UserRepositoryPort userRepositoryPort;
    private final IdGeneratorPort idGeneratorPort;
    private final AccountConfirmationEmailPort accountConfirmationEmailPort;

    public CreateUserService(AccountConfirmationEmailPort accountConfirmationEmailPort, PasswordHasherPort passwordHasherPort, UserRepositoryPort userRepositoryPort, IdGeneratorPort idGeneratorPort) {
        this.accountConfirmationEmailPort = accountConfirmationEmailPort;
        this.passwordHasherPort = passwordHasherPort;
        this.userRepositoryPort = userRepositoryPort;
        this.idGeneratorPort = idGeneratorPort;
    }

    @Override
    public User execute(CreateUserInput input) {

        String normalizeCpf = input.cpf().replaceAll("\\D", "");

        String normalizeEmail = input.email().toLowerCase();

        if (userRepositoryPort.existsByCpf(normalizeCpf)) {
            throw new CpfAlreadyExistsException("This CPF is already registered.");
        }

        if (userRepositoryPort.existsByEmail(normalizeEmail)) {
            throw new EmailAlreadyExistsException("This email is already registered.");
        }

        String passwordHash = passwordHasherPort.hash(input.password());

        Instant createdAt = Instant.now();

        String generatedId = idGeneratorPort.generateId();

        User user = new User(generatedId ,input.name(),input.phone(),normalizeEmail,normalizeCpf,passwordHash,createdAt);

        User savedUser = userRepositoryPort.save(user);

        accountConfirmationEmailPort.sendWelcomeEmail(savedUser.getEmail(),savedUser.getName());

        return  savedUser;


    }

}
