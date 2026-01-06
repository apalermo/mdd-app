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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Article Service Unit Tests")
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("Find article by ID - Success")
    void findByIdShouldReturnArticleWhenIdExists() {
        Long id = 1L;
        Article article = Article.builder().id(id).title("Titre").build();
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));

        Article result = articleService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Find article by ID - Not Found")
    void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        Long id = 99L;
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> articleService.findById(id));
    }

    @Test
    @DisplayName("Create article - Success")
    void createShouldReturnSavedArticleWhenDataIsCorrect() {
        String email = "author@mdd.com";
        ArticleRequest request = ArticleRequest.builder()
                .title("Nouvel Article")
                .content("Contenu de l'article très détaillé...")
                .themeId(1L)
                .build();

        User mockAuthor = User.builder().id(1L).email(email).build();
        Theme mockTheme = new Theme();
        Article savedArticle = Article.builder()
                .id(10L)
                .title(request.getTitle())
                .author(mockAuthor)
                .theme(mockTheme)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockAuthor));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(mockTheme));
        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        Article result = articleService.create(request, email);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Nouvel Article");
        assertThat(result.getAuthor().getEmail()).isEqualTo(email);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    @DisplayName("Create article - User Not Found")
    void createShouldThrowNotFoundExceptionWhenUserNotFound() {
        String email = "unknown@mdd.com";
        ArticleRequest request = ArticleRequest.builder().themeId(1L).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> articleService.create(request, email));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    @DisplayName("Create article - Theme Not Found")
    void createShouldThrowNotFoundExceptionWhenThemeNotFound() {
        String email = "author@mdd.com";
        ArticleRequest request = ArticleRequest.builder().themeId(99L).build();
        User mockUser = User.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(themeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> articleService.create(request, email));
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    @DisplayName("Add comment to article - Success")
    void addCommentShouldReturnSavedCommentWhenDataIsCorrect() {
        // Arrange
        Long articleId = 1L;
        String email = "commenter@mdd.com";
        CommentRequest request = CommentRequest.builder()
                .content("Ceci est un commentaire de test")
                .build();

        User mockUser = User.builder().id(1L).email(email).build();
        Article mockArticle = Article.builder().id(articleId).title("Titre").build();
        Comment savedComment = Comment.builder()
                .id(50L)
                .content(request.getContent())
                .author(mockUser)
                .article(mockArticle)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mockArticle));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Act
        Comment result = articleService.addComment(articleId, request, email);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Ceci est un commentaire de test");
        assertThat(result.getArticle().getId()).isEqualTo(articleId);
        assertThat(result.getAuthor().getEmail()).isEqualTo(email);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Add comment to article - Article Not Found")
    void addCommentShouldThrowNotFoundExceptionWhenArticleDoesNotExist() {
        // Arrange
        Long articleId = 99L;
        String email = "user@test.com";
        CommentRequest request = CommentRequest.builder().content("Contenu").build();
        User mockUser = User.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> articleService.addComment(articleId, request, email));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("Add comment to article - User Not Found")
    void addCommentShouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        Long articleId = 1L;
        String email = "unknown@test.com";
        CommentRequest request = CommentRequest.builder().content("Contenu").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> articleService.addComment(articleId, request, email));
        verify(commentRepository, never()).save(any(Comment.class));
    }
}