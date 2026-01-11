package com.openclassroom.mddapi.mappers;

import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.dtos.articles.CommentResponse;
import com.openclassroom.mddapi.dtos.articles.ThemeResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        if (article == null) return null;

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getAuthor() != null ? article.getAuthor().getName() : null)
                .theme(ThemeResponse.builder()
                        .id(article.getTheme().getId())
                        .title(article.getTheme().getTitle())
                        .build())
                .createdAt(article.getCreatedAt())
                .comments(article.getComments() != null ? article.getComments().stream()
                        .map(this::toCommentResponse)
                        .collect(Collectors.toList()) : List.of())
                .build();
    }

    public List<ArticleResponse> toDtoList(List<Article> articles) {
        if (articles == null) {
            return List.of();
        }
        return articles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}