package com.openclassroom.mddapi.dtos.articles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a comment in API responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
    /**
     * Unique identifier of the comment.
     */
    private Long id;

    /**
     * Content of the peer feedback.
     */
    private String content;

    /**
     * Username of the developer who posted the comment.
     */
    private String authorName;

    /**
     * Timestamp of the interaction.
     */
    private LocalDateTime createdAt;
}