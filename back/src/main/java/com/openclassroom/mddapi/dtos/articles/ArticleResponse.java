package com.openclassroom.mddapi.dtos.articles;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
=======
>>>>>>> 386d206 (feat(articles): add CommentResponse and ArticleResponse DTOs for API output)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
=======
>>>>>>> 386d206 (feat(articles): add CommentResponse and ArticleResponse DTOs for API output)
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
<<<<<<< HEAD
<<<<<<< HEAD
    private ThemeResponse theme;
=======
    private Long themeId;
    private String themeName;
>>>>>>> 386d206 (feat(articles): add CommentResponse and ArticleResponse DTOs for API output)
=======
    private ThemeResponse theme;
>>>>>>> 08131d3 (feat(articles): add response DTOs with nested theme structure)
    private List<CommentResponse> comments;
    private LocalDateTime createdAt;
}