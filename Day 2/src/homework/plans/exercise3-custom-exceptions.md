# Plan: Exercise 3 - Custom Exceptions (Day 2)

## Context
Day 2 Exercise 3 requires two custom runtime exceptions for business rule violations in the Spring Boot project.

## Target Files
- Create: `src/main/java/training/copilot/homework/exception/UserNotFoundException.java`
- Create: `src/main/java/training/copilot/homework/exception/DuplicateEmailException.java`

## Implementation

**UserNotFoundException.java**
```java
package training.copilot.homework.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
```

**DuplicateEmailException.java**
```java
package training.copilot.homework.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email already in use: " + email);
    }
}
```

## Notes
- Both extend `RuntimeException` (unchecked)
- No Lombok needed — constructors are simple one-liners
- No additional dependencies required

## Verification
1. Compile: `mvnw.cmd compile`
