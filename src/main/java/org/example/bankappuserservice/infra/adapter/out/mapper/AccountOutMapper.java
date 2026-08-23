package org.example.bankappuserservice.infra.adapter.out.mapper;

import org.example.bankappuserservice.infra.repository.entity.AccountEntity;
import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountOutMapper {

    public AccountEntity toJpa(Account account) {
        return new AccountEntity(
                account.getId(),
                account.getUserId(),
                account.getBank(),
                account.getBranch(),
                account.getAccountNumber(),
                account.getType(),
                account.getCreatedAt(),
                account.isPrimary());
    }

    public Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getUserId(),
                entity.getBank(),
                entity.getBranch(),
                entity.getAccountNumber(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.isPrimary());
    }
}