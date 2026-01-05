package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Subscription.SubscriptionId> {
}