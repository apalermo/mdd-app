package com.openclassroom.mddapi.dtos.articles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArticleRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String content;

    @NotNull(message = "Le thème est obligatoire")
    private Long themeId;
}