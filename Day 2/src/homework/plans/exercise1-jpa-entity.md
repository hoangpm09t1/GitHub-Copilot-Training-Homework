# Plan: Exercise 1 - JPA User Entity (Day 2)

## Context
Day 2 homework requires creating a JPA `User` entity in a Spring Boot 4.0.6 Maven project located at `e:\2026\GitHub_Copilot_Training\Homework\Day 2\src\homework`. The project already has `spring-boot-starter-data-jpa` and `lombok` as dependencies, so no pom.xml changes are needed.

## Target File
Create: `src/main/java/training/copilot/homework/entity/User.java`

## Implementation

```java
package training.copilot.homework.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // hashed

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

## Key Annotations Checklist
- `@Entity` — marks class as JPA entity
- `@Table(name = "users")` — maps to `users` table (avoids reserved word `user` in SQL)
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` — auto-generated PK
- `@Column(unique = true, nullable = false)` on `username` and `email`
- `@CreationTimestamp` + `@Column(updatable = false)` on `createdAt`
- `@UpdateTimestamp` on `updatedAt`
- Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

## Verification
1. File compiles: `mvnw.cmd compile` in the project directory
2. `HomeworkApplicationTests` context load test still passes: `mvnw.cmd test`
