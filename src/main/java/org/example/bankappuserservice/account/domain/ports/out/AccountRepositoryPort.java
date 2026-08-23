package org.example.bankappuserservice.account.domain.ports.out;

import java.util.List;
import java.util.Optional;
import org.example.bankappuserservice.account.domain.model.Account;

public interface AccountRepositoryPort {

    Account save(Account account);

    List<Account> findByUserId(String userId);

    Optional<Account> findById(String accountId);

    boolean userHasAnyAccount(String userId);

    boolean existsByUserIdAndBankAndBranchAndAccountNumber(
            String userId, String bank, String branch, String accountNumber);

    void unmarkAllPrimary(String userId);

    void deleteById(String accountId);
}