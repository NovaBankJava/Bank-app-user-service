package org.example.bankappuserservice.account.adapter.in.web;

import lombok.extern.slf4j.Slf4j;
import org.example.bankappuserservice.account.adapter.in.web.dto.ApiResponse;
import org.example.bankappuserservice.account.domain.exception.AccountNotFoundException;
import org.example.bankappuserservice.account.domain.exception.DuplicateAccountException;
import org.example.bankappuserservice.account.domain.exception.InvalidCpfException;
import org.example.bankappuserservice.account.domain.exception.OwnershipMismatchException;
import org.example.bankappuserservice.account.domain.exception.SalaryAccountNotAllowedForMinorException;
import org.example.bankappuserservice.account.domain.exception.UserNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AccountController.class)
@Slf4j
public class AccountExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ApiResponse<Void> handleAccountNotFound(AccountNotFoundException ex) {
        log.warn("Account not accessible: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.THIRD_PARTY_ACCESS,
                "account cannot be accessed by third parties");
    }

    @ExceptionHandler(OwnershipMismatchException.class)
    public ApiResponse<Void> handleOwnershipMismatch(OwnershipMismatchException ex) {
        log.warn("Ownership mismatch: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.THIRD_PARTY_ACCESS,
                "informed CPF does not belong to the user");
    }

    @ExceptionHandler(SalaryAccountNotAllowedForMinorException.class)
    public ApiResponse<Void> handleMinorSalary(SalaryAccountNotAllowedForMinorException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.BUSINESS_RULE,
                "minor user cannot have a salary account");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponse<Void> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.USER_NOT_FOUND, "user not found");
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ApiResponse<Void> handleInvalidCpf(InvalidCpfException ex) {
        log.warn("Invalid CPF: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.INVALID_CPF, "invalid CPF");
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ApiResponse<Void> handleDuplicate(DuplicateAccountException ex) {
        log.warn("Duplicate account: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.DUPLICATE_ACCOUNT,
                "account already registered for this user");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("invalid request");
        log.warn("Validation failed: {}", message);
        return ApiResponse.error(ApiResponse.INVALID_INPUT, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleInvalidInput(IllegalArgumentException ex) {
        log.warn("Invalid input: {}", ex.getMessage());
        return ApiResponse.error(ApiResponse.INVALID_INPUT, ex.getMessage());
    }
}