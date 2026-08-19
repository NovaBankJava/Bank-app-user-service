package org.example.bankappuserservice.account.adapter.in.web.dto;

import org.example.bankappuserservice.account.application.port.in.CreateAccountUseCase.CreateAccountCommand;
import org.example.bankappuserservice.account.domain.model.AccountType;

public record CreateAccountRequest(
        String bank,
        String branch,
        String accountNumber,
        AccountType type,
        boolean setAsPrimary) {

    public CreateAccountCommand toCommand(String userId) {
        return new CreateAccountCommand(userId, bank, branch, accountNumber, type, setAsPrimary);
    }
}