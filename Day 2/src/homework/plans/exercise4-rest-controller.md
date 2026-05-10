# Plan: Exercise 4 - UserController REST Skeleton (Day 2)

## Context
Day 2 Exercise 4 requires a REST controller skeleton with 5 CRUD endpoints. It depends on `CreateUserRequest` (Exercise 2) and the custom exceptions from Exercise 3. No new dependencies needed.

## Target File
Create: `src/main/java/training/copilot/homework/controller/UserController.java`

## Implementation

```java
package training.copilot.homework.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.copilot.homework.dto.CreateUserRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<Long> register(@Valid @RequestBody CreateUserRequest request) {
        log.info("Register user: {}", request.getUsername());
        // TODO: delegate to service
        return ResponseEntity.ok(0L);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        // TODO: delegate to service
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(Pageable pageable) {
        log.info("Get all users, page: {}", pageable.getPageNumber());
        // TODO: delegate to service
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Object request) {
        log.info("Update user id: {}", id);
        // TODO: delegate to service
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Delete user id: {}", id);
        // TODO: delegate to service
        return ResponseEntity.noContent().build();
    }
}
```

## Key Annotations Checklist
- `@RestController` — marks as REST controller
- `@RequestMapping("/api/v1/users")` — base path
- `@Slf4j` — Lombok logger
- `@PostMapping("/register")` + `@Valid` + `@RequestBody` on register
- `@GetMapping("/{id}")` + `@PathVariable` on getById
- `@GetMapping("/")` + `Pageable` on getAll
- `@PutMapping("/{id}")` + `@PathVariable` + `@RequestBody` on update
- `@DeleteMapping("/{id}")` + `@PathVariable` on delete

## Verification
1. Compile: `mvnw.cmd compile`
2. Context load test: `mvnw.cmd test`
