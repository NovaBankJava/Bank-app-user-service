package org.example.bankappuserservice.account.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankappuserservice.account.adapter.in.web.dto.AccountResponse;
import org.example.bankappuserservice.account.adapter.in.web.dto.ApiResponse;
import org.example.bankappuserservice.account.adapter.in.web.dto.CreateAccountRequest;
import org.example.bankappuserservice.account.application.port.in.CreateAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.DeleteAccountUseCase;
import org.example.bankappuserservice.account.application.port.in.SetPrimaryAccountUseCase;
import org.example.bankappuserservice.account.application.service.AccountService;
import org.example.bankappuserservice.account.domain.model.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final CreateAccountUseCase createAccount;
    private final SetPrimaryAccountUseCase setPrimaryAccount;
    private final DeleteAccountUseCase deleteAccount;
    private final AccountService accountService;

    @PostMapping("/createAccount")
    public ApiResponse<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        log.info("POST /createAccount for user: {}", request.userId());
        Account account = createAccount.createAccount(
                request.userId(), request.cpf(), request.bank(), request.branch(),
                request.accountNumber(), request.type(), request.setAsPrimary());
        return ApiResponse.ok(AccountResponse.from(account));
    }

    @GetMapping("/listAccounts/{userId}")
    public ApiResponse<List<AccountResponse>> listAccounts(@PathVariable String userId) {
        log.info("GET /listAccounts for user: {}", userId);
        List<AccountResponse> accounts = accountService.findByUserId(userId).stream()
                .map(AccountResponse::from)
                .toList();
        return ApiResponse.ok(accounts);
    }

    @PatchMapping("/setPrimaryAccount/{userId}/{accountId}")
    public ApiResponse<Void> setPrimaryAccount(@PathVariable String userId,
                                               @PathVariable String accountId) {
        log.info("PATCH /setPrimaryAccount {} for user: {}", accountId, userId);
        setPrimaryAccount.setPrimaryAccount(userId, accountId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/deleteAccount/{userId}/{accountId}")
    public ApiResponse<Void> deleteAccount(@PathVariable String userId,
                                           @PathVariable String accountId) {
        log.info("DELETE /deleteAccount {} for user: {}", accountId, userId);
        deleteAccount.deleteAccount(userId, accountId);
        return ApiResponse.ok();
    }
}