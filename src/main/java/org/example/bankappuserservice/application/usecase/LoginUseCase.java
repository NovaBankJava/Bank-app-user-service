package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.JwtPort;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final JwtPort jwtPort;

    public LoginUseCase(UserRepositoryPort userRepositoryPort, JwtPort jwtPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.jwtPort = jwtPort;
    }

    public Map<String, String> execute(String identifier, String password){
        User user = userRepositoryPort.findByEmailOrCpf(identifier).orElseThrow(()-> new RuntimeException("Credentials invalid"));

        if (!user.getPassword().equals(password)){
            throw new RuntimeException("Credentials invalid");
        }

        String accessToken = jwtPort.generateAccessToken(user.getEmail());
        String refreshToken = jwtPort.generateRefreshToken(user.getEmail());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "tokenType", "Bearer",
                "expiresIn", "900"
        );
    }
}
