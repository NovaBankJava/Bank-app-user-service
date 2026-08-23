package org.example.bankappuserservice.account.domain.ports.in;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;

public interface CreateAccountUseCase {

    Account createAccount(String userId, String cpf, String bank, String branch,
                          String accountNumber, AccountType type, boolean setAsPrimary);
}