package org.example.bankappuserservice.account.application.port.in;

public interface DeleteAccountUseCase {

    void delete(String userId, String accountId);
}