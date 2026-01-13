package com.openclassroom.mddapi.dtos.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new developer profile.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    /**
     * Valid email address used for login and notifications.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    private String email;

    /**
     * Unique public name or handle. Between 3 and 50 characters.
     */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    /**
     * Secure password. Must be between 8 and 64 characters.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    private String password;
}