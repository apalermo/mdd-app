package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.auth.AuthResponse;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.entities.User;
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
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@test.com");
        request.setName("NewUser");
        request.setPassword("password123");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(1L).email("new@test.com").build());
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token-exemple");

        // WHEN
        AuthResponse response = authService.register(request);

        // THEN
        assertNotNull(response);

        assertEquals("jwt-token-exemple", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldAuthenticateUserSuccessfully() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setIdentifier("existing@test.com");
        request.setPassword("password123");

        User mockUser = User.builder().email("existing@test.com").password("encodedPass").build();

        when(userRepository.findByEmailOrName(request.getIdentifier(), request.getIdentifier())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("jwt-token-login");

        // WHEN
        AuthResponse response = authService.authenticate(request);

        // THEN
        assertNotNull(response);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when password is incorrect")
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setIdentifier("test@test.com");
        request.setPassword("wrongPassword");

        doThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // WHEN & THEN
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authService.authenticate(request);
        });

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should throw NotFoundException when user does not exist during login")
    void shouldThrowExceptionWhenUserNotFound() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setIdentifier("ghost@test.com");
        request.setPassword("password123");


        when(userRepository.findByEmailOrName(anyString(), anyString())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(com.openclassroom.mddapi.exceptions.NotFoundException.class, () -> {
            authService.authenticate(request);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when email is already taken")
    void shouldThrowExceptionWhenEmailExists() {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("already@test.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // WHEN & THEN
        assertThrows(com.openclassroom.mddapi.exceptions.BadRequestException.class, () -> {
            authService.register(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}