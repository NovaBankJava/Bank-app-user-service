package org.example.bankappuserservice.account.application.port.out;

import org.example.bankappuserservice.account.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);

    List<Account> findByUserId(String userId);

    Optional<Account> findById(String accountId);

    boolean userHasAnyAccount(String userId);

    void unmarkAllPrimary(String userId);

    void deleteById(String accountId);
}
