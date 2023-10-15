package io.programmingnotes.apigenerator.controller;

import io.programmingnotes.apigenerator.data.login.LoginRequest;
import io.programmingnotes.apigenerator.service.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user here (you can use Spring Security or your custom logic).
        // If authentication is successful, generate and return a JWT token.

        String token = jwtTokenProvider.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }
}
