package org.example.bankappuserservice.account.adapter.in.web.dto;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;

public record AccountResponse(
        String id,
        String bank,
        String branch,
        String accountNumber,
        AccountType type,
        boolean primary) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBank(),
                account.getBranch(),
                account.getAccountNumber(),
                account.getType(),
                account.isPrimary());
    }
}