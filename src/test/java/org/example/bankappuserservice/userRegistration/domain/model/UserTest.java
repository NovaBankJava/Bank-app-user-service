package org.example.bankappuserservice.userRegistration.domain.model;

import org.example.bankappuserservice.userRegistration.domain.exception.InvalidUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    Instant createdAt = Instant.parse("2026-08-31T20:00:00Z");

    private  User user;

    @BeforeEach
    void setUp () {
        user = new User(
                "teste-id",
                "user teste",
                "75998887777",
                "teste@gmail.com",
                "12345678910",
                "passwordHash",
                createdAt);
    }

    @Test
    void shouldStatusBePendingVerification() {

        assertEquals(UserStatus.PENDING_VERIFICATION, user.getStatus());

    }

    @Test
    void shouldUserStatusBeActive() {

        user.activate();

        assertEquals(UserStatus.ACTIVE, user.getStatus());

    }
    @Test
    void shouldIsPendingReturnFalse() {

        user.activate();
        boolean result = user.isPending();
        assertEquals(false, result);

    }

    @Test
    void shouldIsPendingReturnTrue() {

        boolean result = user.isPending();
        assertEquals(true, result);

    }

    @Test
    void shouldThrowsExceptionWhenNameIsBlank () {

        InvalidUserException exception = assertThrows(
                InvalidUserException.class,
                ()-> new User("teste-id",
                        " ",
                        "75998887777",
                        "teste@gmail.com",
                        "12345678910",
                        "passwordHash",
                        createdAt
                ));

        assertEquals("must not be blank or null", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        InvalidUserException exception = assertThrows(
                InvalidUserException.class,
                ()-> new User("teste-id",
                        null,
                        "75998887777",
                        "teste@gmail.com",
                        "12345678910",
                        "passwordHash",
                        createdAt)
        );

        assertEquals("must not be blank or null", exception.getMessage());

    }

    @Test
    void shouldTrimUserName() {
        User trimUser = new User("teste-id",
                " user ",
                "75998887777",
                "teste@gmail.com",
                "12345678910",
                "passwordHash",
                createdAt);

        assertEquals("user", trimUser.getName());



    }

    @Test
    void shouldReturnStatusActive() {
        User activeUser = new User("teste-id",
                "user",
                "75998887777",
                "teste@gmail.com",
                "12345678910",
                "passwordHash",
                UserStatus.ACTIVE,
                createdAt);

        assertEquals(UserStatus.ACTIVE, activeUser.getStatus());

    }

}

