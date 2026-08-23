package org.example.bankappuserservice.infra.adapter.out;

import lombok.RequiredArgsConstructor;
import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.ports.out.AccountRepositoryPort;
import org.example.bankappuserservice.infra.adapter.out.mapper.AccountOutMapper;
import org.example.bankappuserservice.infra.repository.AccountRepository;
import org.example.bankappuserservice.infra.repository.entity.AccountEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountAdapter implements AccountRepositoryPort {

    private final AccountRepository jpaRepository;
    private final AccountOutMapper mapper;

    @Override
    public Account save(Account account) {
        AccountEntity saved = jpaRepository.save(mapper.toJpa(account));
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
    public boolean existsByUserIdAndBankAndBranchAndAccountNumber(
            String userId, String bank, String branch, String accountNumber) {
        return jpaRepository.existsByUserIdAndBankAndBranchAndAccountNumber(
                userId, bank, branch, accountNumber);
    }

    @Override
    public void unmarkAllPrimary(String userId) {
        List<AccountEntity> primaries = jpaRepository.findByUserIdAndPrimaryTrue(userId);
        primaries.forEach(entity -> entity.setPrimary(false));
        jpaRepository.saveAll(primaries);
    }

    @Override
    public void deleteById(String accountId) {
        jpaRepository.deleteById(accountId);
    }
}