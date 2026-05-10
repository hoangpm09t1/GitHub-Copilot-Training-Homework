# Exercise 4: REST Controller Rules

## Class-level Annotations
- `@RestController` — marks as REST controller returning JSON
- `@RequestMapping("/api/v1/users")` — base path
- `@Slf4j` — Lombok logger (use `log.info(...)`)

## Method-level Annotations by HTTP Verb
| Verb   | Annotation       | Extra params                        |
|--------|-----------------|-------------------------------------|
| POST   | `@PostMapping`  | `@Valid @RequestBody` on DTO param  |
| GET    | `@GetMapping`   | `@PathVariable` for `/{id}`         |
| GET    | `@GetMapping`   | `Pageable` for paginated list       |
| PUT    | `@PutMapping`   | `@PathVariable` + `@RequestBody`    |
| DELETE | `@DeleteMapping`| `@PathVariable`                     |

## Response Conventions
- Created resource → `ResponseEntity.ok(id)` or `ResponseEntity.status(201).body(...)`
- No content (delete) → `ResponseEntity.noContent().build()`
- Wildcard return type → `ResponseEntity<?>`

## Notes
- Import `jakarta.validation.Valid`
- Use `org.springframework.data.domain.Pageable` for pagination
