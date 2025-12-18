package com.openclassroom.mddapi.controllers;

import com.openclassroom.mddapi.dtos.UserDto;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();

        UserDto userDto = userService.getByEmail(userEmail);

        return ResponseEntity.ok(userDto);
    }
}
