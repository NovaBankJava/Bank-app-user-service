package org.example.bankappuserservice.application.model;

public class User {

    private String email;
    private String cpf;
    private String password;
    private int failedAttempts;
    private boolean locked;

    public User(String email, String cpf, String password, int failedAttempts, boolean locked) {
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.failedAttempts = failedAttempts;
        this.locked = locked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
