package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginUseCaseTest {

    @Test
    void deveAutenticarComSucessoUsandoEmailOuCpf(){
        UserRepositoryPort userRepositoryPort = mock(UserRepositoryPort.class);
        User user = new User("teste@gmail.com", "0987", "123456");

        when(userRepositoryPort.findByEmailOrCpf("0987")).thenReturn(Optional.of(user));

        LoginUseCase loginUseCase = new LoginUseCase(userRepositoryPort);

        assertDoesNotThrow(()-> loginUseCase.execute("0987", "123456"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForIncorreta() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        User user = new User("teste@novabank.com", "12345678900", "Senha123@");

        when(repo.findByEmailOrCpf("teste@novabank.com")).thenReturn(Optional.of(user));

        LoginUseCase useCase = new LoginUseCase(repo);

        assertThrows(RuntimeException.class, () -> useCase.execute("teste@novabank.com", "Errada123"));
    }
}
