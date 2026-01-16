package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.articles.ArticleRequest;
import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.dtos.articles.CommentRequest;
import com.openclassroom.mddapi.dtos.articles.CommentResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import com.openclassroom.mddapi.mappers.ArticleMapper;
import com.openclassroom.mddapi.services.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing technical articles and their associated community comments.
 * Provides endpoints for retrieving, creating, and interacting with developer-focused content.
 */
@Slf4j
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Endpoints for technical articles and community collaboration")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    /**
     * Retrieves all articles available in the system.
     *
     * @return a list of ArticleResponse objects.
     */
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        log.info("REST request to get all articles");
        List<Article> articles = articleService.findAll();
        log.debug("Found {} articles", articles.size());
        return ResponseEntity.ok(articleMapper.toDtoList(articles));
    }

    /**
     * Retrieves a specific article by its unique identifier.
     *
     * @param id the ID of the article to retrieve.
     * @return the ArticleResponse matching the ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long id) {
        log.info("REST request to get article : {}", id);
        Article article = articleService.findById(id);
        return ResponseEntity.ok(articleMapper.toResponse(article));
    }

    /**
     * Creates a new article.
     *
     * @param request   the article creation data.
     * @param principal the authenticated user information.
     * @return the newly created ArticleResponse.
     */
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody ArticleRequest request,
            Principal principal
    ) {
        log.info("REST request to create article '{}' by user '{}'",
                request.getTitle(), principal.getName());

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
     * @return the created CommentResponse.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            Principal principal
    ) {
        log.info("REST request to add comment on article {} by user '{}'",
                id, principal.getName());

        Comment comment = articleService.addComment(id, request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toCommentResponse(comment));
    }
}