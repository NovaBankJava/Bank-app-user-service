package org.example.bankappuserservice.application.ports.out;

import java.time.Instant;

public interface TokenBlackListPort {
    void revoke(String  token, Instant expiresAt);
    boolean isRevoked(String token);
}
