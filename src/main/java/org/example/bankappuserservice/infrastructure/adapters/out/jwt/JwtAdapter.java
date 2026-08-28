package org.example.bankappuserservice.infrastructure.adapters.out.jwt;

import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtAdapter implements JwtPort {
    @Override
    public String generateAccessToken(String email) {
        return "access-token-mock-" + UUID.randomUUID();
    }

    @Override
    public String generateRefreshToken(String email) {
        return "refresh-token-mock-" + UUID.randomUUID();
    }
}
