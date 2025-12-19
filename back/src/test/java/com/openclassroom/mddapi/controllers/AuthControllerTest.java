package com.openclassroom.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should register user successfully and return JWT token")
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test-register@test.com");
        request.setName("RegisterUser");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when email already exists")
    void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("duplicate@test.com");
        request.setName("Doublon");
        request.setPassword("password123");
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should authenticate user successfully and return token")
    void shouldAuthenticateUserSuccessfully() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("login@test.com");
        registerRequest.setName("LoginUser");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("login@test.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when bad credentials")
    void shouldReturnUnauthorizedWithBadCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("unknown@test.com");
        loginRequest.setPassword("wrong-password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}