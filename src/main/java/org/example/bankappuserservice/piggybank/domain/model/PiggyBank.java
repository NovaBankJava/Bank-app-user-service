package org.example.bankappuserservice.piggybank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.bankappuserservice.piggybank.domain.exception.PiggyBankDomainException;

public class PiggyBank {

    // Identificadores em String (conforme alinhado)
    private final String id;
    private final String userId;
    private final String accountId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentBalance;
    private final LocalDateTime createdAt;

    public PiggyBank(String id, String userId, String accountId, String name, BigDecimal targetAmount, BigDecimal currentBalance, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentBalance = (currentBalance != null) ? currentBalance : BigDecimal.ZERO;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }

    // Regra RF03: Não permitir resgate se não houver saldo suficiente
    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);

        if (this.currentBalance.compareTo(amount) < 0) {
            throw new PiggyBankDomainException("Saldo insuficiente no cofrinho para realizar o resgate.");
        }

        this.currentBalance = this.currentBalance.subtract(amount);
    }

    // Regra de depósito
    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.currentBalance = this.currentBalance.add(amount);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PiggyBankDomainException("O valor da operação deve ser maior que zero.");
        }
    }

    // Getters para consultar os dados
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getAccountId() { return accountId; }
    public String getName() { return name; }
    public BigDecimal getTargetAmount() { return targetAmount; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}