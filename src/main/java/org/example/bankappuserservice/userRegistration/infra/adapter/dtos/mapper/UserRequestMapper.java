package org.example.bankappuserservice.userRegistration.infra.adapter.dtos.mapper;

import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.infra.adapter.dtos.Request.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public CreateUserInput toInput (UserRequestDTO dto) {
        return new CreateUserInput(
                dto.name(),
                dto.cpf(),
                dto.email(),
                dto.phone(),
                dto.password()
        );
    }

}
