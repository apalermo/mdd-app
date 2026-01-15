package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.articles.ArticleRequest;
import com.openclassroom.mddapi.dtos.articles.CommentRequest;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.ArticleRepository;
import com.openclassroom.mddapi.repositories.CommentRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final CommentRepository commentRepository;

    public List<Article> findAll() {
        log.info("Fetching all articles from database");
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        log.info("Fetching article with id: {}", id);
        return articleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Article search failed: ID {} not found", id);
                    return new NotFoundException("Article introuvable avec l'identifiant : " + id);
                });
    }

    public Article create(ArticleRequest request, String email) {
        log.info("Attempting to create article '{}' for user: {}", request.getTitle(), email);

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new NotFoundException("Thème introuvable"));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .theme(theme)
                .build();

        Article savedArticle = articleRepository.save(article);
        log.info("Article successfully created with ID: {}", savedArticle.getId());
        return savedArticle;
    }

    public Comment addComment(Long articleId, CommentRequest request, String email) {
        log.info("Attempting to add comment on article {} by user: {}", articleId, email);

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article introuvable"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .article(article)
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment ID {} successfully added to article {}", savedComment.getId(), articleId);
        return savedComment;
    }
}