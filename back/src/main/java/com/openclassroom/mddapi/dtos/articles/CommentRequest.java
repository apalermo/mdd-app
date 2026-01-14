package com.openclassroom.mddapi.dtos.articles;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for posting a new comment.
 * Encourages collaborative feedback between developers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequest {

    /**
     * Message content of the comment.
     * Constraints: 2 to 2000 characters.
     */
    @NotBlank(message = "Le contenu du commentaire ne peut pas être vide")
    @Size(min = 2, max = 2000, message = "Le commentaire doit faire entre 2 et 2000 caractères")
    private String content;
}