package org.example.bankappuserservice.userRegistration.domain.ports.out;

import org.example.bankappuserservice.userRegistration.domain.model.User;

import java.util.Optional;

public interface UserInterfaceImport {

    User save(User user);
    Optional<User> findById(String id);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

}
