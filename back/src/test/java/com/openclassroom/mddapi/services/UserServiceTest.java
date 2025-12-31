package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.UserDto;
import com.openclassroom.mddapi.entities.Subscription;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return UserDto with mapped subscriptions when user exists")
    void shouldReturnUserDtoWhenUserExists() {
        String email = "test@test.com";

        Theme mockTheme = Theme.builder()
                .id(10L)
                .title("Java")
                .description("Langage Orienté Objet")
                .build();

        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .name("TestUser")
                .subscriptions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Subscription sub = Subscription.builder()
                .id(new Subscription.SubscriptionId(mockUser.getId(), mockTheme.getId()))
                .user(mockUser)
                .theme(mockTheme)
                .build();

        mockUser.getSubscriptions().add(sub);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        UserDto result = userService.getByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getName()).isEqualTo("TestUser");

        assertThat(result.getSubscriptions()).isNotNull();
        assertThat(result.getSubscriptions()).hasSize(1);
        
        assertThat(result.getSubscriptions().getFirst().getId()).isEqualTo(10L);
        assertThat(result.getSubscriptions().getFirst().getTitle()).isEqualTo("Java");

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user does not exist")
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        String email = "unknown@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getByEmail(email));

        verify(userRepository, times(1)).findByEmail(email);
    }
}