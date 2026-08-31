package org.example.bankappuserservice.infrastructure.adapters.out;

import org.example.bankappuserservice.application.ports.out.TokenBlackListPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlackListAdapter implements TokenBlackListPort {

    private final Map<String, Instant> revoked = new ConcurrentHashMap<>();

    @Override
    public void revoke(String token, Instant expiresAt) {
        revoked.put(token, expiresAt);
    }

    @Override
    public boolean isRevoked(String token) {
        Instant exp = revoked.get(token);
        if(exp == null) return false;
        if(exp.isBefore(Instant.now())) {revoked.remove(token); return false;};
        return true;
    }
}
