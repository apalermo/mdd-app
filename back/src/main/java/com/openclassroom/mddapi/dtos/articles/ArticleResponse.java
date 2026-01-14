package com.openclassroom.mddapi.dtos.articles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing an article in the MDD feed.
 * Includes author information and nested comments for the detailed view.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArticleResponse {
    /**
     * Unique identifier of the article.
     */
    private Long id;

    /**
     * Technical title of the article.
     */
    private String title;

    /**
     * Article content, formatted for Markdown rendering on the frontend.
     */
    private String content;

    /**
     * Display name of the developer who shared this article.
     */
    private String authorName;

    /**
     * Simplified theme information for article categorization.
     */
    private ThemeResponse theme;

    /**
     * List of peer interactions and feedback.
     */
    @Builder.Default
    private List<CommentResponse> comments = List.of();

    /**
     * Publication timestamp.
     * Essential for the chronological sorting of the developer newsfeed.
     */
    private LocalDateTime createdAt;
}