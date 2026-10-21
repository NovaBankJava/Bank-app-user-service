package org.example.bankappuserservice.userRegistration.infra.adapter.dtos.Reponse;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserResponseDTO(

        @NotBlank
        String name,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String phone) {



}
