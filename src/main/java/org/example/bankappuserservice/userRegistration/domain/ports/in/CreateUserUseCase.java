package org.example.bankappuserservice.userRegistration.domain.ports.in;

import org.example.bankappuserservice.userRegistration.domain.model.User;

public interface CreateUserUseCase {

    User execute (CreateUserInput input);

}
