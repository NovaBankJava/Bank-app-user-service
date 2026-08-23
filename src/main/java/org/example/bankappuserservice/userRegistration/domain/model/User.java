package org.example.bankappuserservice.userRegistration.domain.model;


import java.time.Instant;
import java.util.UUID;

public class User {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String cpf;
    private String passwordHash;
    private UserStatus userStatus;
    private Instant createAt;

    public User(String name, String phone, String email, String cpf, String passwordHash, Instant createAt) {

        this.name = requireText(name, "name");
        this.phone = requireText(phone, "phone");
        this.email = requireText(email, "email");
        this.cpf = requireText(cpf, "cpf");
        this.passwordHash = requireText(passwordHash, "passwordHash");
        this.createAt = createAt;

        this.userStatus = UserStatus.PENDING_VERIFICATION;

    }


    public void activate() {
        this.userStatus = UserStatus.ACTIVE;
    }

    public boolean isPending() {
        return userStatus == UserStatus.PENDING_VERIFICATION;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createAt;
    }

    public UserStatus getStatus() {
        return userStatus;
    }
}

