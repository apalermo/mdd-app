package com.openclassroom.mddapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.mddapi.dtos.users.UserResponse;
import com.openclassroom.mddapi.dtos.users.UserUpdateRequest;
import com.openclassroom.mddapi.entities.User;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import com.openclassroom.mddapi.mappers.UserMapper;
import com.openclassroom.mddapi.security.jwt.JwtService;
import com.openclassroom.mddapi.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("User Controller Unit Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /me - Success: should return user profile")
    void shouldReturnAuthenticatedUserDetails() throws Exception {
        UserResponse mockResponse = UserResponse.builder()
                .email("me-user@test.com")
                .name("MeUser")
                .subscriptions(List.of())
                .build();

        when(userService.getByEmail("me-user@test.com")).thenReturn(new User());
        when(userMapper.toDto(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/users/me")
                        .principal(() -> "me-user@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("me-user@test.com"))
                .andExpect(jsonPath("$.name").value("MeUser"));
    }

    @Test
    @DisplayName("GET /me - Failure: should return 404 when user deleted")
    void shouldReturnNotFoundWhenUserDeleted() throws Exception {
        when(userService.getByEmail("deleted-user@test.com"))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/users/me")
                        .principal(() -> "deleted-user@test.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /me - Success: Update profile with password")
    void shouldUpdateUserProfileSuccessfully() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("UpdatedName", "updated@test.com", "newpassword123");

        UserResponse mockResponse = UserResponse.builder()
                .email("updated@test.com")
                .name("UpdatedName")
                .build();

        User mockUser = User.builder().id(1L).email("original@test.com").build();

        when(userService.getByEmail("original@test.com")).thenReturn(mockUser);
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(mockUser);
        when(userMapper.toDto(any())).thenReturn(mockResponse);

        mockMvc.perform(put("/api/users/me")
                        .principal(() -> "original@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@test.com"))
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    @DisplayName("PUT /me - Success: Update profile WITHOUT password")
    void shouldUpdateUserWithoutPassword() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("UpdatedNameNoPass", "updated@test.com", null);

        UserResponse mockResponse = UserResponse.builder()
                .email("updated@test.com")
                .name("UpdatedNameNoPass")
                .build();

        User mockUser = User.builder().id(1L).email("original@test.com").build();

        when(userService.getByEmail("original@test.com")).thenReturn(mockUser);
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(mockUser);
        when(userMapper.toDto(any())).thenReturn(mockResponse);

        mockMvc.perform(put("/api/users/me")
                        .principal(() -> "original@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedNameNoPass"));
    }

    @Test
    @DisplayName("PUT /me - Failure: Conflict on email")
    void shouldReturnConflictWhenEmailExists() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("Victim", "obstacle@test.com", "newpassword123");
        User mockUser = User.builder().id(1L).email("victim@test.com").build();

        when(userService.getByEmail("victim@test.com")).thenReturn(mockUser);

        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class)))
                .thenThrow(new ConflictException("Cet email est déjà utilisé par un autre utilisateur."));

        mockMvc.perform(put("/api/users/me")
                        .principal(() -> "victim@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}