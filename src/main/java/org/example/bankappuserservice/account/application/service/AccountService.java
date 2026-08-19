package org.example.bankappuserservice.account.application.service;

import java.util.List;
import org.example.bankappuserservice.account.application.port.in.CreateAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.DeleteAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.SetPrimaryAccountUseCase;
import org.example.bankappuserservice.account.application.port.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements CreateAccountUseCase, SetPrimaryAccountUseCase, DeleteAccountUseCase {

    private final AccountRepositoryPort repository;

    public AccountService(AccountRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Account create(CreateAccountCommand command) {
        Account account = Account.create(
                command.userId(), command.bank(), command.branch(),
                command.accountNumber(), command.type());

        boolean firstAccount = !repository.userHasAnyAccount(command.userId());

        if (firstAccount || command.setAsPrimary()) {
            repository.unmarkAllPrimary(command.userId());
            account.markAsPrimary();
        }

        return repository.save(account);
    }

    @Override
    public void setPrimary(String userId, String accountId) {
        Account account = findOwnedAccount(userId, accountId);
        repository.unmarkAllPrimary(userId);
        account.markAsPrimary();
        repository.save(account);
    }

    public List<Account> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    private Account findOwnedAccount(String userId, String accountId) {
        return repository.findById(accountId)
                .filter(account -> account.belongsTo(userId))
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void delete(String userId, String accountId) {
        Account account = findOwnedAccount(userId, accountId);
        repository.deleteById(account.getId());
    }

}

