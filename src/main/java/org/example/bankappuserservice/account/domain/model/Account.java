package org.example.bankappuserservice.account.domain.model;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Account {

    private final String id;
    private final String userId;
    private final String bank;
    private final String branch;
    private final String accountNumber;
    private final AccountType type;
    private final Instant createdAt;
    private boolean primary;

    public Account(String id, String userId, String bank, String branch,
                   String accountNumber, AccountType type, Instant createdAt, boolean primary) {
        this.id = id;
        this.userId = userId;
        this.bank = requireText(bank, "bank");
        this.branch = requireText(branch, "branch");
        this.accountNumber = requireText(accountNumber, "accountNumber");
        this.type = type;
        this.createdAt = createdAt;
        this.primary = primary;
    }

    public static Account create(String userId, String bank, String branch,
                                 String accountNumber, AccountType type) {
        return new Account(UUID.randomUUID().toString(), userId, bank, branch,
                accountNumber, type, Instant.now(), false);
    }

    public void markAsPrimary() {
        this.primary = true;
    }

    public void unmarkAsPrimary() {
        this.primary = false;
    }

    public boolean belongsTo(String userId) {
        return this.userId.equals(userId);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}