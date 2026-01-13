package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.auth.AuthResponse;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entry point for MDD authentication flows.
 * Handles new user registration and secure login to obtain JWT tokens.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and session management")
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new developer account.
     * Automatically generates a JWT token upon successful creation.
     *
     * @param request user registration details.
     * @return an {@link AuthResponse} containing the JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Authenticates a developer using an email/name and password.
     *
     * @param request login credentials.
     * @return an {@link AuthResponse} containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}