package com.openclassroom.mddapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a technical article within the MDD (Monde de Dév) ecosystem.
 * This entity is a core component of the peer-to-peer collaboration platform,
 * allowing developers to share knowledge and attract potential recruiters.
 */
@Entity
@Table(name = "articles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Article {

    /**
     * Unique identifier for the article.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Technical title of the article.
     * Constraints: 3 to 255 characters, must not be blank.
     */
    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String title;

    /**
     * Full content of the article, supporting Markdown for technical documentation.
     * Stored as a LOB (Large Object) to handle extensive technical posts.
     */
    @NotBlank
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * The developer who authored and shared this technical content.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * The specific programming subject or category (e.g., Java, JavaScript) this article relates to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    /**
     * Feedback loop consisting of comments from other developers.
     * Comments are sorted by creation date (newest first) to facilitate active collaboration.
     */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    /**
     * Automatic timestamp of when the article was shared.
     * Critical for the chronological feed requirement of the MVP.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last revision made to the article content.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}