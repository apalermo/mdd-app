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
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling business logic for the MDD (Monde de Dév) social network.
 * Facilitates peer collaboration by managing articles and developer interactions.
 */
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
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found with id: " + id));
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
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new NotFoundException("Theme not found"));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .theme(theme)
                .build();

        return articleRepository.save(article);
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
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .article(article)
                .build();

        return commentRepository.save(comment);
    }
}