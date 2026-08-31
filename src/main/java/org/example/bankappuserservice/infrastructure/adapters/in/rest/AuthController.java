package org.example.bankappuserservice.infrastructure.adapters.in.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bankappuserservice.application.ports.out.UserRepositoryPort;
import org.example.bankappuserservice.application.usecase.LoginUseCase;
import org.example.bankappuserservice.application.usecase.LogoutUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(LoginUseCase loginUseCase, LogoutUseCase logoutUseCase) {
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> save(@RequestBody Map<String, String> request, HttpServletRequest httpRequest){

        String ipAddress = httpRequest.getRemoteAddr();
        String device = httpRequest.getHeader("User-Agent");

        var response = loginUseCase.execute(request.get("identifier"), request.get("password"), ipAddress, device);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "").trim();
        logoutUseCase.execute(token);
        return ResponseEntity.noContent().build();
    }
}
