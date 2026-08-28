package org.example.bankappuserservice.infrastructure.adapters.out.persistence;

import org.example.bankappuserservice.application.model.User;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final Map<String, User> dbMock = new HashMap<>();

    public UserRepositoryAdapter(){
        User user = new User("teste@gmail.com", "0987", "123456");
        dbMock.put("teste@gmail.com", user);
        dbMock.put("0987", user);
    }

    @Override
    public Optional<User> findByEmailOrCpf(String identifier) {
        return Optional.ofNullable(dbMock.get(identifier));
    }
}
