package com.openclassroom.mddapi.dtos.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for returning detailed user profile information.
 * Used primarily for the 'My Profile' view and account management.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    /**
     * Internal unique identifier for the developer.
     */
    private Long id;

    /**
     * Professional email address.
     */
    private String email;

    /**
     * Public display name or handle.
     */
    private String name;

    /**
     * List of technical themes the user has subscribed to for their feed.
     */
    @Builder.Default
    private List<ThemeResponse> subscriptions = List.of();

    /**
     * Account creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last profile update timestamp.
     */
    private LocalDateTime updatedAt;
}