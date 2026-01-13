package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.articles.ArticleRequest;
import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.dtos.articles.CommentRequest;
import com.openclassroom.mddapi.dtos.articles.CommentResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import com.openclassroom.mddapi.mappers.ArticleMapper;
import com.openclassroom.mddapi.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing articles and their associated comments.
 * Provides endpoints for retrieving, creating, and interacting with developer-focused content.
 */
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    /**
     * Retrieves all articles available in the system.
     * Note: Current implementation returns the raw list; sorting and filtering
     * are expected to be handled by the client application.
     *
     * @return a list of {@link ArticleResponse} objects.
     */
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<Article> articles = articleService.findAll();
        return ResponseEntity.ok(articleMapper.toDtoList(articles));
    }

    /**
     * Retrieves a specific article by its unique identifier.
     *
     * @param id the ID of the article to retrieve.
     * @return the {@link ArticleResponse} matching the ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long id) {
        Article article = articleService.findById(id);
        return ResponseEntity.ok(articleMapper.toResponse(article));
    }

    /**
     * Creates a new article.
     * The author is automatically set based on the authenticated user's credentials.
     *
     * @param request   the article creation data.
     * @param principal the authenticated user information.
     * @return the newly created {@link ArticleResponse}.
     */
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody ArticleRequest request,
            Principal principal
    ) {
        Article article = articleService.create(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toResponse(article));
    }

    /**
     * Adds a comment to an existing article.
     *
     * @param id        the ID of the article to comment on.
     * @param request   the comment data.
     * @param principal the authenticated user information.
     * @return the created {@link CommentResponse}.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            Principal principal
    ) {
        Comment comment = articleService.addComment(id, request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toCommentResponse(comment));
    }
}