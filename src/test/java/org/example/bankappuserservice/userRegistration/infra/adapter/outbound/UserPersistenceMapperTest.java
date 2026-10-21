package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.example.bankappuserservice.userRegistration.domain.model.UserStatus;

import org.example.bankappuserservice.userRegistration.infra.Integration.JpaUserEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    @Test
    void shouldMapUserToJpaUserEntity() {

        Instant createdAt = Instant.parse("2026-08-31T20:00:00Z");


        User user = new User(
                "test-id",
                "user",
                "75988887777",
                "user@gmail.com",
                "12345678910",
                "password-hasher",
                createdAt
        );

       JpaUserEntity entity = mapper.toEntity(user);

        assertEquals("test-id", entity.getId());
        assertEquals("user", entity.getName());
        assertEquals("75988887777", entity.getPhone());
        assertEquals("user@gmail.com", entity.getEmail());
        assertEquals("12345678910", entity.getCpf());
        assertEquals("password-hasher", entity.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION,entity.getStatus());
        assertEquals(createdAt, entity.getCreatedAt());


    }
    @Test
    void shouldMapJpaUserEntityToUser() {

        Instant createdAt = Instant.parse("2026-08-31T20:00:00Z");


        JpaUserEntity entity = new JpaUserEntity(
                "test-id",
                "user",
                "75988887777",
                "12345678910",
                "user@gmail.com",
                "password-hasher",
                UserStatus.ACTIVE,
                createdAt
        );

        User user = mapper.toDomain(entity);

        assertEquals("test-id", user.getId());
        assertEquals("user", user.getName());
        assertEquals("75988887777", user.getPhone());
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("12345678910", user.getCpf());
        assertEquals("password-hasher", user.getPasswordHash());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(createdAt, user.getCreatedAt());

    }


}
