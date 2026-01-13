package com.openclassroom.mddapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Link entity representing a user's subscription to a specific technical theme.
 * This connection drives the personalized content delivery for the developer's feed.
 */
@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

    /**
     * The developer who is subscribing.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The technical theme being followed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("themeId")
    @JoinColumn(name = "theme_id")
    private Theme theme;

    /**
     * Timestamp of when the subscription was created.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Composite key for user-theme subscription uniqueness.
     */
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubscriptionId implements Serializable {
        @Column(name = "user_id")
        private Long userId;
        @Column(name = "theme_id")
        private Long themeId;
    }
}