package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.AuditLogPort;
import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUseCaseTest {

    @Test
    void deveAutenticarERetornarTokensComSucesso(){

        UserRepositoryPort userRepositoryPort = mock(UserRepositoryPort.class);
        JwtPort jwtPort = mock(JwtPort.class);
        AuditLogPort auditLogPort = mock(AuditLogPort.class);

        User user = new User("teste@gmail.com", "0987", "123456",0, false);

        when(userRepositoryPort.findByEmailOrCpf("0987")).thenReturn(Optional.of(user));
        when(jwtPort.generateAccessToken("teste@gmail.com")).thenReturn("fake-access-token");
        when(jwtPort.generateRefreshToken("teste@gmail.com")).thenReturn("fake-refresh-token");

        LoginUseCase loginUseCase = new LoginUseCase(userRepositoryPort, jwtPort, auditLogPort);

        var response = loginUseCase.execute("0987", "123456", "123.123", "pc");

        assertNotNull(response.get("accessToken"));
        assertNotNull(response.get("refreshToken"));
        assertEquals("Bearer", response.get("tokenType"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForIncorreta() {

        UserRepositoryPort userRepositoryPort = mock(UserRepositoryPort.class);
        JwtPort jwtPort = mock(JwtPort.class);
        AuditLogPort auditLogPort = mock(AuditLogPort.class);
        User user = new User("teste@novabank.com", "12345678900", "Senha123@", 0,false);

        when(userRepositoryPort.findByEmailOrCpf("teste@novabank.com")).thenReturn(Optional.of(user));

        LoginUseCase useCase = new LoginUseCase(userRepositoryPort, jwtPort, auditLogPort);

        assertThrows(RuntimeException.class, () -> useCase.execute("teste@novabank.com", "Errada123", "123.123", "pc"));
    }

    @Test
    void deveBloquearContaApos5TentativasIncorretas() {

        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        JwtPort jwt = mock(JwtPort.class);
        AuditLogPort auditLogPort = mock(AuditLogPort.class);

        User user = new User("teste@novabank.com", "12345678900", "Senha123@", 4, false);
        when(repo.findByEmailOrCpf("teste@novabank.com")).thenReturn(Optional.of(user));

        LoginUseCase useCase = new LoginUseCase(repo, jwt, auditLogPort);

        assertThrows(RuntimeException.class, () -> useCase.execute("teste@novabank.com", "SenhaErrada", "123.123", "pc"));

        assertTrue(user.isLocked());
        verify(repo, times(1)).save(user);
    }

    @Test
    void deveAutenticarERegistrarLogDeSucesso() {

        UserRepositoryPort userRepositoryPort = mock(UserRepositoryPort.class);
        JwtPort jwtPort = mock(JwtPort.class);
        AuditLogPort auditLogPort = mock(AuditLogPort.class);

        User user = new User("teste@novabank.com", "12345678900", "Senha123@", 0, false);
        when(userRepositoryPort.findByEmailOrCpf("12345678900")).thenReturn(Optional.of(user));
        when(jwtPort.generateAccessToken("teste@novabank.com")).thenReturn("fake-access-token");
        when(jwtPort.generateRefreshToken("teste@novabank.com")).thenReturn("fake-refresh-token");

        LoginUseCase useCase = new LoginUseCase(userRepositoryPort, jwtPort, auditLogPort);

        var response = useCase.execute("12345678900", "Senha123@", "127.0.0.1", "PC-Test");

        assertNotNull(response.get("accessToken"));
        verify(auditLogPort, times(1)).logAccess("teste@novabank.com", "SUCESSO", "127.0.0.1", "PC-Test");
    }
}
