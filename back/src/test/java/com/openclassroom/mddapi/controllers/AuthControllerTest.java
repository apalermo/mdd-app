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
    @DisplayName("POST /register: Should return 201 Created and JWT token on success")
    void shouldRegisterSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("integration@test.com", "IntegrationUser", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("POST /register: Should return 400 Bad Request for invalid input")
    void shouldReturn400ForInvalidRegisterInput() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest("not-an-email", "", "short");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("POST /register: Should return 409 Conflict when email or name is already taken")
    void shouldReturnConflictForDuplicateUser() throws Exception {
        RegisterRequest request = new RegisterRequest("dup@test.com", "DupUser", "password123");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /login: Should return 200 OK and JWT token on success")
    void shouldLoginSuccessfully() throws Exception {
        RegisterRequest reg = new RegisterRequest("login@test.com", "LoginUser", "password123");
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reg)));

        LoginRequest login = new LoginRequest("login@test.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("POST /login: Should return 400 Bad Request for empty credentials")
    void shouldReturn400ForEmptyLoginCredentials() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /login: Should return 401 Unauthorized for invalid credentials")
    void shouldReturnUnauthorizedForBadCredentials() throws Exception {
        LoginRequest login = new LoginRequest("unknown@test.com", "wrong-pass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}