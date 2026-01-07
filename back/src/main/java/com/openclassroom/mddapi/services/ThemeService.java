package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.entities.Subscription;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.SubscriptionRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public void subscribe(Long themeId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("Theme not found with ID: " + themeId));


        var subscriptionId = new Subscription.SubscriptionId(user.getId(), theme.getId());

        if (subscriptionRepository.existsById(subscriptionId)) {
            return;
        }

        Subscription subscription = Subscription.builder()
                .id(subscriptionId)
                .user(user)
                .theme(theme)
                .build();

        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(Long themeId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var subscriptionId = new Subscription.SubscriptionId(user.getId(), themeId);

        if (subscriptionRepository.existsById(subscriptionId)) {
            subscriptionRepository.deleteById(subscriptionId);
        }
    }
}