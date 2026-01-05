package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.UserUpdateRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return User with mapped subscriptions when user exists")
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

        User result = userService.getByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getName()).isEqualTo("TestUser");
        assertThat(result.getSubscriptions()).hasSize(1);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user does not exist")
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        String email = "unknown@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getByEmail(email));
    }


    @Test
    @DisplayName("Should update user successfully when no conflict with other users")
    void shouldUpdateUserSuccessfully() {
        Long userId = 1L;
        User existingUser = User.builder()
                .id(userId)
                .email("old@test.com")
                .name("OldName")
                .password("OldEncodedPass")
                .build();

        UserUpdateRequest updateRequest = new UserUpdateRequest("NewName", "new@test.com", "newpassword123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmailAndIdNot("new@test.com", userId)).thenReturn(false);
        when(userRepository.existsByNameAndIdNot("NewName", userId)).thenReturn(false);

        when(passwordEncoder.encode("newpassword123")).thenReturn("NewEncodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.updateUser(userId, updateRequest);

        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getName()).isEqualTo("NewName");
        assertThat(result.getPassword()).isEqualTo("NewEncodedPass");

        verify(passwordEncoder).encode("newpassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when updating with an email taken by another user")
    void shouldThrowConflictExceptionWhenEmailIsTaken() {
        Long myId = 1L;
        User me = User.builder().id(myId).email("me@test.com").build();

        UserUpdateRequest updateRequest = new UserUpdateRequest("MyName", "taken@test.com", "password123");

        when(userRepository.findById(myId)).thenReturn(Optional.of(me));

        when(userRepository.existsByEmailAndIdNot("taken@test.com", myId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(myId, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cet email est déjà utilisé par un autre utilisateur.");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when updating with a username taken by another user")
    void shouldThrowConflictExceptionWhenUsernameIsTaken() {
        Long myId = 1L;
        User me = User.builder().id(myId).name("MeUser").build();

        UserUpdateRequest updateRequest = new UserUpdateRequest("TakenName", "me@test.com", "password123");

        when(userRepository.findById(myId)).thenReturn(Optional.of(me));

        when(userRepository.existsByEmailAndIdNot("me@test.com", myId)).thenReturn(false);

        when(userRepository.existsByNameAndIdNot("TakenName", myId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(myId, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ce nom d'utilisateur est déjà pris.");
    }
}