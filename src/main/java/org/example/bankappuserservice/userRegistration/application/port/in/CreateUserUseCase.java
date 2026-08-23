package org.example.bankappuserservice.userRegistration.application.port.in;

import org.example.bankappuserservice.userRegistration.domain.model.User;

public interface CreateUserUseCase {

    User create (CreateUserInput input);

}
