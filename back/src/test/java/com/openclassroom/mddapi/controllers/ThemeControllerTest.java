package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.entities.Subscription;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.repositories.SubscriptionRepository;
import com.openclassroom.mddapi.repositories.ThemeRepository;
import com.openclassroom.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Theme Controller Integration Tests")
public class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User testUser;
    private Theme testTheme;

    @BeforeEach
    public void setup() {
        subscriptionRepository.deleteAll();
        themeRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .email("test@test.com")
                .name("TestUser")
                .password("password123")
                .build();
        userRepository.save(testUser);

        testTheme = Theme.builder()
                .title("Java")
                .description("Le meilleur langage")
                .build();
        themeRepository.save(testTheme);
    }

    @AfterEach
    public void tearDown() {
        subscriptionRepository.deleteAll();
        themeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should subscribe a user to a theme successfully")
    @WithMockUser(username = "test@test.com")
    public void shouldSubscribeToThemeSuccessfully() throws Exception {
        mockMvc.perform(post("/api/themes/" + testTheme.getId() + "/subscribe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Subscribed")));

        var id = new Subscription.SubscriptionId(testUser.getId(), testTheme.getId());
        assertTrue(subscriptionRepository.existsById(id), "Subscription should be present in database");
    }

    @Test
    @DisplayName("Should be idempotent when user is already subscribed (return 200 OK)")
    @WithMockUser(username = "test@test.com")
    public void shouldBeIdempotentWhenUserIsAlreadySubscribed() throws Exception {
        var id = new Subscription.SubscriptionId(testUser.getId(), testTheme.getId());
        Subscription sub = Subscription.builder()
                .id(id)
                .user(testUser)
                .theme(testTheme)
                .build();
        subscriptionRepository.save(sub);

        mockMvc.perform(post("/api/themes/" + testTheme.getId() + "/subscribe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Subscribed")));

        assertTrue(subscriptionRepository.existsById(id));
    }

    @Test
    @DisplayName("Should return 404 Not Found when theme ID does not exist")
    @WithMockUser(username = "test@test.com")
    public void shouldReturnNotFoundWhenThemeDoesNotExistOnSubscribe() throws Exception {
        mockMvc.perform(post("/api/themes/999/subscribe"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 Not Found when user does not exist")
    @WithMockUser(username = "unknown@test.com")
    public void shouldReturnNotFoundWhenUserDoesNotExistOnSubscribe() throws Exception {
        mockMvc.perform(post("/api/themes/" + testTheme.getId() + "/subscribe"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should unsubscribe a user from a theme successfully")
    @WithMockUser(username = "test@test.com")
    public void shouldUnsubscribeFromThemeSuccessfully() throws Exception {
        // GIVEN
        var id = new Subscription.SubscriptionId(testUser.getId(), testTheme.getId());
        Subscription sub = Subscription.builder()
                .id(id)
                .user(testUser)
                .theme(testTheme)
                .build();
        subscriptionRepository.save(sub);

        mockMvc.perform(delete("/api/themes/" + testTheme.getId() + "/unsubscribe"))
                .andExpect(status().isNoContent());

        assertFalse(subscriptionRepository.existsById(id), "Subscription should be removed from database");
    }

    @Test
    @DisplayName("Should return 204 No Content when user is not subscribed (Idempotence)")
    @WithMockUser(username = "test@test.com")
    public void shouldReturnNoContentWhenUserIsNotSubscribed() throws Exception {
        mockMvc.perform(delete("/api/themes/" + testTheme.getId() + "/unsubscribe"))
                .andExpect(status().isNoContent());
    }
}