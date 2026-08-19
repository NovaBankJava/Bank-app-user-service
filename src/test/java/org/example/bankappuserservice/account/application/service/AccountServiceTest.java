package org.example.bankappuserservice.account.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.example.bankappuserservice.account.application.port.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.application.port.out.UserLookupPort;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.exception.DuplicateAccountException;
import org.example.bankappuserservice.account.domain.exception.InvalidCpfException;
import org.example.bankappuserservice.account.domain.exception.OwnershipMismatchException;
import org.example.bankappuserservice.account.domain.exception.SalaryAccountNotAllowedForMinorException;
import org.example.bankappuserservice.account.domain.exception.UserNotFoundException;
import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private static final String CPF = "52998224725";

    private final String userId = UUID.randomUUID().toString();
    private FakeUserLookup userLookup;
    private AccountService service;

    @BeforeEach
    void setUp() {
        userLookup = new FakeUserLookup();
        userLookup.addAdult(userId, CPF);
        service = new AccountService(new InMemoryAccountRepository(), userLookup);
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
    void creatingAccountWithWrongCpfFails() {
        assertThatThrownBy(() -> service.createAccount(
                userId, "11144477735", "NovaBank", "0001", "111111", AccountType.CHECKING, false))
                .isInstanceOf(OwnershipMismatchException.class);
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
    void minorCannotCreateSalaryAccount() {
        String minorId = UUID.randomUUID().toString();
        userLookup.addMinor(minorId, CPF);

        assertThatThrownBy(() -> service.createAccount(
                minorId, CPF, "NovaBank", "0001", "111111", AccountType.SALARY, false))
                .isInstanceOf(SalaryAccountNotAllowedForMinorException.class);
    }

    @Test
    void minorCanCreateNonSalaryAccount() {
        String minorId = UUID.randomUUID().toString();
        userLookup.addMinor(minorId, CPF);

        Account created = service.createAccount(
                minorId, CPF, "NovaBank", "0001", "111111", AccountType.SAVINGS, false);

        assertThat(created.isPrimary()).isTrue();
    }

    @Test
    void creatingAccountForUnknownUserFails() {
        assertThatThrownBy(() -> service.createAccount(
                UUID.randomUUID().toString(), CPF, "NovaBank", "0001", "111111", AccountType.CHECKING, false))
                .isInstanceOf(UserNotFoundException.class);
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
        String otherId = UUID.randomUUID().toString();
        userLookup.addAdult(otherId, CPF);

        assertThatThrownBy(() -> service.setPrimaryAccount(otherId, account.getId()))
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

    static class FakeUserLookup implements UserLookupPort {
        private final Map<String, UserData> users = new HashMap<>();

        void addAdult(String userId, String cpf) {
            users.put(userId, new UserData(userId, cpf, LocalDate.now().minusYears(30)));
        }

        void addMinor(String userId, String cpf) {
            users.put(userId, new UserData(userId, cpf, LocalDate.now().minusYears(10)));
        }

        @Override
        public Optional<UserData> findByUserId(String userId) {
            return Optional.ofNullable(users.get(userId));
        }
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