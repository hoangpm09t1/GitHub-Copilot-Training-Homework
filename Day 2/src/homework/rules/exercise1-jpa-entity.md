# Exercise 1: JPA Entity Rules

## Required Annotations
- `@Entity` — marks class as JPA entity
- `@Table(name = "users")` — explicit table name (avoids SQL reserved word `user`)
- `@Id` — marks primary key field
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` — auto-increment PK
- `@Column(unique = true, nullable = false)` — on `username` and `email`
- `@Column(nullable = false)` — on `password`
- `@CreationTimestamp` + `@Column(updatable = false)` — on `createdAt`
- `@UpdateTimestamp` — on `updatedAt`

## Lombok Annotations
- `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

## Field Types
- `id`: `Long`
- `createdAt`, `updatedAt`: `LocalDateTime`
- Other fields: `String`
