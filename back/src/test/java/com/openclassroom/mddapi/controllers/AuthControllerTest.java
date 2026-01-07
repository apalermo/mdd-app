package com.openclassroom.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.auth.AuthResponse;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.security.jwt.JwtService;
import com.openclassroom.mddapi.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Auth Controller Unit Tests")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("POST /register - Should return 201 Created and JWT token on success")
    void shouldRegisterSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("integration@test.com", "IntegrationUser", "password123");
        AuthResponse response = AuthResponse.builder().token("mock-jwt-token").build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    @DisplayName("POST /register - Should return 400 Bad Request for invalid input")
    void shouldReturn400ForInvalidRegisterInput() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest("not-an-email", "", "short");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /register - Should return 409 Conflict when user already exists")
    void shouldReturnConflictForDuplicateUser() throws Exception {
        RegisterRequest request = new RegisterRequest("dup@test.com", "DupUser", "password123");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new ConflictException("Cet email est déjà utilisé !"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /login - Should return 200 OK and JWT token on success")
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("login@test.com", "password123");
        AuthResponse response = AuthResponse.builder().token("mock-jwt-token").build();

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}