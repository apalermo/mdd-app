package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.ThemeDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public List<ThemeDto> getThemes() {
        return themeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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

    private ThemeDto mapToDto(Theme theme) {
        ThemeDto dto = new ThemeDto();
        dto.setId(theme.getId());
        dto.setTitle(theme.getTitle());
        dto.setDescription(theme.getDescription());
        dto.setCreatedAt(theme.getCreatedAt());
        dto.setUpdatedAt(theme.getUpdatedAt());
        return dto;
    }
}