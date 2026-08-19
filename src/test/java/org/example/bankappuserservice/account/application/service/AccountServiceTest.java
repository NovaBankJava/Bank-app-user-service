package org.example.bankappuserservice.account.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.bankappuserservice.account.application.port.in.CreateAccountUseCase.CreateAccountCommand;
import org.example.bankappuserservice.account.application.port.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private final String userId = UUID.randomUUID().toString();

    @Test
    void firstAccountBecomesPrimaryAutomatically() {
        AccountService service = new AccountService(new InMemoryAccountRepository());

        Account created = service.create(new CreateAccountCommand(
                userId, "NovaBank", "0001", "123456", AccountType.CHECKING, false));

        assertThat(created.isPrimary()).isTrue();
    }

    @Test
    void secondAccountWithoutRequestingPrimaryStaysNonPrimary() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        service.create(new CreateAccountCommand(
                userId, "NovaBank", "0001", "111111", AccountType.CHECKING, false));

        Account second = service.create(new CreateAccountCommand(
                userId, "NovaBank", "0002", "222222", AccountType.SAVINGS, false));

        assertThat(second.isPrimary()).isFalse();
    }

    @Test
    void setPrimarySwitchesThePrimaryAccount() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        service.create(new CreateAccountCommand(
                userId, "NovaBank", "0001", "111111", AccountType.CHECKING, false));
        Account second = service.create(new CreateAccountCommand(
                userId, "NovaBank", "0002", "222222", AccountType.SAVINGS, false));

        service.setPrimary(userId, second.getId());

        List<Account> accounts = service.findByUserId(userId);
        assertThat(accounts).filteredOn(Account::isPrimary)
                .extracting(Account::getId)
                .containsExactly(second.getId());
    }

    @Test
    void setPrimaryOnAccountOfAnotherUserFails() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        Account account = service.create(new CreateAccountCommand(
                userId, "NovaBank", "0001", "111111", AccountType.CHECKING, false));

        assertThatThrownBy(() -> service.setPrimary("another-user", account.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteRemovesTheUsersAccount() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        Account account = service.create(new CreateAccountCommand(
                userId, "NovaBank", "0001", "111111", AccountType.CHECKING, false));

        service.delete(userId, account.getId());

        assertThat(service.findByUserId(userId)).isEmpty();
    }

    @Test
    void deleteNonExistentAccountFails() {
        AccountService service = new AccountService(new InMemoryAccountRepository());

        assertThatThrownBy(() -> service.delete(userId, "does-not-exist"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    static class InMemoryAccountRepository implements AccountRepositoryPort {
        private final List<Account> data = new ArrayList<>();

        @Override
        public Account save(Account account) {
            data.removeIf(a -> a.getId().equals(account.getId()));
            data.add(account);
            return account;
        }

        @Override
        public List<Account> findByUserId(String userId) {
            return data.stream().filter(a -> a.belongsTo(userId)).toList();
        }

        @Override
        public Optional<Account> findById(String accountId) {
            return data.stream().filter(a -> a.getId().equals(accountId)).findFirst();
        }

        @Override
        public boolean userHasAnyAccount(String userId) {
            return data.stream().anyMatch(a -> a.belongsTo(userId));
        }

        @Override
        public void unmarkAllPrimary(String userId) {
            data.stream().filter(a -> a.belongsTo(userId)).forEach(Account::unmarkAsPrimary);
        }

        @Override
        public void deleteById(String accountId) {
            data.removeIf(a -> a.getId().equals(accountId));
        }
    }
}