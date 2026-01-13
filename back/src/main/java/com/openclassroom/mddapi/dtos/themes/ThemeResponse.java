package com.openclassroom.mddapi.dtos.themes;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a technical theme in the MDD platform.
 * Provides full details, including descriptions, for the theme listing view.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThemeResponse {

    /**
     * Unique identifier for the technical theme.
     */
    private Long id;

    /**
     * Public title of the theme (e.g., "Java", "Angular").
     */
    private String title;

    /**
     * Detailed description of the theme's content and scope.
     */
    private String description;

    /**
     * Timestamp of when the theme was added to the platform.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the theme's information.
     */
    private LocalDateTime updatedAt;
}