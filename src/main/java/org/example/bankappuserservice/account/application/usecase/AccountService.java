package org.example.bankappuserservice.account.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankappuserservice.account.domain.ports.in.CreateAccountUseCase;
import org.example.bankappuserservice.account.domain.ports.in.DeleteAccountUseCase;
import org.example.bankappuserservice.account.domain.ports.in.SetPrimaryAccountUseCase;
import org.example.bankappuserservice.account.domain.ports.out.AccountRepositoryPort;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.exception.DuplicateAccountException;
import org.example.bankappuserservice.account.domain.exception.InvalidCpfException;
import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.example.bankappuserservice.account.domain.validation.CpfValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService
        implements CreateAccountUseCase, SetPrimaryAccountUseCase, DeleteAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Account createAccount(String userId, String cpf, String bank, String branch,
                                 String accountNumber, AccountType type, boolean setAsPrimary) {
        log.info("Creating account for user: {}", userId);

        requireText(userId, "userId");
        requireText(cpf, "cpf");
        if (!CpfValidator.isValid(cpf)) {
            log.warn("Invalid CPF format for user: {}", userId);
            throw new InvalidCpfException();
        }
        if (accountRepositoryPort.existsByUserIdAndBankAndBranchAndAccountNumber(
                userId, bank, branch, accountNumber)) {
            log.warn("Duplicate account for user: {}", userId);
            throw new DuplicateAccountException();
        }

        Account account = Account.create(userId, bank, branch, accountNumber, type);

        boolean firstAccount = !accountRepositoryPort.userHasAnyAccount(userId);
        if (firstAccount || setAsPrimary) {
            accountRepositoryPort.unmarkAllPrimary(userId);
            account.markAsPrimary();
        }

        Account saved = accountRepositoryPort.save(account);
        log.info("Account created with ID: {} (primary: {})", saved.getId(), saved.isPrimary());
        return saved;
    }

    @Override
    public void setPrimaryAccount(String userId, String accountId) {
        log.info("Setting primary account {} for user: {}", accountId, userId);
        Account account = findOwnedAccount(userId, accountId);
        accountRepositoryPort.unmarkAllPrimary(userId);
        account.markAsPrimary();
        accountRepositoryPort.save(account);
        log.info("Primary account updated for user: {}", userId);
    }

    @Override
    public void deleteAccount(String userId, String accountId) {
        log.info("Deleting account {} for user: {}", accountId, userId);
        Account account = findOwnedAccount(userId, accountId);
        accountRepositoryPort.deleteById(account.getId());
        log.info("Account deleted: {}", accountId);
    }

    public List<Account> findByUserId(String userId) {
        log.info("Listing accounts for user: {}", userId);
        return accountRepositoryPort.findByUserId(userId);
    }

    private Account findOwnedAccount(String userId, String accountId) {
        return accountRepositoryPort.findById(accountId)
                .filter(account -> account.belongsTo(userId))
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}