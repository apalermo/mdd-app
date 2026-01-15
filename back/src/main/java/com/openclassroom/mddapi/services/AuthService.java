package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.auth.AuthResponse;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.BadCredentialsException;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.repositories.UserRepository;
import com.openclassroom.mddapi.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration conflict: Email {} is already taken", request.getEmail());
            throw new ConflictException("Cet email est déjà utilisé !");
        }

        if (userRepository.existsByName(request.getName())) {
            log.warn("Registration conflict: Username '{}' is already taken", request.getName());
            throw new ConflictException("Ce nom d'utilisateur est déjà utilisé !");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        log.info("User '{}' successfully registered", request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        log.info("Authentication attempt for user: {}", request.getIdentifier());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentifier(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmailOrName(request.getIdentifier(), request.getIdentifier())
                    .orElseThrow(() -> {
                        log.warn("Login failed: User identity '{}' not found after authentication manager check", request.getIdentifier());
                        return new BadCredentialsException("Identifiants incorrects");
                    });

            var jwtToken = jwtService.generateToken(user);
            log.info("User '{}' successfully authenticated", request.getIdentifier());
            return AuthResponse.builder().token(jwtToken).build();

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            log.warn("Authentication failed: Invalid credentials for user {}", request.getIdentifier());
            throw new com.openclassroom.mddapi.exceptions.BadCredentialsException("Identifiants incorrects");
        }
    }
}