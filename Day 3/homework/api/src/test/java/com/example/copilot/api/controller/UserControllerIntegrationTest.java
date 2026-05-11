package com.example.copilot.api.controller;

import com.example.copilot.core.dto.UserDTO;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.example.copilot.api.HomeworkApplication.class)
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    private MockMvc mockMvc;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
        user = new User(1L, "John Doe", "john@example.com", "encodedPassword", Role.USER);
        userDTO = new UserDTO(null, "John Doe", "john@example.com", "Password@123", Role.USER);
    }

    @Test
    void createUser_shouldReturn201_whenValidInput() throws Exception {
        when(userService.addUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUser_shouldReturn400_whenInvalidEmail() throws Exception {
        userDTO.setEmail("invalid-email");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_whenPasswordTooShort() throws Exception {
        userDTO.setPassword("abc");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAllUsers_shouldReturn200_whenAuthenticated() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void getAllUsers_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getUserById_shouldReturn200_whenExists() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturn200_whenValidInput() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deleteUser_shouldReturn204_whenExists() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getUserById_shouldReturn404_whenNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturn404_whenNotFound() throws Exception {
        when(userService.updateUser(eq(99L), any(UserDTO.class)))
            .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/users/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteUser_shouldReturn404_whenNotFound() throws Exception {
        org.mockito.Mockito.doThrow(new RuntimeException("User not found"))
            .when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/users/99"))
            .andExpect(status().isNotFound());
    }
}
