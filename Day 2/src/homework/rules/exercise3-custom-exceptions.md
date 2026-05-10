# Exercise 3: Custom Exception Rules

## Pattern
- Extend `RuntimeException` (unchecked)
- Single constructor accepting the relevant identifier
- Pass a descriptive message to `super()`
- Place in `exception` package

## Examples
- `UserNotFoundException(Long id)` → `"User not found with id: " + id`
- `DuplicateEmailException(String email)` → `"Email already in use: " + email`

## Notes
- No Lombok needed for simple exception classes
- No additional dependencies required
