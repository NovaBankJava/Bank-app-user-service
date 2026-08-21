package org.example.bankappuserservice.userRegistration.domain.model;


import java.time.LocalDateTime;

public class User {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String cpf;
    private String passwordHash;
    private AccountStatus accountStatus;
    private LocalDateTime createAt;

    public User(String name, String phone, String email, String cpf, String passwordHash, LocalDateTime createAt) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.passwordHash = passwordHash;
        this.createAt = createAt;

        this.accountStatus = AccountStatus.PENDING_VERIFICATION;

    }
}
