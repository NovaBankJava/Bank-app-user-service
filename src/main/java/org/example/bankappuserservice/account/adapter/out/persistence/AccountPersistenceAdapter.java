package org.example.bankappuserservice.account.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.example.bankappuserservice.account.application.port.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository jpaRepository;
    private final AccountPersistenceMapper mapper;

    public AccountPersistenceAdapter(AccountJpaRepository jpaRepository,
                                     AccountPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity saved = jpaRepository.save(mapper.toJpa(account));
        return mapper.toDomain(saved);
    }

    @Override
    public List<Account> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return jpaRepository.findById(accountId).map(mapper::toDomain);
    }

    @Override
    public boolean userHasAnyAccount(String userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void unmarkAllPrimary(String userId) {
        List<AccountJpaEntity> primaries =
                jpaRepository.findByUserIdAndPrimaryTrue(userId);
        primaries.forEach(entity -> entity.setPrimary(false));
        jpaRepository.saveAll(primaries);
    }

    @Override
    public void deleteById(String accountId) {
        jpaRepository.deleteById(accountId);
    }
}