package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.users.UserResponse;
import com.openclassroom.mddapi.dtos.users.UserUpdateRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.mappers.UserMapper;
import com.openclassroom.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * REST controller for managing developer profiles.
 * Allows users to retrieve their own information and keep their professional data up to date.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing developer profile and settings")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves the profile of the currently authenticated developer.
     * Uses the security principal to identify the user.
     *
     * @param principal the authenticated user context.
     * @return the UserResponse containing profile and subscriptions.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Principal principal) {
        User user = userService.getByEmail(principal.getName());
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * Updates the authenticated developer's profile.
     * Handles changes to email, username, and password.
     *
     * @param userUpdateRequest the new profile data.
     * @param principal         the authenticated user context.
     * @return the updated UserResponse.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserUpdateRequest userUpdateRequest, Principal principal) {
        User currentUser = userService.getByEmail(principal.getName());
        Long userId = currentUser.getId();

        User updatedUser = userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }
}