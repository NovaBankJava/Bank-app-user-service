package org.example.bankappuserservice.userRegistration.infra.adapter.inbound;

import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserUseCase;
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

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserInput input){
        createUserUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
