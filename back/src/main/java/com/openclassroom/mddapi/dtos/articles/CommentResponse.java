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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
=======
>>>>>>> 386d206 (feat(articles): add CommentResponse and ArticleResponse DTOs for API output)
public class CommentResponse {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}