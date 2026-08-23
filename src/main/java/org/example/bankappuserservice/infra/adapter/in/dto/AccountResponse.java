package org.example.bankappuserservice.infra.adapter.in.dto;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;

import java.time.Instant;

public record AccountResponse(
        String id,
        String bank,
        String branch,
        String accountNumber,
        AccountType type,
        Instant createdAt,
        boolean primary) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBank(),
                account.getBranch(),
                account.getAccountNumber(),
                account.getType(),
                account.getCreatedAt(),
                account.isPrimary());
    }
}