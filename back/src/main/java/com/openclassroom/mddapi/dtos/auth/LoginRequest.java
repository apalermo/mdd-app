package com.openclassroom.mddapi.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "L'identifiant est obligatoire")
    private String identifier;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}