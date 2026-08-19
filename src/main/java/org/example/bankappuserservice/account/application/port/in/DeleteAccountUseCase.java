package org.example.bankappuserservice.account.application.port.in;

public interface DeleteAccountUseCase {

    void deleteAccount(String userId, String accountId);
}