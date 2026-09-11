package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordHashAdapterTest {

    private final PasswordEncoder passwordEncoder =
            Mockito.mock(PasswordEncoder.class);

    private final PasswordHashAdapter hashAdapter = new PasswordHashAdapter(passwordEncoder);

    @Test
    void shouldHashPassword() {

        String password = "12345678";
        String passwordHash = "password-hash";

        Mockito.when(passwordEncoder.encode(password)).thenReturn(passwordHash);

        String result = hashAdapter.hash(password);

        Mockito.verify(passwordEncoder).encode(password);

        assertEquals("password-hash", result);

    }
}
