package com.openclassroom.mddapi.dtos.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object returned upon successful authentication.
 * Contains the Bearer token required for accessing protected resources.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponse {
    /**
     * The JSON Web Token (JWT) to be used in the Authorization header.
     */
    private String token;
}