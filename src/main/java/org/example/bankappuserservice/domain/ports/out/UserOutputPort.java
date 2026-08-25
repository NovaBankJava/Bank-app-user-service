package org.example.bankappuserservice.domain.ports.out;

import org.example.bankappuserservice.domain.model.User;

public interface UserOutputPort {

    User save(User user);
}
