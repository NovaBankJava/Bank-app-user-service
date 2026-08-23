package org.example.bankappuserservice.account.domain.ports.in;

public interface SetPrimaryAccountUseCase {

    void setPrimaryAccount(String userId, String accountId);
}