package org.example.bankappuserservice.userRegistration.infra.adapter.dtos.Request;

public record UserRequestDTO(
        String name,
        String cpf,
        String email,
        String phone,
        String password) {
}
