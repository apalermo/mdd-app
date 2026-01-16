package com.openclassroom.mddapi.services;

import com.openclassroom.mddapi.dtos.users.UserUpdateRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling user profile management and data retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by their unique email address.
     *
     * @param email user email.
     * @return the {@link User} entity.
     * @throws NotFoundException if user is not found.
     */
    public User getByEmail(String email) {
        log.info("Fetching profile for user: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User profile retrieval failed: {} not found", email);
                    return new NotFoundException("Utilisateur non trouvé avec l'adresse email : " + email);
                });
    }

    /**
     * Updates an existing user's profile information.
     *
     * @param userId            ID of the user to update.
     * @param userUpdateRequest new profile data.
     * @return the updated {@link User} entity.
     * @throws ConflictException if the new email or name is already in use by another user.
     */
    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        log.info("Attempting to update profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Update failed: User ID {} does not exist", userId);
                    return new NotFoundException("Utilisateur introuvable");
                });

        if (userRepository.existsByEmailAndIdNot(userUpdateRequest.getEmail(), userId)) {
            log.warn("Update conflict: email {} is already in use", userUpdateRequest.getEmail());
            throw new ConflictException("Cet email est déjà utilisé par un autre utilisateur.");
        }

        if (userRepository.existsByNameAndIdNot(userUpdateRequest.getName(), userId)) {
            log.warn("Update conflict: username '{}' is already taken", userUpdateRequest.getName());
            throw new ConflictException("Ce nom d'utilisateur est déjà pris.");
        }

        user.setEmail(userUpdateRequest.getEmail());
        user.setName(userUpdateRequest.getName());

        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            log.debug("Password update requested for user ID: {}", userId);
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("Profile successfully updated for user ID: {}", userId);
        return updatedUser;
    }
}