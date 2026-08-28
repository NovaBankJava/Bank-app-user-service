package org.example.bankappuserservice.application.ports.out;

public interface JwtPort {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);
}
