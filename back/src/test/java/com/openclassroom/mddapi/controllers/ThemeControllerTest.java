package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.mappers.ThemeMapper;
import com.openclassroom.mddapi.security.jwt.JwtService;
import com.openclassroom.mddapi.services.ThemeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThemeController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Theme Unit Tests")
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private ThemeMapper themeMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /api/themes - Should return list of themes")
    void getThemesShouldReturnList() throws Exception {
        // Arrange
        ThemeResponse themeResponse = ThemeResponse.builder().id(1L).title("Java").build();

        when(themeService.getThemes()).thenReturn(List.of(new Theme()));
        when(themeMapper.toDtoList(any())).thenReturn(List.of(themeResponse));

        // Act & Assert
        mockMvc.perform(get("/api/themes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Java"));
    }

    @Test
    @DisplayName("POST /api/themes/{id}/subscribe - Success")
    void subscribeShouldReturnNoContent() throws Exception {
        // Arrange
        Long themeId = 1L;
        doNothing().when(themeService).subscribe(eq(themeId), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/themes/{id}/subscribe", themeId)
                        .principal(() -> "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/themes/{id}/subscribe - Not Found")
    void subscribeShouldReturnNotFoundWhenThemeMissing() throws Exception {
        // Arrange
        doThrow(new NotFoundException("Theme not found"))
                .when(themeService).subscribe(eq(99L), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/themes/{id}/subscribe", 99L)
                        .principal(() -> "test@test.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/themes/{id}/unsubscribe - Success")
    void unsubscribeShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(themeService).unsubscribe(eq(1L), anyString());

        // Act & Assert
        mockMvc.perform(delete("/api/themes/{id}/unsubscribe", 1L)
                        .principal(() -> "test@test.com"))
                .andExpect(status().isNoContent());
    }
}