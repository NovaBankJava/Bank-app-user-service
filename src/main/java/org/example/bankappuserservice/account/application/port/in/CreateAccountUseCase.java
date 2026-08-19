package org.example.bankappuserservice.account.application.port.in;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;

public interface CreateAccountUseCase {

    Account create(CreateAccountCommand command);

    record CreateAccountCommand(
            String userId,
            String bank,
            String branch,
            String accountNumber,
            AccountType type,
            boolean setAsPrimary) {
    }
}
