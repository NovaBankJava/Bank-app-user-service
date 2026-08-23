package org.example.bankappuserservice.account.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.bankappuserservice.account.domain.ports.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.exception.DuplicateAccountException;
import org.example.bankappuserservice.account.domain.exception.InvalidCpfException;
import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private static final String CPF = "52998224725";

    private final String userId = UUID.randomUUID().toString();
    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountService(new InMemoryAccountRepository());
    }

    @Test
    void firstAccountBecomesPrimaryAutomatically() {
        Account created = create(AccountType.CHECKING, "111111", false);
        assertThat(created.isPrimary()).isTrue();
    }

    @Test
    void secondAccountWithoutRequestingPrimaryStaysNonPrimary() {
        create(AccountType.CHECKING, "111111", false);
        Account second = create(AccountType.SAVINGS, "222222", false);
        assertThat(second.isPrimary()).isFalse();
    }

    @Test
    void creatingAccountWithInvalidCpfFails() {
        assertThatThrownBy(() -> service.createAccount(
                userId, "111", "NovaBank", "0001", "111111", AccountType.CHECKING, false))
                .isInstanceOf(InvalidCpfException.class);
    }

    @Test
    void creatingDuplicateAccountFails() {
        create(AccountType.CHECKING, "111111", false);
        assertThatThrownBy(() -> service.createAccount(
                userId, CPF, "NovaBank", "0001", "111111", AccountType.CHECKING, false))
                .isInstanceOf(DuplicateAccountException.class);
    }

    @Test
    void creatingAccountWithBlankUserIdFails() {
        assertThatThrownBy(() -> service.createAccount(
                "  ", CPF, "NovaBank", "0001", "111111", AccountType.CHECKING, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setPrimarySwitchesThePrimaryAccount() {
        create(AccountType.CHECKING, "111111", false);
        Account second = create(AccountType.SAVINGS, "222222", false);

        service.setPrimaryAccount(userId, second.getId());

        List<Account> accounts = service.findByUserId(userId);
        assertThat(accounts).filteredOn(Account::isPrimary)
                .extracting(Account::getId)
                .containsExactly(second.getId());
    }

    @Test
    void setPrimaryOnAccountOfAnotherUserFails() {
        Account account = create(AccountType.CHECKING, "111111", false);
        assertThatThrownBy(() -> service.setPrimaryAccount(UUID.randomUUID().toString(), account.getId()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteRemovesTheUsersAccount() {
        Account account = create(AccountType.CHECKING, "111111", false);
        service.deleteAccount(userId, account.getId());
        assertThat(service.findByUserId(userId)).isEmpty();
    }

    @Test
    void deleteNonExistentAccountFails() {
        assertThatThrownBy(() -> service.deleteAccount(userId, "does-not-exist"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    private Account create(AccountType type, String number, boolean primary) {
        return service.createAccount(userId, CPF, "NovaBank", "0001", number, type, primary);
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
        public boolean existsByUserIdAndBankAndBranchAndAccountNumber(
                String userId, String bank, String branch, String accountNumber) {
            return data.stream().anyMatch(a ->
                    a.belongsTo(userId)
                            && a.getBank().equals(bank)
                            && a.getBranch().equals(branch)
                            && a.getAccountNumber().equals(accountNumber));
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