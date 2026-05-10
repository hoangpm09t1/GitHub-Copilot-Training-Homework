# Plan: Exercise 2 - CreateUserRequest DTO (Day 2)

## Context
Day 2 Exercise 2 requires creating a validation DTO for user creation requests. The project already has `spring-boot-starter-validation` (Jakarta Validation) and Lombok configured in pom.xml.

## Target File
Create: `src/main/java/training/copilot/homework/dto/CreateUserRequest.java`

## Implementation

```java
package training.copilot.homework.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$",
             message = "Password must contain at least one letter and one digit")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String fullName;
}
```

## Key Annotations Checklist
- `@NotBlank` on all four fields
- `@Size(min = 5, max = 20)` on `username`
- `@Size(min = 8, max = 30)` + `@Pattern` on `password` (lookahead ensures ≥1 letter AND ≥1 digit)
- `@Email` on `email`
- Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

## Verification
1. Compile: `mvnw.cmd compile`
2. Context load test passes: `mvnw.cmd test`
