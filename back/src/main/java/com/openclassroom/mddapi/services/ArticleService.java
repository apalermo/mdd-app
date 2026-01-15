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

/**
 * Service handling business logic for the MDD (Monde de Dév) social network.
 * Facilitates peer collaboration by managing articles and developer interactions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final CommentRepository commentRepository;

    /**
     * Retrieves all available articles for the MDD platform.
     * Note: Sorting and filtering are currently handled client-side to
     * meet MVP requirements for dynamic feed display.
     *
     * @return a list of all technical articles.
     */
    public List<Article> findAll() {
        log.info("Fetching all articles from database");
        return articleRepository.findAll();
    }

    /**
     * Finds a specific MDD article by its identifier.
     *
     * @param id the unique identifier of the article.
     * @return the found article entity.
     * @throws NotFoundException if the article does not exist.
     */
    public Article findById(Long id) {
        log.info("Fetching article with id: {}", id);
        return articleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Article search failed: ID {} not found", id);
                    return new NotFoundException("Article introuvable avec l'identifiant : " + id);
                });
    }

    /**
     * Creates and persists a new technical article.
     * Contributes to the peer-to-peer knowledge sharing and recruitment pool.
     *
     * @param request the article details.
     * @param email   the email of the authenticated author.
     * @return the saved article entity.
     */
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

    /**
     * Adds a comment to an article to foster developer collaboration.
     *
     * @param articleId the target article ID.
     * @param request   the comment content.
     * @param email     the email of the commenting user.
     * @return the saved comment entity.
     */
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