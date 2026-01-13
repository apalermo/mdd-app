package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.mappers.ThemeMapper;
import com.openclassroom.mddapi.services.ThemeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing technical themes and user subscriptions.
 */
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Tag(name = "Themes", description = "Endpoints for technical subjects and subject-based subscriptions")
public class ThemeController {

    private final ThemeService themeService;
    private final ThemeMapper themeMapper;

    /**
     * Lists all available technical themes on the platform.
     *
     * @return a list of ThemeResponse objects.
     */
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getThemes();
        return ResponseEntity.ok(themeMapper.toDtoList(themes));
    }

    /**
     * Subscribes the authenticated user to a technical theme.
     *
     * @param id        the ID of the theme to follow.
     * @param principal the authenticated user context.
     * @return a confirmation message.
     */
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(@PathVariable Long id, Principal principal) {
        themeService.subscribe(id, principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("message", "Subscribed"));
    }

    /**
     * Removes the authenticated user's subscription from a theme.
     *
     * @param id        the ID of the theme to unfollow.
     * @param principal the authenticated user context.
     * @return a 204 No Content response upon success.
     */
    @DeleteMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id, Principal principal) {
        themeService.unsubscribe(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}