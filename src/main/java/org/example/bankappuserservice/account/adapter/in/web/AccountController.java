package org.example.bankappuserservice.account.adapter.in.web;

import java.net.URI;
import java.util.List;
import org.example.bankappuserservice.account.adapter.in.web.dto.AccountResponse;
import org.example.bankappuserservice.account.adapter.in.web.dto.CreateAccountRequest;
import org.example.bankappuserservice.account.application.port.in.CreateAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.DeleteAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.SetPrimaryAccountUseCase;
import org.example.bankappuserservice.account.application.service.AccountService;
import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccount;
    private final SetPrimaryAccountUseCase setPrimaryAccount;
    private final DeleteAccountUseCase deleteAccount;
    private final AccountService accountService;

    public AccountController(CreateAccountUseCase createAccount,
                             SetPrimaryAccountUseCase setPrimaryAccount,
                             DeleteAccountUseCase deleteAccount,
                             AccountService accountService) {
        this.createAccount = createAccount;
        this.setPrimaryAccount = setPrimaryAccount;
        this.deleteAccount = deleteAccount;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@PathVariable String userId,
                                                  @RequestBody CreateAccountRequest request) {
        Account account = createAccount.create(request.toCommand(userId));
        URI location = URI.create("/api/v1/users/" + userId + "/accounts/" + account.getId());
        return ResponseEntity.created(location).body(AccountResponse.from(account));
    }

    @GetMapping
    public List<AccountResponse> list(@PathVariable String userId) {
        return accountService.findByUserId(userId).stream()
                .map(AccountResponse::from)
                .toList();
    }

    @PatchMapping("/{accountId}/primary")
    public ResponseEntity<Void> setPrimary(@PathVariable String userId,
                                           @PathVariable String accountId) {
        setPrimaryAccount.setPrimary(userId, accountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> delete(@PathVariable String userId,
                                       @PathVariable String accountId) {
        deleteAccount.delete(userId, accountId);
        return ResponseEntity.noContent().build();
    }
}