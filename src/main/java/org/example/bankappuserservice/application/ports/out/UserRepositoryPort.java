package org.example.bankappuserservice.application.ports.out;

import org.example.bankappuserservice.application.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmailOrCpf(String identifier);
    void save(User user);
}
