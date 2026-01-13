package com.openclassroom.mddapi.repositories;

import com.openclassroom.mddapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * Essential for identity management, providing methods to verify uniqueness
 * of credentials and retrieve developer profiles.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique email address.
     *
     * @param email the email to search for.
     * @return an Optional containing the found user.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by either their email or their display name.
     * Used for flexible authentication (Login with email or username).
     *
     * @param email user email identifier.
     * @param name  user display name identifier.
     * @return an Optional containing the found user.
     */
    Optional<User> findByEmailOrName(String email, String name);

    /**
     * Checks if an email is already registered in the system.
     *
     * @param email the email to check.
     * @return true if the email exists.
     */
    Boolean existsByEmail(String email);

    /**
     * Checks if a username is already taken.
     *
     * @param name the name to check.
     * @return true if the name exists.
     */
    Boolean existsByName(String name);

    /**
     * Verifies if an email is used by any user OTHER than the specified ID.
     * Used during profile updates to prevent email duplication.
     */
    Boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Verifies if a username is used by any user OTHER than the specified ID.
     * Used during profile updates to prevent name duplication.
     */
    Boolean existsByNameAndIdNot(String name, Long id);
}