package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.example.bankappuserservice.application.ports.out.TokenBlackListPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class LogoutUseCaseTest {

    @Test
    void deveRevogarOTokenNoLogout() {

        TokenBlackListPort tokenBlackListPort = mock(TokenBlackListPort.class);
        JwtPort jwtPort = mock(JwtPort.class);
        when(jwtPort.getAccessTokenExpiresInSeconds()).thenReturn(900L);

        LogoutUseCase useCase = new LogoutUseCase(tokenBlackListPort, jwtPort);

        useCase.execute("token-123");

        verify(tokenBlackListPort, times(1)).revoke(eq("token-123"), any(Instant.class));
    }
}
