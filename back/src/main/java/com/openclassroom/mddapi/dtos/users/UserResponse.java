package com.openclassroom.mddapi.dtos.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.openclassroom.mddapi.dtos.themes.ThemeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    @Builder.Default
    private List<ThemeResponse> subscriptions = List.of();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}