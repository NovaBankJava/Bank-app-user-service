package org.example.bankappuserservice.application.usecase;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public LoginUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void execute(String identifier, String password){
        User user = userRepositoryPort.findByEmailOrCpf(identifier).orElseThrow(()-> new RuntimeException("Credentials invalid"));

        if (!user.getPassword().equals(password)){
            throw new RuntimeException("Credentials invalid");
        }
    }
}
