package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Article} entities within the MDD platform.
 * Provides standard abstraction for database operations to support
 * the developer community's shared content.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}