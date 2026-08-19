package org.example.bankappuserservice.account.adapter.out.persistence;

import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {

    public AccountJpaEntity toJpa(Account account) {
        return new AccountJpaEntity(
                account.getId(),
                account.getUserId(),
                account.getBank(),
                account.getBranch(),
                account.getAccountNumber(),
                account.getType(),
                account.getCreatedAt(),
                account.isPrimary());
    }

    public Account toDomain(AccountJpaEntity entity) {
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