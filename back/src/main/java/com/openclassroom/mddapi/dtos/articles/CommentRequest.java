package com.openclassroom.mddapi.dtos.articles;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
=======
>>>>>>> 83838fb (feat(articles): implement addComment with full TDD coverage including error cases)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequest {

=======
public class CommentRequest {
    
>>>>>>> 83838fb (feat(articles): implement addComment with full TDD coverage including error cases)
    @NotBlank(message = "Le contenu du commentaire ne peut pas être vide")
    @Size(min = 2, max = 2000, message = "Le commentaire doit faire entre 2 et 2000 caractères")
    private String content;
}
