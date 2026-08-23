package org.example.bankappuserservice.userRegistration.application.port.out;

import org.example.bankappuserservice.userRegistration.domain.model.User;



public interface UserRepositoryPort {

    boolean existsByCpf(String cpf);
    boolean existsByEmail (String email);
    User save (User user);

}
