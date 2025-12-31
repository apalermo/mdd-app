package com.openclassroom.mddapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserDto {

    private Long id;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    private String email;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    @Builder.Default
    private List<ThemeDto> subscriptions = List.of();

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}