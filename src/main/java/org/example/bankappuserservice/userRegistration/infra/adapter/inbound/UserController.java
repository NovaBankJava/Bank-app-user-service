package org.example.bankappuserservice.userRegistration.infra.adapter.inbound;

import jakarta.validation.Valid;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserUseCase;
import org.example.bankappuserservice.userRegistration.infra.adapter.dtos.Request.UserRequestDTO;
import org.example.bankappuserservice.userRegistration.infra.adapter.dtos.mapper.UserRequestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserRequestMapper userRequestMapper;

    public UserController(CreateUserUseCase createUserUseCase, UserRequestMapper userRequestMapper) {
        this.createUserUseCase = createUserUseCase;
        this.userRequestMapper = userRequestMapper;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequestDTO dto){

        CreateUserInput input = userRequestMapper.toInput(dto);
        createUserUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
