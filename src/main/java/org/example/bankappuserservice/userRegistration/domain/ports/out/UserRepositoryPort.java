package org.example.bankappuserservice.userRegistration.domain.ports.out;

import org.example.bankappuserservice.userRegistration.domain.model.User;



public interface UserRepositoryPort {

    boolean existsByCpf(String cpf);
    boolean existsByEmail (String email);
    User save (User user);

}
