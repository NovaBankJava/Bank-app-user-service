package org.example.bankappuserservice.infrastructure.adapters.out.jwt;

import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtAdapter implements JwtPort {

    @Value("${security.jwt.access-expiration}")
    private long accessExpirationMs;

    @Override
    public String generateAccessToken(String email) {
        return "access-token-mock-" + UUID.randomUUID();
    }

    @Override
    public String generateRefreshToken(String email) {
        return "refresh-token-mock-" + UUID.randomUUID();
    }

    @Override
    public long getAccessTokenExpiresInSeconds() {
        return accessExpirationMs / 1000;
    }
}
