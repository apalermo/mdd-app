package com.openclassroom.mddapi.dtos.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user authentication.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequest {

    /**
     * Can be either the email address or the username.
     */
    @NotBlank(message = "L'identifiant est obligatoire")
    private String identifier;

    /**
     * User's private password.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}