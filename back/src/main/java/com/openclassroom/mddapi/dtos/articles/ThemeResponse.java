package com.openclassroom.mddapi.dtos.articles;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
=======
>>>>>>> 08131d3 (feat(articles): add response DTOs with nested theme structure)
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
>>>>>>> 08131d3 (feat(articles): add response DTOs with nested theme structure)
public class ThemeResponse {
    private Long id;
    private String title;
}