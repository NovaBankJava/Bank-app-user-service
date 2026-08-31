package org.example.bankappuserservice.infrastructure.adapters.out;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TokenBlackListAdapterTest {

    @Test
    void deveMarcarTokenComoRevogado() {

        TokenBlackListAdapter adapter = new TokenBlackListAdapter();

        adapter.revoke("token-123", Instant.now().plusSeconds(60));

        assertTrue(adapter.isRevoked("token-123"));
    }

    @Test
    void naoDeveConsiderarRevogadoTokenDesconhecido() {

        TokenBlackListAdapter adapter = new TokenBlackListAdapter();

        assertFalse(adapter.isRevoked("token-inexistente"));
    }

    @Test
    void naoDeveConsiderarRevogadoTokenExpirado() {

        TokenBlackListAdapter adapter = new TokenBlackListAdapter();

        adapter.revoke("token-123", Instant.now().minusSeconds(1));

        assertFalse(adapter.isRevoked("token-123"));
    }
}
