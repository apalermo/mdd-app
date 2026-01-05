package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.ThemeDto;
import com.openclassroom.mddapi.dtos.UserDto;
import com.openclassroom.mddapi.entities.Theme;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'adresse email : " + email));

        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        List<ThemeDto> subscribedThemes = user.getSubscriptions().stream()
                .map(subscription -> mapThemeToDto(subscription.getTheme()))
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

    private ThemeDto mapThemeToDto(Theme theme) {
        if (theme == null) return null;
        ThemeDto dto = new ThemeDto();
        dto.setId(theme.getId());
        dto.setTitle(theme.getTitle());
        dto.setDescription(theme.getDescription());
        dto.setCreatedAt(theme.getCreatedAt());
        dto.setUpdatedAt(theme.getUpdatedAt());
        return dto;
    }
}
