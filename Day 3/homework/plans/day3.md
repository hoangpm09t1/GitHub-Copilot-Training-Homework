# AI Development Plan - Multi-Module Spring Boot User Management System

## Project Goal

Build a modular Spring Boot 3.x User Management REST API using GitHub Copilot.

The system must support:

- CRUD operations
- DTO validation
- Centralized exception handling
- JWT authentication
- Swagger API documentation
- Unit testing with JUnit
- Integration testing with Mockito and MockMvc

---

# Project Architecture

Project type:

- Maven Multi-Module Project
- Spring Boot 3.x
- Layered Architecture

---

# Project Structure

```text
Day 3/homework/  (PARENT PROJECT)
│
├── pom.xml  (Parent POM - defines modules)
│
├── core/  (Core Module)
│   ├── pom.xml
│   └── src/main/
│       ├── entity/
│       ├── dto/
│       └── enums/
│
├── service/  (Service Module)
│   ├── pom.xml
│   ├── src/main/
│   │   ├── repository/
│   │   ├── service/
│   │   └── implementation/
│   └── src/main/resources/
│
└── api/  (API Module - Main Application)
    ├── pom.xml
    ├── src/main/
    │   ├── controller/
    │   ├── exception/
    │   ├── security/
    │   ├── config/
    │   └── HomeworkApplication.java
    ├── src/test/
    └── src/main/resources/
```

---

# Package Structure

```text
training.copilot.homework
├── api
│   ├── controller
│   ├── exception
│   └── security
│
├── core
│   ├── dto
│   ├── entity
│   └── enums
│
└── service
    ├── implementation
    ├── repository
    └── service
```

---

# Module Responsibilities

## 1. Core Module

Package:

```text
training.copilot.homework.core
```

Responsibilities:

- JPA entities
- DTO classes
- Enum definitions
- Shared models

---

## Files to Create

```text
User.java
UserDTO.java
Role.java
```

---

## User Entity Fields

| Field | Type |
|---|---|
| id | Long |
| name | String |
| email | String |
| password | String |
| role | Role |

---

## Validation Requirements

Use validation annotations:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Size`
- `@Pattern`

---

# 2. Service Module

Package:

```text
training.copilot.homework.service
```

Responsibilities:

- Business logic
- Repository layer
- Service interfaces
- DTO ↔ Entity conversion

---

## Files to Create

```text
UserRepository.java
UserService.java
UserServiceImpl.java
```

---

## Repository Requirements

- Extend `JpaRepository`
- Add custom query methods:
  - `findByEmail`

---

## Required Service Methods

```text
addUser
getUserById
getAllUsers
updateUser
deleteUser
```

---

# 3. API Module

Package:

```text
training.copilot.homework.api
```

Responsibilities:

- REST controllers
- Exception handling
- Security configuration
- JWT authentication
- Swagger configuration

---

## Files to Create

```text
UserController.java
AuthController.java
GlobalExceptionHandler.java
ResourceNotFoundException.java
ErrorResponse.java
JwtUtil.java
JwtFilter.java
SecurityConfig.java
OpenApiConfig.java
JpaConfig.java
AuthRequest.java (dto)
HomeworkApplication.java
```

---

# REST API Endpoints

## User Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users` | Get all users |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

---

## Authentication Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Generate JWT token |

---

# DTO Rules

## UserDTO

Fields:

| Field | Validation |
|---|---|
| name | `@NotBlank` |
| email | `@Email` |
| password | `@Size(min = 8)` |
| role | `@NotNull` |

Requirements:

- Do not expose password in API responses
- Use DTOs instead of returning entities directly

---

# Validation Requirements

Controller methods must use:

```java
@Valid
```

Validation errors should return:

- HTTP 400
- JSON error response

---

# Exception Handling

Use centralized exception handling with:

```java
@ControllerAdvice
```

---

## Exceptions to Handle

- `ResourceNotFoundException`
- `MethodArgumentNotValidException`
- `ValidationException`
- Generic `Exception`

---

## Error Response Format

```json
{
  "timestamp": "",
  "status": 400,
  "message": "",
  "details": ""
}
```

---

# Security Requirements

Use:

- Spring Security
- JWT Authentication
- Bearer Token Authorization

---

## Security Goals

- Secure API endpoints
- Validate JWT tokens
- Authenticate login requests
- Prevent unauthorized access

---

## Public Endpoints

```text
/auth/login
/swagger-ui/**
/v3/api-docs/**
```

---

# Swagger Requirements

Use:

- Springdoc OpenAPI

Swagger URL:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Testing Requirements

## Unit Testing

Use:

- JUnit 5
- Mockito

Test:

- UserService
- Repository interactions
- DTO conversion logic

---

## Integration Testing

Use:

- SpringBootTest
- MockMvc

Test:

- CRUD endpoints
- JWT authentication
- Validation failures
- Error responses

---

# Postman Testing

Create Postman collection for:

- Valid user creation
- Invalid email validation
- Missing required fields
- Fetch existing user
- Fetch non-existing user
- Update user
- Delete user
- JWT login
- Unauthorized access

Export Postman collection JSON file.

---

# GitHub Copilot Usage Strategy

Use GitHub Copilot for:

- Entity generation
- DTO generation
- Repository generation
- Service implementation
- CRUD controller generation
- JWT security configuration
- Swagger setup
- Unit and integration tests
- Exception handling

---

# Recommended Copilot Workflow

```text
Create empty file
→ Write detailed comments
→ Trigger Copilot
→ Review generated code
→ Fix imports/errors
→ Run application
→ Test APIs
```

---

# Coding Standards

Requirements:

- Use Lombok
- Use ResponseEntity
- Use constructor injection
- Follow layered architecture
- Keep modules independent
- Use DTOs instead of exposing entities
- Use proper package naming
- Add validation annotations
- Maintain clean and readable code

---

# Build Commands

## Build project

```bash
mvn clean install
```

---

## Run application

```bash
mvn spring-boot:run
```

---

## Run tests

```bash
mvn test
```

---

# Final Deliverables

Required submission files:

```text
- Multi-module Spring Boot project
- README.md
- PLAN.md
- Postman collection JSON
- Unit tests
- Integration tests
```

---

# Development Notes

- Review all AI-generated code carefully
- Validate imports and dependencies
- Ensure secure coding practices
- Test all endpoints using Postman
- Verify JWT authentication flow
- Keep modules clean and maintainable

---

# IMPLEMENTATION STATUS

## ✅ COMPLETED

### Phase 1: Project Setup & Core Module
- [x] Parent pom.xml with 3 modules (core, service, api)
- [x] Core module pom.xml
- [x] `com/example/copilot/core/entity/User.java` - JPA entity with validation
- [x] `com/example/copilot/core/dto/UserDTO.java` - Data transfer object
- [x] `com/example/copilot/core/enums/Role.java` - User roles enumeration (ADMIN, USER, MODERATOR)

### Phase 2: Service Layer Module
- [x] Service module pom.xml
- [x] `com/example/copilot/service/repository/UserRepository.java` - JpaRepository with `findByEmail()`
- [x] `com/example/copilot/service/service/UserService.java` - Service interface
- [x] `com/example/copilot/service/implementation/UserServiceImpl.java` - Service implementation with BCrypt encryption
- [x] `com/example/copilot/service/implementation/CustomUserDetailsService.java` - Spring Security UserDetailsService

### Phase 3: API Layer Module
- [x] API module pom.xml with mainClass = `com.example.copilot.api.HomeworkApplication`
- [x] `com/example/copilot/api/HomeworkApplication.java` - Main class with `@ComponentScan(basePackages = "com.example.copilot")`
- [x] `com/example/copilot/api/security/JwtUtil.java` - JWT token generation/validation (JJWT 0.12.3, HS512)
- [x] `com/example/copilot/api/security/JwtFilter.java` - JWT authentication filter
- [x] `com/example/copilot/api/exception/ResourceNotFoundException.java` - HTTP 404 custom exception
- [x] `com/example/copilot/api/exception/ErrorResponse.java` - Standardized error response DTO
- [x] `com/example/copilot/api/exception/GlobalExceptionHandler.java` - Centralized `@ControllerAdvice`
- [x] `com/example/copilot/api/config/SecurityConfig.java` - Spring Security stateless, HTTP 401 for unauthenticated
- [x] `com/example/copilot/api/config/OpenApiConfig.java` - Swagger/OpenAPI 3 with JWT security scheme
- [x] `com/example/copilot/api/config/JpaConfig.java` - Manual JPA config scanning `com.example.copilot.core.entity`
- [x] `com/example/copilot/api/dto/AuthRequest.java` - Login request DTO
- [x] `com/example/copilot/api/controller/UserController.java` - REST CRUD endpoints
- [x] `com/example/copilot/api/controller/AuthController.java` - JWT login endpoint
- [x] `src/main/resources/application.yml` - Database, JPA, JWT, Swagger, logging config

### Phase 4: Testing
- [x] `UserServiceImplTest.java` - 10 unit tests (JUnit 5 + Mockito)
- [x] `UserControllerIntegrationTest.java` - 8 integration tests (MockMvc + Spring Security Test)
- [x] All 18 tests PASS

### Build & Run Verification
- [x] `mvn clean install -DskipTests` — BUILD SUCCESS (all 3 modules)
- [x] `mvnw -pl api spring-boot:run` — Application started on port 8080
- [x] H2 file-based database initialized with `update` (data persisted across restarts)
- [x] JPA EntityManagerFactory initialized for `com.example.copilot.core.entity`
- [x] Tomcat started successfully

### Documentation
- [x] README.md - Project documentation (updated)
- [x] IMPLEMENTATION_SUMMARY.md - Full implementation summary (updated)
- [x] day3.md - This plan file (updated)

## ⚠️ Known Constraints & Fixes Applied

| Issue | Fix |
|-------|-----|
| Java files in `src/main/` (non-standard) | Moved all files to `src/main/java/` |
| Flat package names (`entity`, `controller`) → not standard | Refactored to `com.example.copilot.*` package structure |
| `HomeworkApplication` scanned Spring internals → ConflictingBeanDefinitionException | Moved to `com.example.copilot.api` with `@ComponentScan(basePackages = "com.example.copilot")` |
| `@EntityScan` not available in Spring Boot 4.x | Replaced with manual `LocalContainerEntityManagerFactoryBean` in `JpaConfig` |
| Conflict between custom and auto-configured EntityManagerFactory | Excluded `HibernateJpaAutoConfiguration` via `spring.autoconfigure.exclude` in `application.yml` |
| Spring Security returns 403 for unauthenticated requests | Added `HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)` in `SecurityConfig` |

## 🚀 Application Running

```
Started HomeworkApplication in ~4.4 seconds
Tomcat started on port 8080
```

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui/index.html` | Swagger UI |
| `http://localhost:8080/v3/api-docs` | OpenAPI JSON |
| `http://localhost:8080/h2-console` | H2 Database Console |
| `http://localhost:8080/auth/login` | JWT Login |
| `http://localhost:8080/api/users` | User API |
