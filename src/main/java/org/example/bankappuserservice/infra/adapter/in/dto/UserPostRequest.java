package org.example.bankappuserservice.infra.adapter.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserPostRequest(
        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @CPF
        String cpf,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}
