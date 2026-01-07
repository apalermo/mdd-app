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

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<Article> articles = articleService.findAll();
        return ResponseEntity.ok(articleMapper.toDtoList(articles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long id) {
        Article article = articleService.findById(id);
        return ResponseEntity.ok(articleMapper.toResponse(article));
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody ArticleRequest request,
            Principal principal
    ) {
        Article article = articleService.create(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toResponse(article));
    }

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