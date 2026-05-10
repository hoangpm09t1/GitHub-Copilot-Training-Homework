# Exercise 2: DTO Validation Rules

## Required Annotations per Field
- `username`: `@NotBlank` + `@Size(min = 5, max = 20)`
- `password`: `@NotBlank` + `@Size(min = 8, max = 30)` + `@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$")`
- `email`: `@NotBlank` + `@Email`
- `fullName`: `@NotBlank`

## Lombok Annotations
- `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

## Notes
- Import from `jakarta.validation.constraints.*`
- `@Pattern` regex uses lookahead to enforce ≥1 letter AND ≥1 digit in password
- DTOs should NOT have JPA annotations
