# Multi-Module Spring Boot User Management System

## Project Overview

This project is a multi-module Spring Boot application for managing users.  
The application was developed with the assistance of GitHub Copilot to improve development productivity and automate repetitive coding tasks.

The system supports:

- User CRUD operations
- Request validation
- Centralized exception handling
- JWT authentication
- Swagger API documentation
- Unit and integration testing

---

# Technologies Used

- Java 26
- Spring Boot 4.0.6
- Spring Data JPA
- Hibernate 7.x
- Maven Multi-Module Project
- GitHub Copilot
- Spring Security
- JWT Authentication (JJWT 0.12.3)
- Swagger / Springdoc OpenAPI 2.7.0
- H2 File-based Database
- Lombok
- JUnit 5 / Mockito

---

# Project Structure

```text
homework/  (PARENT PROJECT)
│
├── pom.xml  (Parent POM)
│
├── core/  (Core Module)
│   ├── pom.xml
│   └── src/main/java/com/example/copilot/core/
│       ├── entity/
│       │   └── User.java
│       ├── dto/
│       │   └── UserDTO.java
│       └── enums/
│           └── Role.java
│
├── service/  (Service Module)
│   ├── pom.xml
│   └── src/main/java/com/example/copilot/service/
│       ├── repository/
│       │   └── UserRepository.java
│       ├── service/
│       │   └── UserService.java
│       └── implementation/
│           ├── UserServiceImpl.java
│           └── CustomUserDetailsService.java
│
└── api/  (API Module)
    ├── pom.xml
    ├── src/main/java/com/example/copilot/api/
    │   ├── HomeworkApplication.java
    │   ├── controller/
    │   │   ├── UserController.java
    │   │   └── AuthController.java
    │   ├── dto/
    │   │   └── AuthRequest.java
    │   ├── exception/
    │   │   ├── ResourceNotFoundException.java
    │   │   ├── ErrorResponse.java
    │   │   └── GlobalExceptionHandler.java
    │   ├── security/
    │   │   ├── JwtUtil.java
    │   │   └── JwtFilter.java
    │   └── config/
    │       ├── SecurityConfig.java
    │       ├── OpenApiConfig.java
    │       └── JpaConfig.java
    ├── src/main/resources/
    │   └── application.yml
    └── src/test/java/com/example/copilot/api/
        ├── service/
        │   └── UserServiceImplTest.java
        └── controller/
            └── UserControllerIntegrationTest.java
```

---

# Package Structure

```text
com.example.copilot
├── core
│   ├── entity
│   ├── dto
│   └── enums
├── service
│   ├── repository
│   ├── service
│   └── implementation
└── api
    ├── controller
    ├── dto
    ├── exception
    ├── security
    └── config
```

---

# Modules Description

## Core Module

Package: `com.example.copilot.core`

Contains:

- JPA entities
- DTO classes
- Enums
- Shared models

Files:

- `User.java` - JPA entity
- `UserDTO.java` - Data transfer object with validation
- `Role.java` - Enum (ADMIN, USER, MODERATOR)

---

## Service Module

Package: `com.example.copilot.service`

Contains:

- Business logic
- Service interfaces
- Service implementations
- Repository layer
- Spring Security UserDetailsService

Files:

- `UserService.java` - Service interface
- `UserServiceImpl.java` - Business logic implementation
- `UserRepository.java` - JPA repository
- `CustomUserDetailsService.java` - Spring Security integration

---

## API Module

Package: `com.example.copilot.api`

Contains:

- REST controllers
- Exception handling
- Security configuration
- JWT authentication
- Swagger/OpenAPI configuration
- JPA configuration
- Application entry point

Files:

- `HomeworkApplication.java` - Main Spring Boot class
- `UserController.java` - User CRUD endpoints
- `AuthController.java` - JWT login endpoint
- `AuthRequest.java` - Login request DTO
- `GlobalExceptionHandler.java` - Centralized error handling
- `ResourceNotFoundException.java` - Custom 404 exception
- `ErrorResponse.java` - Standardized error response
- `JwtUtil.java` - JWT generation/validation
- `JwtFilter.java` - JWT request filter
- `SecurityConfig.java` - Spring Security configuration
- `OpenApiConfig.java` - Swagger configuration
- `JpaConfig.java` - JPA/entity manager configuration

---

# Features

## User Management API

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/users` | No | Create user |
| GET | `/api/users/{id}` | Yes | Get user by ID |
| GET | `/api/users` | Yes | Get all users |
| PUT | `/api/users/{id}` | Yes | Update user |
| DELETE | `/api/users/{id}` | Yes | Delete user |
| POST | `/auth/login` | No | Generate JWT token |

---

# Validation

Validation is implemented using annotations:

- `@NotBlank` - name, email, password
- `@Email` - email field
- `@Size(min=8)` - password
- `@Pattern` - password complexity
- `@NotNull` - role

Validation errors are handled centrally using `@ControllerAdvice`.

---

# Exception Handling

The application uses centralized exception handling:

- `GlobalExceptionHandler` - catches all exceptions
- `ResourceNotFoundException` - thrown when user not found (HTTP 404)
- Validation errors return HTTP 400

Error response format:

```json
{
  "timestamp": "2026-05-10T10:00:00",
  "status": 400,
  "message": "Validation failed",
  "details": "email: must be a well-formed email address"
}
```

---

# JWT Authentication

- Login via `POST /auth/login` with `email` and `password`
- Returns a Bearer token valid for 24 hours
- Include token in subsequent requests: `Authorization: Bearer <token>`
- Algorithm: HS512

Public endpoints (no token required):

```text
POST /auth/login
POST /api/users
GET  /swagger-ui/**
GET  /v3/api-docs/**
```

---

# Swagger API Documentation

Access Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

API docs (JSON):

```text
http://localhost:8080/v3/api-docs
```

---

# Database

- **Type**: H2 File-based
- **URL**: `jdbc:h2:file:./data/testdb`
- **Username**: `sa` / **Password**: (empty)
- **DDL**: `update` (schema updated on startup, data persisted across restarts)
- **H2 Console**: `http://localhost:8080/h2-console`

---

# Testing

## Unit Tests

- `UserServiceImplTest` — 10 tests covering all service methods
- Uses JUnit 5 + Mockito

## Integration Tests

- `UserControllerIntegrationTest` — 8 tests covering all REST endpoints
- Uses SpringBootTest + MockMvc + Spring Security Test

## Run Tests

```bash
.\mvnw.cmd -pl api test
```

---

# Running the Application

## Step 1: Navigate to the homework folder

```bash
cd "Day 3/homework"
```

## Step 2: Build all modules

```bash
.\mvnw.cmd clean install -DskipTests
```

## Step 3: Run the application

```bash
.\mvnw.cmd -pl api spring-boot:run
```

Application starts on: `http://localhost:8080`

---

# Troubleshooting

## Must run from `homework` folder (not `api` folder)

The `.mvn` wrapper is in the `homework` folder. Always run from there:

```bash
cd "Day 3/homework"
.\mvnw.cmd -pl api spring-boot:run
```

## JPA / Entity scan issues

`JpaConfig.java` manually configures `LocalContainerEntityManagerFactoryBean` to scan `com.example.copilot.core.entity`. `HibernateJpaAutoConfiguration` is excluded via `application.yml` to avoid conflict.

---

# GitHub Copilot Usage

GitHub Copilot was used throughout the project to assist with:

- Entity and DTO generation
- Repository and service implementation
- REST controller generation
- JWT security configuration
- Exception handling
- Swagger setup
- Unit and integration test generation

All generated code was manually reviewed and adjusted where necessary.

---

# Reflection

This project demonstrated how GitHub Copilot can significantly accelerate enterprise application development. However, AI-generated code still requires careful validation, security review, and testing to ensure correctness and maintainability.
