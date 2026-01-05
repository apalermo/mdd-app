package com.openclassroom.mddapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThemeDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 50, message = "Le titre ne doit pas dépasser 50 caractères")
    private String title;

    @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}