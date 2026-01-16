package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.entities.Subscription;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.SubscriptionRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("getThemes should return a list of themes")
    void getThemes_shouldReturnList() {
        // Arrange
        when(themeRepository.findAll()).thenReturn(List.of(new Theme(), new Theme()));

        // Act
        List<Theme> result = themeService.getThemes();

        // Assert
        assertThat(result).hasSize(2);
        verify(themeRepository).findAll();
    }

    @Test
    @DisplayName("subscribe should save subscription when valid")
    void subscribe_shouldSaveSubscription() {
        // Arrange
        Long themeId = 1L;
        String email = "test@test.com";
        User user = User.builder().id(10L).email(email).build();
        Theme theme = Theme.builder().id(themeId).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(theme));
        when(subscriptionRepository.existsById(any())).thenReturn(false);

        // Act
        themeService.subscribe(themeId, email);

        // Assert
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    @DisplayName("subscribe should do nothing if already subscribed")
    void subscribe_shouldDoNothingIfAlreadySubscribed() {
        // Arrange
        Long themeId = 1L;
        String email = "test@test.com";
        User user = User.builder().id(10L).email(email).build();
        Theme theme = Theme.builder().id(themeId).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(theme));
        when(subscriptionRepository.existsById(any())).thenReturn(true);

        // Act
        themeService.subscribe(themeId, email);

        // Assert
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("subscribe should throw NotFoundException if user not found")
    void subscribe_shouldThrowIfUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.subscribe(1L, "unknown@test.com"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Utilisateur introuvable");
    }

    @Test
    @DisplayName("unsubscribe should delete subscription")
    void unsubscribe_shouldDeleteSubscription() {
        // Arrange
        Long themeId = 1L;
        String email = "test@test.com";
        User user = User.builder().id(10L).email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(subscriptionRepository.existsById(any())).thenReturn(true);

        // Act
        themeService.unsubscribe(themeId, email);

        // Assert
        verify(subscriptionRepository).deleteById(any());
    }
}