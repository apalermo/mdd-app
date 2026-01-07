package com.openclassroom.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.articles.ArticleRequest;
import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.dtos.articles.CommentRequest;
import com.openclassroom.mddapi.dtos.articles.CommentResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.mappers.ArticleMapper;
import com.openclassroom.mddapi.security.jwt.JwtService;
import com.openclassroom.mddapi.services.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Article Unit Tests")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private ArticleMapper articleMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /api/articles - Success")
    void findAllShouldReturnListOfArticles() throws Exception {
        ArticleResponse article1 = ArticleResponse.builder().id(1L).title("Art 1").authorName("Auteur").build();
        ArticleResponse article2 = ArticleResponse.builder().id(2L).title("Art 2").authorName("Auteur").build();

        when(articleService.findAll()).thenReturn(List.of(new Article(), new Article()));
        when(articleMapper.toDtoList(any())).thenReturn(List.of(article1, article2));

        mockMvc.perform(get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].author_name").value("Auteur"));
    }

    @Test
    @DisplayName("GET /api/articles/{id} - Success")
    void getByIdShouldReturnArticle() throws Exception {
        Long id = 1L;
        ArticleResponse mockResponse = ArticleResponse.builder()
                .id(id)
                .authorName("Auteur")
                .createdAt(LocalDateTime.now())
                .build();

        when(articleMapper.toResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/articles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author_name").value("Auteur"))
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    @DisplayName("POST /api/articles - Success")
    void createShouldReturnCreatedArticle() throws Exception {
        ArticleRequest request = ArticleRequest.builder()
                .title("Nouveau")
                .content("Contenu")
                .themeId(1L)
                .build();

        Article mockArticle = Article.builder().id(10L).title("Nouveau").build();
        ArticleResponse mockResponse = ArticleResponse.builder().id(10L).title("Nouveau").build();

        when(articleService.create(any(ArticleRequest.class), eq("test@example.com"))).thenReturn(mockArticle);
        when(articleMapper.toResponse(mockArticle)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/articles")
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Nouveau"));
    }

    @Test
    @DisplayName("POST /api/articles - Failure (Validation Error)")
    void createShouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        ArticleRequest invalidRequest = ArticleRequest.builder()
                .title("")
                .content("Contenu")
                .themeId(1L)
                .build();

        mockMvc.perform(post("/api/articles")
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/articles/{id}/comments - Success")
    void addCommentShouldReturnComment() throws Exception {
        Long articleId = 1L;
        String content = "Super article !";
        CommentRequest request = CommentRequest.builder().content(content).build();

        CommentResponse mockResponse = CommentResponse.builder()
                .id(1L)
                .content(content)
                .authorName("Auteur")
                .build();

        when(articleService.addComment(eq(articleId), any(CommentRequest.class), eq("test@example.com")))
                .thenReturn(new Comment());
        when(articleMapper.toCommentResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/articles/{id}/comments", articleId)
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.author_name").value("Auteur"));
    }

    @Test
    @DisplayName("POST /api/articles/{id}/comments - Failure (Empty Content)")
    void addCommentShouldReturnBadRequestWhenContentIsEmpty() throws Exception {
        CommentRequest invalidRequest = CommentRequest.builder().content("").build();

        mockMvc.perform(post("/api/articles/{id}/comments", 1L)
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/articles/{id}/comments - Failure (Article Not Found)")
    void addCommentShouldReturnNotFoundWhenArticleDoesNotExist() throws Exception {
        CommentRequest request = CommentRequest.builder().content("Contenu").build();

        when(articleService.addComment(eq(99L), any(CommentRequest.class), anyString()))
                .thenThrow(new NotFoundException("Article not found"));

        mockMvc.perform(post("/api/articles/{id}/comments", 99L)
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}