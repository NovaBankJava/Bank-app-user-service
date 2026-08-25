package org.example.bankappuserservice.domain.model;

public record CreateUserCommand(
        String username,
        String email,
        String firstName,
        String lastName,
        String password
) {
}