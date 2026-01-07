package com.openclassroom.mddapi.mappers;

import com.openclassroom.mddapi.dtos.UserDto;
import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        List<ThemeResponse> subscribedThemes = user.getSubscriptions().stream()
                .map(subscription -> toThemeDto(subscription.getTheme()))
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .subscriptions(subscribedThemes)
                .build();
    }

    private ThemeResponse toThemeDto(Theme theme) {
        if (theme == null) return null;
        ThemeResponse dto = new ThemeResponse();
        dto.setId(theme.getId());
        dto.setTitle(theme.getTitle());
        dto.setDescription(theme.getDescription());
        dto.setCreatedAt(theme.getCreatedAt());
        dto.setUpdatedAt(theme.getUpdatedAt());
        return dto;
    }
}