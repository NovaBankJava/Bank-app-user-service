package org.example.bankappuserservice.account.domain.ports.in;

public interface DeleteAccountUseCase {

    void deleteAccount(String userId, String accountId);
}