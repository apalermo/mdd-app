package com.openclassroom.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.articles.ArticleRequest;
import com.openclassroom.mddapi.dtos.auth.LoginRequest;
import com.openclassroom.mddapi.dtos.auth.RegisterRequest;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.repositories.ArticleRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@DisplayName("Integration Tests - Article Feature")
class ArticleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private String jwtToken;
    private Long themeId;

    @BeforeEach
    void setUp() throws Exception {
        articleRepository.deleteAll();
        userRepository.deleteAll();
        themeRepository.deleteAll();

        Theme theme = new Theme();
        theme.setTitle("Java");
        theme.setDescription("Java rocks");
        themeId = themeRepository.save(theme).getId();

        createAndLoginUser();
    }

    @Test
    @DisplayName("Should create an article and retrieve it")
    void shouldCreateAndRetrieveArticle() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("Integration Cypress");
        request.setContent("Backend safeguard.");
        request.setThemeId(themeId);

        mockMvc.perform(post("/api/articles")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/articles")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Cypress"));
    }

    private void createAndLoginUser() throws Exception {
        RegisterRequest register = new RegisterRequest("test@test.com", "Tester", "Pass123!");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest("test@test.com", "Pass123!");
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))).andReturn();

        this.jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}