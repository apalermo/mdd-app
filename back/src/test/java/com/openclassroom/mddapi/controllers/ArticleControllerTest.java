package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.mappers.ArticleMapper;
import com.openclassroom.mddapi.security.jwt.JwtService;
import com.openclassroom.mddapi.services.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Article Integration Tests")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private ArticleMapper articleMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /api/articles/{id} - Success")
    void getByIdShouldReturnArticle() throws Exception {
        // Arrange
        Long id = 1L;
        Article mockArticle = Article.builder().id(id).title("Test").build();
        ArticleResponse mockResponse = ArticleResponse.builder().id(id).title("Test").build();

        when(articleService.findById(id)).thenReturn(mockArticle);
        when(articleMapper.toResponse(mockArticle)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/articles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Test"));
    }
}