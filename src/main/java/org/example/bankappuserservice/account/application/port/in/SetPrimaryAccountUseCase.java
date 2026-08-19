package org.example.bankappuserservice.account.application.port.in;

public interface SetPrimaryAccountUseCase {

    void setPrimary(String userId, String accountId);
}