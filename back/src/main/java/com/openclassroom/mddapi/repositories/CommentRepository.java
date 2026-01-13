package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Comment} entities.
 * Manages developer feedback and peer collaboration interactions.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}