package com.example.copilot.api.controller;

import com.example.copilot.api.exception.ResourceNotFoundException;
import com.example.copilot.core.dto.UserDTO;
import com.example.copilot.core.entity.User;
import com.example.copilot.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "User created"),
                   @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User found"),
                   @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User updated"),
                   @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDTO));
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "User deleted"),
                   @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }
}
