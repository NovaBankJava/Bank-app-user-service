package org.example.bankappuserservice.application;

import org.example.bankappuserservice.domain.exception.InvalidUserException;
import org.example.bankappuserservice.domain.model.CreateUserCommand;
import org.example.bankappuserservice.domain.model.User;
import org.example.bankappuserservice.domain.ports.in.UserInboundPort;
import org.example.bankappuserservice.domain.ports.out.PasswordEncoderPort;
import org.example.bankappuserservice.domain.ports.out.UserOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUseCase implements UserInboundPort {

    private static final int USER_ID_LENGTH = 36;
    private static final String USER_ID_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserOutputPort userOutputPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User createUser(CreateUserCommand command) {
        validateUser(command);

        log.info("Creating user: {}", command.username());

        var user = User.builder()
                .id(generateRandomUserId())
                .username(command.username())
                .email(command.email())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .passwordHash(passwordEncoderPort.encode(command.password()))
                .createdAt(Instant.now())
                .build();

        var save = userOutputPort.save(user);

        log.info("User created with ID: {}", save.getId());

        return save;
    }

    private void validateUser(CreateUserCommand command) {
        if (command == null) {
            throw new InvalidUserException("User data is required");
        }

        if (command.password() == null || command.password().isBlank()) {
            throw new InvalidUserException("Password is required");
        }
    }

    private String generateRandomUserId() {
        StringBuilder id = new StringBuilder(USER_ID_LENGTH);

        for (int i = 0; i < USER_ID_LENGTH; i++) {
            id.append(USER_ID_CHARS.charAt(RANDOM.nextInt(USER_ID_CHARS.length())));
        }

        return id.toString();
    }
}
