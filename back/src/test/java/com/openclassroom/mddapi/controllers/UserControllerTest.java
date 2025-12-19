package com.openclassroom.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository; // Pour manipuler la base en direct si besoin

    @Test
    @DisplayName("Should return authenticated user details when token is valid")
    void shouldReturnAuthenticatedUserDetails() throws Exception {
        // 1. Inscription
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("me-user@test.com");
        registerRequest.setName("MeUser");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // 2. Login pour avoir le token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("me-user@test.com");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String token = JsonPath.parse(loginResult.getResponse().getContentAsString()).read("$.token");

        // 3. Appel de /me avec le token
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("me-user@test.com"))
                .andExpect(jsonPath("$.name").value("MeUser")); // Vérifie bien le nom du champ dans ton UserDto (name ou username)
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when accessing /me without token")
    void shouldReturnUnauthorizedWhenNoToken() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 404 Not Found when token is valid but user deleted")
    void shouldReturnNotFoundWhenUserDeleted() throws Exception {
        // C'est un cas limite très intéressant :
        // Le token est cryptographiquement valide, mais l'user n'existe plus en BDD.
        // Comme notre architecture appelle la BDD dans le endpoint /me, on doit avoir une 404.

        // 1. Inscription
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("deleted-user@test.com");
        registerRequest.setName("DeletedUser");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // 2. Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("deleted-user@test.com");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        String token = JsonPath.parse(loginResult.getResponse().getContentAsString()).read("$.token");

        // 3. SUPPRESSION de l'utilisateur (Simulation d'un user supprimé entre temps)
        // Note: Ici on supprime manuellement en base car on n'a pas encore de endpoint delete
        var user = userRepository.findByEmail("deleted-user@test.com").orElseThrow();
        userRepository.delete(user);
        userRepository.flush(); // Force la suppression immédiate

        // 4. Appel avec le token (qui est toujours valide techniquement !)
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound()); // Grâce à ton GlobalExceptionHandler
    }
}