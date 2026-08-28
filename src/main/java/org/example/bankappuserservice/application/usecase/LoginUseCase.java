package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final JwtPort jwtPort;

    @Value("${security.lockout.max-attempts:5}")
    private int maxAttempts = 5;

    public LoginUseCase(UserRepositoryPort userRepositoryPort, JwtPort jwtPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.jwtPort = jwtPort;
    }

    public Map<String, String> execute(String identifier, String password){
        User user = userRepositoryPort.findByEmailOrCpf(identifier).orElseThrow(()-> new RuntimeException("Credentials invalid"));

        if (user.isLocked()){
            throw new RuntimeException("Conta temporariamente bloqueada por excesso de tentativas");
        }

        if (!user.getPassword().equals(password)){
            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= maxAttempts){
                user.setLocked(true);
            }

            userRepositoryPort.save(user);
            throw new RuntimeException("Credentials invalid");
        }

        if (user.getFailedAttempts() > 0){
            user.setFailedAttempts(0);
            userRepositoryPort.save(user);
        }

        String accessToken = jwtPort.generateAccessToken(user.getEmail());
        String refreshToken = jwtPort.generateRefreshToken(user.getEmail());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "tokenType", "Bearer",
                "expiresIn", String.valueOf(jwtPort.getAccessTokenExpiresInSeconds())
        );
    }
}
