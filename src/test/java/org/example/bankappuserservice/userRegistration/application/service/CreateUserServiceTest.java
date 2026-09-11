package org.example.bankappuserservice.userRegistration.application.service;

import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.example.bankappuserservice.userRegistration.domain.model.UserStatus;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.out.IdGeneratorPort;
import org.example.bankappuserservice.userRegistration.domain.ports.out.PasswordHasherPort;
import org.example.bankappuserservice.userRegistration.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateUserServiceTest {

    private final UserRepositoryPort userRepositoryPort =
            Mockito.mock(UserRepositoryPort.class);
    private final PasswordHasherPort passwordHasherPort =
            Mockito.mock(PasswordHasherPort.class);
    private final IdGeneratorPort idGeneratorPort =
            Mockito.mock(IdGeneratorPort.class);

    private final CreateUserService service = new CreateUserService(
            passwordHasherPort,
            userRepositoryPort,
            idGeneratorPort);


    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {

        CreateUserInput input = new CreateUserInput(
                "user",
                "12345678910",
                "user@gmail.com" ,
                "75988887777",
                "12345678");


        Mockito.when(userRepositoryPort.existsByCpf(input.cpf())).thenReturn(true);
        assertThrows(
                CpfAlreadyExistsException.class,
                ()-> service.execute(input)
        );


        Mockito.verify(userRepositoryPort)
                .existsByCpf(input.cpf());

        Mockito.verify(
                userRepositoryPort,
                Mockito.never()
        ).existsByEmail(input.email());

        Mockito.verify(
                passwordHasherPort,
                Mockito.never()
        ).hash(input.password());

        Mockito.verify(
                idGeneratorPort,
                Mockito.never()
        ).generateId();

        Mockito.verify(
                userRepositoryPort,
                Mockito.never()
        ).save(Mockito.any());


    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        CreateUserInput input = new CreateUserInput(
                "user",
                "12345678910",
                "user@gmail.com" ,
                "75988887777",
                "12345678");


        Mockito.when(userRepositoryPort.existsByCpf(input.cpf())).thenReturn(false);

        Mockito.when(userRepositoryPort.existsByEmail(input.email())).thenReturn(true);
        assertThrows(
                EmailAlreadyExistsException.class,
                ()-> service.execute(input)
        );

        Mockito.verify(userRepositoryPort)
                .existsByCpf(input.cpf());

        Mockito.verify(userRepositoryPort)
                .existsByEmail(input.email());

        Mockito.verify(
                passwordHasherPort,
                Mockito.never()
        ).hash(input.password());

        Mockito.verify(
                idGeneratorPort,
                Mockito.never()
        ).generateId();

        Mockito.verify(
                userRepositoryPort,
                Mockito.never()
        ).save(Mockito.any());

    }

    // happy-path
    @Test
    void shouldCreateUserSuccessfully() {

        CreateUserInput input = new CreateUserInput(
                "user",
                "12345678910",
                "user@gmail.com" ,
                "75988887777",
                "12345678");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        Mockito.when(
                userRepositoryPort.existsByCpf(input.cpf()))
                .thenReturn(false);

        Mockito.when(
                userRepositoryPort.existsByEmail(input.email()))
                .thenReturn(false);

        Mockito.when(
                passwordHasherPort.hash(input.password()))
                .thenReturn("password-hasher");

        Mockito.when(
                idGeneratorPort.generateId())
                .thenReturn("test-id");

        Mockito.when(
                userRepositoryPort.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.execute(input);

        Mockito.verify(
                userRepositoryPort)
                .save(userArgumentCaptor.capture());

        User userCapture = userArgumentCaptor.getValue();

        assertEquals("test-id", result.getId());
        assertEquals("user", result.getName());
        assertEquals("user@gmail.com", result.getEmail());
        assertEquals("75988887777", result.getPhone());
        assertEquals("password-hasher", result.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION,result.getStatus());

        assertEquals("test-id", userCapture.getId());
        assertEquals("user", userCapture.getName());
        assertEquals("user@gmail.com", userCapture.getEmail());
        assertEquals("75988887777", userCapture.getPhone());
        assertEquals("password-hasher", userCapture.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION,userCapture.getStatus());

        Mockito.verify(userRepositoryPort)
                .existsByCpf(input.cpf());

        Mockito.verify(userRepositoryPort)
                .existsByEmail(input.email());

        Mockito.verify(passwordHasherPort)
                .hash(input.password());

        Mockito.verify(idGeneratorPort)
                .generateId();




    }

}
