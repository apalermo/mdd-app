package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.mappers.ThemeMapper;
import com.openclassroom.mddapi.services.ThemeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing technical themes and user subscriptions.
 */
@Slf4j
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
        log.info("REST request to get all available themes");
        List<Theme> themes = themeService.getThemes();
        log.debug("Found {} themes", themes.size());
        return ResponseEntity.ok(themeMapper.toDtoList(themes));
    }

    /**
     * Subscribes the authenticated user to a technical theme.
     *
     * @param id        the ID of the theme to follow.
     * @param principal the authenticated user context.
     * @return a 204 No Content response upon success.
     */
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable Long id, Principal principal) {
        log.info("REST request to subscribe user '{}' to theme ID: {}", principal.getName(), id);
        themeService.subscribe(id, principal.getName());
        log.info("User '{}' successfully subscribed to theme ID: {}", principal.getName(), id);
        return ResponseEntity.noContent().build();
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
        log.info("REST request to unsubscribe user '{}' from theme ID: {}", principal.getName(), id);
        themeService.unsubscribe(id, principal.getName());
        log.info("User '{}' successfully unsubscribed from theme ID: {}", principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}