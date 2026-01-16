package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.auth.AuthResponse;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.exceptions.MddBadCredentialsException;
import com.openclassroom.mddapi.repositories.UserRepository;
import com.openclassroom.mddapi.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Register: Should successfully create a user and return a token")
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("new@test.com", "NewUser", "password123");
        User savedUser = User.builder().id(1L).email("new@test.com").name("NewUser").build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByName(request.getName())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register: Should throw ConflictException when email is already taken")
    void shouldThrowConflictWhenEmailExists() {
        RegisterRequest request = new RegisterRequest("already@taken.com", "User", "pass");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Register: Should throw ConflictException when username is already taken")
    void shouldThrowConflictWhenNameExists() {
        RegisterRequest request = new RegisterRequest("new@test.com", "AlreadyTaken", "pass");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("Login: Should successfully authenticate and return a JWT token")
    void shouldAuthenticateSuccessfully() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        User mockUser = User.builder().email("user@test.com").name("User").build();

        when(userRepository.findByEmailOrName(anyString(), anyString())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("login-token");

        AuthResponse response = authService.authenticate(request);

        assertEquals("login-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Login: Should propagate BadCredentialsException on failure")
    void shouldPropagateAuthenticationError() {
        LoginRequest request = new LoginRequest("test@test.com", "wrong");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new MddBadCredentialsException("Bad credentials"));

        assertThrows(MddBadCredentialsException.class, () -> authService.authenticate(request));
        verify(jwtService, never()).generateToken(any());
    }
}