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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Cet email est déjà utilisé !");
        }

        if (userRepository.existsByName(request.getName())) {
            throw new ConflictException("Ce nom d'utilisateur est déjà utilisé !");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentifier(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmailOrName(request.getIdentifier(), request.getIdentifier())
                    .orElseThrow(() -> new BadCredentialsException("Identifiants incorrects"));

            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Identifiants incorrects");
        }
    }
}