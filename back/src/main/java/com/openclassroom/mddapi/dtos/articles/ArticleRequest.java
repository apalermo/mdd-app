package com.openclassroom.mddapi.dtos.articles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new technical article.
 * Enforces validation rules to maintain content quality within the MDD community.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArticleRequest {

    /**
     * Technical title of the article.
     * Must be between 3 and 255 characters.
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255)
    private String title;

    /**
     * Main body of the article.
     * Supports Markdown syntax for code snippets and technical documentation.
     */
    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String content;

    /**
     * Identifier of the related theme (e.g., Java, TypeScript).
     * Necessary for categorization in the user's subscription feed.
     */
    @NotNull(message = "Le thème est obligatoire")
    private Long themeId;
}