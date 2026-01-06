package com.openclassroom.mddapi.dtos.articles;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
=======
>>>>>>> c93089e (feat(articles): implement core service logic for retrieval and creation with TDD)
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
<<<<<<< HEAD
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
=======
>>>>>>> c93089e (feat(articles): implement core service logic for retrieval and creation with TDD)
public class ArticleRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String content;

    @NotNull(message = "Le thème est obligatoire")
    private Long themeId;
}