package org.example.bankappuserservice.infra.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.hibernate.validator.constraints.br.CPF;

public record CreateAccountRequest(
        @NotBlank String userId,
        @NotBlank @CPF String cpf,
        @NotBlank String bank,
        @NotBlank String branch,
        @NotBlank String accountNumber,
        @NotNull AccountType type,
        boolean setAsPrimary) {
}