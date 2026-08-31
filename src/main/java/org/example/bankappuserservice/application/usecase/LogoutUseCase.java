package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.example.bankappuserservice.application.ports.out.TokenBlackListPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LogoutUseCase {

    private final TokenBlackListPort tokenBlackListPort;
    private final JwtPort jwtPort;

    public LogoutUseCase(TokenBlackListPort tokenBlackListPort, JwtPort jwtPort) {
        this.tokenBlackListPort = tokenBlackListPort;
        this.jwtPort = jwtPort;
    }

    public void execute(String token){
        long ttl = jwtPort.getAccessTokenExpiresInSeconds();
        tokenBlackListPort.revoke(token, Instant.now().plusSeconds(ttl));
    }
}
