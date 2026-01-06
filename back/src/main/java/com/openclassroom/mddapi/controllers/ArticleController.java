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
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
>>>>>>> 0f5e52a (feat(articles): implement find all articles endpoint with unit test ( happy path ))
=======
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
>>>>>>> 502d948 (feat(articles): implement article creation happy path with unit tests)

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
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 502d948 (feat(articles): implement article creation happy path with unit tests)

    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody ArticleRequest request,
            Principal principal
    ) {
        Article article = articleService.create(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toResponse(article));
    }
<<<<<<< HEAD

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
>>>>>>> 60e89df (feat(articles): implement add comment with unit test (happy path))
=======
>>>>>>> 502d948 (feat(articles): implement article creation happy path with unit tests)
}