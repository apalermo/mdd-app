package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.ThemeDto;
import com.openclassroom.mddapi.services.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getThemes() {
        return ResponseEntity.ok(themeService.getThemes());
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(@PathVariable Long id, Principal principal) {
        themeService.subscribe(id, principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("message", "Subscribed"));
    }

    @DeleteMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id, Principal principal) {
        themeService.unsubscribe(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}