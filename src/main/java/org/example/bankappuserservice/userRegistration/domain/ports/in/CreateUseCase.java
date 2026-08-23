package org.example.bankappuserservice.userRegistration.domain.ports.in;

import org.example.bankappuserservice.userRegistration.domain.model.User;

public interface CreateUseCase {
    User createUser(String id, String nome, String phone, String email, String cpf, String passwordHash);

}
