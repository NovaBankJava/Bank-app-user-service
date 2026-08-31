package org.example.bankappuserservice.infrastructure.adapters.in.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.example.bankappuserservice.application.usecase.LoginUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> save(@RequestBody Map<String, String> request, HttpServletRequest httpRequest){

        String ipAddress = httpRequest.getRemoteAddr();
        String device = httpRequest.getHeader("User-Agent");

        var response = loginUseCase.execute(request.get("identifier"), request.get("password"), ipAddress, device);

        return ResponseEntity.ok(response);
    }
}
