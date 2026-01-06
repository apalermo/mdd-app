package com.openclassroom.mddapi.mappers;

import com.openclassroom.mddapi.dtos.articles.ArticleResponse;
import com.openclassroom.mddapi.dtos.articles.CommentResponse;
import com.openclassroom.mddapi.dtos.articles.ThemeResponse;
import com.openclassroom.mddapi.entities.Article;
import com.openclassroom.mddapi.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        if (article == null) return null;

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getAuthor().getUsername())
                .theme(ThemeResponse.builder()
                        .id(article.getTheme().getId())
                        .title(article.getTheme().getTitle())
                        .build())
                .createdAt(article.getCreatedAt())
                .comments(article.getComments().stream()
                        .map(this::toCommentResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}