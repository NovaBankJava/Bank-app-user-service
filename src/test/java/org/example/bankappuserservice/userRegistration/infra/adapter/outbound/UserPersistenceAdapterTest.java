package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;


import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.example.bankappuserservice.userRegistration.domain.model.UserStatus;
import org.example.bankappuserservice.userRegistration.infra.Integration.JpaUserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserPersistenceAdapterTest {


    private final JpaUserRepository jpaUserRepository =
            Mockito.mock(JpaUserRepository.class);
    private final UserPersistenceMapper userPersistenceMapper =
            Mockito.mock(UserPersistenceMapper.class);

    private final UserPersistenceAdapter adapter = new UserPersistenceAdapter(
            jpaUserRepository,
            userPersistenceMapper
    );

    @Test
    void shouldReturnTrueWhenCpfExists() {

        String cpf = "12345678910";

        Mockito.when(jpaUserRepository.existsByCpf(cpf)).thenReturn(true);

        boolean result = adapter.existsByCpf(cpf);

        Mockito.verify(jpaUserRepository).existsByCpf(cpf);

        assertEquals(true, result);


    }

    @Test
    void shouldReturnTrueWhenEmailExists() {

        String email = "user@gmail.com";

        Mockito.when(jpaUserRepository.existsByEmail(email)).thenReturn(true);

        boolean result = adapter.existsByEmail(email);

        Mockito.verify(jpaUserRepository).existsByEmail(email);

        assertEquals(true, result);

    }

    @Test
    void shouldSaveUserSuccessfully() {

        Instant createdAt = Instant.parse("2026-08-31T20:00:00Z");

        User user = new User(
                "test-id",
                "user",
                "759988887777",
                "user@gmail.com",
                "12345678910",
                "password-hasher",
                createdAt

        );

        JpaUserEntity entity = new JpaUserEntity(
                "test-id",
                "user",
                "759988887777",
                "12345678910",
                "user@gmail.com",
                "password-hasher",
                UserStatus.PENDING_VERIFICATION,
                createdAt
        );

        JpaUserEntity savedEntity = new JpaUserEntity(
                "test-id",
                "user",
                "759988887777",
                "12345678910",
                "user@gmail.com",
                "password-hasher",
                UserStatus.PENDING_VERIFICATION,
                createdAt
        );

        User savedUser = new User("test-id",
                "user",
                "759988887777",
                "user@gmail.com",
                "12345678910",
                "password-hasher",
                UserStatus.PENDING_VERIFICATION,
                createdAt);


        Mockito.when(userPersistenceMapper.toEntity(user)).thenReturn(entity);

        Mockito.when(jpaUserRepository.save(entity)).thenReturn(savedEntity);

        Mockito.when(userPersistenceMapper.toDomain(savedEntity)).thenReturn(savedUser);


        User result = adapter.save(user);


        Mockito.verify(userPersistenceMapper).toEntity(user);

        Mockito.verify(jpaUserRepository).save(entity);

        Mockito.verify(userPersistenceMapper).toDomain(savedEntity);

        assertEquals("test-id", result.getId());
        assertEquals("user", result.getName());
        assertEquals("759988887777", result.getPhone());
        assertEquals("user@gmail.com", result.getEmail());
        assertEquals("12345678910", result.getCpf());
        assertEquals("password-hasher", result.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION, result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());

    }



}
