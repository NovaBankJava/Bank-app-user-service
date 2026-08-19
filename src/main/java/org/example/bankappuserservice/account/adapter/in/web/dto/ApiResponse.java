package org.example.bankappuserservice.account.adapter.in.web.dto;

public record ApiResponse<T>(int status, String description, T data) {

    public static final int SUCCESS = 0;
    public static final int INVALID_INPUT = 1;
    public static final int THIRD_PARTY_ACCESS = 2;
    public static final int INVALID_CPF = 5;
    public static final int DUPLICATE_ACCOUNT = 6;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(SUCCESS, "OK", data);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(SUCCESS, "OK", null);
    }

    public static <T> ApiResponse<T> error(int status, String description) {
        return new ApiResponse<>(status, description, null);
    }
}