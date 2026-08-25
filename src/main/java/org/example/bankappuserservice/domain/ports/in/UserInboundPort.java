package org.example.bankappuserservice.domain.ports.in;

import org.example.bankappuserservice.domain.model.CreateUserCommand;
import org.example.bankappuserservice.domain.model.User;

public interface UserInboundPort {
    User createUser(CreateUserCommand command);
}
