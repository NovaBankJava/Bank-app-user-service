package org.example.bankappuserservice.infra.adapter.in.controller;

import org.example.bankappuserservice.domain.ports.in.UserInboundPort;
import org.example.bankappuserservice.infra.adapter.in.dto.UserPostRequest;
import org.example.bankappuserservice.infra.adapter.in.mapper.UserInboundMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserInboundPort userInboundPort;
    private final UserInboundMapper mapper;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserPostRequest request) {
        var command = mapper.toCommand(request);
        userInboundPort.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}