package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.entities.Subscription;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.SubscriptionRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling technical themes and user subscriptions.
 * Manages the core discovery and following logic for the MDD community.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /**
     * Lists all available technical themes on the platform.
     *
     * @return a list of all technical themes.
     */
    public List<Theme> getThemes() {
        log.info("Fetching all themes from database");
        return themeRepository.findAll();
    }

    /**
     * Subscribes the authenticated user to a specific technical theme.
     * Prevents duplicate subscriptions via an idempotency check.
     *
     * @param themeId the unique identifier of the theme to follow.
     * @param email   the email of the user who wants to subscribe.
     * @throws NotFoundException if the user or the theme is not found.
     */
    public void subscribe(Long themeId, String email) {
        log.info("User '{}' requesting subscription to theme ID: {}", email, themeId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Subscription failed: User with email {} not found", email);
                    return new NotFoundException("Utilisateur introuvable");
                });

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> {
                    log.warn("Subscription failed: Theme ID {} not found", themeId);
                    return new NotFoundException("Thème introuvable avec l'identifiant : " + themeId);
                });

        var subscriptionId = new Subscription.SubscriptionId(user.getId(), theme.getId());

        if (subscriptionRepository.existsById(subscriptionId)) {
            log.info("Subscription already exists for user {} and theme {}", user.getId(), themeId);
            return;
        }

        Subscription subscription = Subscription.builder()
                .id(subscriptionId)
                .user(user)
                .theme(theme)
                .build();

        subscriptionRepository.save(subscription);
        log.info("User {} successfully subscribed to theme {}", user.getId(), themeId);
    }

    /**
     * Removes the authenticated user's subscription from a specific technical theme.
     *
     * @param themeId the identifier of the theme to unfollow.
     * @param email   the email of the user who wants to unsubscribe.
     * @throws NotFoundException if the user is not found.
     */
    public void unsubscribe(Long themeId, String email) {
        log.info("User '{}' requesting unsubscription from theme ID: {}", email, themeId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Unsubscription failed: User with email {} not found", email);
                    return new NotFoundException("Utilisateur introuvable");
                });

        var subscriptionId = new Subscription.SubscriptionId(user.getId(), themeId);

        if (subscriptionRepository.existsById(subscriptionId)) {
            subscriptionRepository.deleteById(subscriptionId);
            log.info("User {} successfully unsubscribed from theme {}", user.getId(), themeId);
        } else {
            log.warn("Unsubscribe failed: No active subscription found for user {} and theme {}", user.getId(), themeId);
        }
    }
}