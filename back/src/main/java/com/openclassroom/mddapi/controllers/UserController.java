package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.UserDto;
import com.openclassroom.mddapi.dtos.UserUpdateRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.mappers.UserMapper;
import com.openclassroom.mddapi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Principal principal) {
        User user = userService.getByEmail(principal.getName());

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(@Valid @RequestBody UserUpdateRequest userUpdateRequest, Principal principal) {
        User currentUser = userService.getByEmail(principal.getName());
        Long userId = currentUser.getId();

        User updatedUser = userService.updateUser(userId, userUpdateRequest);

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }
}