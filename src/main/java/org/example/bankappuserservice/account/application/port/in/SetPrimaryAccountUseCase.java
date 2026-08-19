package org.example.bankappuserservice.account.application.port.in;

public interface SetPrimaryAccountUseCase {

    void setPrimaryAccount(String userId, String accountId);
}