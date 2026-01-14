package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Subscription} entities.
 * Handles the relationship between developers and their followed technical themes.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Subscription.SubscriptionId> {
}