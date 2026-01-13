package com.openclassroom.mddapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a technical theme or category (e.g., Java, Web3) in the MDD platform.
 * Users subscribe to these themes to personalize their chronological newsfeed.
 */
@Entity
@Table(name = "themes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Theme {

    /**
     * Unique identifier for the technical theme.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The technical name of the theme. Must be unique across the platform.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String title;

    /**
     * Detailed description of the theme matter to guide developer subscriptions.
     */
    @Column(length = 2000)
    private String description;

    /**
     * Timestamp of when the theme was first registered.
     * Useful for auditing the platform's theme expansion.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the theme's metadata or description.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}