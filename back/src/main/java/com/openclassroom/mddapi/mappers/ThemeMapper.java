package com.openclassroom.mddapi.mappers;

import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import com.openclassroom.mddapi.entities.Theme;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ThemeMapper {

    public ThemeResponse toResponse(Theme theme) {
        if (theme == null) return null;

        return ThemeResponse.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .description(theme.getDescription())
                .createdAt(theme.getCreatedAt())
                .updatedAt(theme.getUpdatedAt())
                .build();
    }

    public List<ThemeResponse> toDtoList(List<Theme> themes) {
        if (themes == null) return List.of();
        return themes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}