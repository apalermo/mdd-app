package com.openclassroom.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Integration Tests - Theme Feature")
class ThemeIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        themeRepository.deleteAll();
        userRepository.deleteAll();

        Theme theme = new Theme();
        theme.setTitle("Java");
        theme.setDescription("Best lang");
        themeRepository.save(theme);

        createAndLoginUser();
    }

    @Test
    @DisplayName("Should return themes when authenticated")
    void shouldReturnThemes() throws Exception {
        mockMvc.perform(get("/api/themes")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"));
    }

    @Test
    @DisplayName("Should return 401 when not authenticated")
    void shouldRefuseAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isUnauthorized());
    }

    private void createAndLoginUser() throws Exception {
        RegisterRequest register = new RegisterRequest("theme@test.com", "ThemeUser", "Pass123!");
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest("theme@test.com", "Pass123!");
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(login))).andReturn();

        this.jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}