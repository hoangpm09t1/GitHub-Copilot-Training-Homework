# Multi-Module Spring Boot E-Commerce System

## Project Overview

This project is a multi-module Spring Boot application for managing users and e-commerce operations.  
The application was developed with the assistance of GitHub Copilot to improve development productivity and automate repetitive coding tasks.

The system supports:

- User CRUD operations
- Product Catalog Management
- Hierarchical Category Management
- Advanced Product Search with Pagination and Filtering
- Order Management with Transactional Business Logic
- Inventory Management (auto stock reduction on order)
- Request validation
- Centralized exception handling
- JWT authentication
- Caching with `@Cacheable`
- Scheduled Tasks with `@Scheduled`
- Native SQL Queries
- Swagger API documentation
- Unit and integration testing (42 tests)

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
│       │   ├── User.java
│       │   ├── Category.java
│       │   ├── Product.java
│       │   ├── Order.java
│       │   └── OrderItem.java
│       ├── dto/
│       │   ├── UserDTO.java
│       │   ├── CategoryDTO.java
│       │   ├── ProductDTO.java
│       │   ├── OrderDTO.java
│       │   ├── OrderItemDTO.java
│       │   └── CreateOrderRequestDTO.java
│       └── enums/
│           ├── Role.java
│           └── OrderStatus.java
│
├── service/  (Service Module)
│   ├── pom.xml
│   └── src/main/java/com/example/copilot/service/
│       ├── repository/
│       │   ├── UserRepository.java
│       │   ├── CategoryRepository.java
│       │   ├── ProductRepository.java
│       │   ├── OrderRepository.java
│       │   └── OrderItemRepository.java
│       ├── service/
│       │   ├── UserService.java
│       │   ├── CategoryService.java
│       │   ├── ProductService.java
│       │   └── OrderService.java
│       ├── implementation/
│       │   ├── UserServiceImpl.java
│       │   ├── CategoryServiceImpl.java
│       │   ├── ProductServiceImpl.java
│       │   ├── OrderServiceImpl.java
│       │   └── CustomUserDetailsService.java
│       └── exception/
│           └── InsufficientStockException.java
│
└── api/  (API Module)
    ├── pom.xml
    ├── src/main/java/com/example/copilot/api/
    │   ├── HomeworkApplication.java
    │   ├── controller/
    │   │   ├── UserController.java
    │   │   ├── AuthController.java
    │   │   ├── CategoryController.java
    │   │   ├── ProductController.java
    │   │   └── OrderController.java
    │   ├── dto/
    │   │   └── AuthRequest.java
    │   ├── exception/
    │   │   ├── ResourceNotFoundException.java
    │   │   ├── ErrorResponse.java
    │   │   └── GlobalExceptionHandler.java
    │   ├── scheduler/
    │   │   └── OrderScheduler.java
    │   ├── security/
    │   │   ├── JwtUtil.java
    │   │   └── JwtFilter.java
    │   └── config/
    │       ├── SecurityConfig.java
    │       ├── CacheConfig.java
    │       ├── OpenApiConfig.java
    │       └── JpaConfig.java
    ├── src/main/resources/
    │   └── application.yml
    └── src/test/java/com/example/copilot/api/
        ├── service/
        │   ├── UserServiceImplTest.java
        │   ├── ProductServiceImplTest.java
        │   └── OrderServiceImplTest.java
        └── controller/
            ├── UserControllerIntegrationTest.java
            ├── ProductControllerIntegrationTest.java
            └── OrderControllerIntegrationTest.java
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
│   ├── implementation
│   └── exception
└── api
    ├── controller
    ├── dto
    ├── exception
    ├── scheduler
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
- `Category.java` - Hierarchical category entity (self-referencing)
- `Product.java` - Product entity with category relationship
- `Order.java` - Order entity with user and order items
- `OrderItem.java` - Order item with price snapshot at purchase time
- `UserDTO.java`, `CategoryDTO.java`, `ProductDTO.java` - Request/response DTOs
- `OrderDTO.java`, `OrderItemDTO.java`, `CreateOrderRequestDTO.java` - Order DTOs
- `Role.java` - Enum (ADMIN, USER, MODERATOR)
- `OrderStatus.java` - Enum (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

---

## Service Module

Package: `com.example.copilot.service`

Contains:

- Business logic
- Service interfaces and implementations
- Repository layer
- Spring Security UserDetailsService
- Custom exceptions

Files:

- `UserService.java` / `UserServiceImpl.java` - User business logic
- `CategoryService.java` / `CategoryServiceImpl.java` - Category management with `@Cacheable`
- `ProductService.java` / `ProductServiceImpl.java` - Product management with `@Cacheable` / `@CacheEvict`
- `OrderService.java` / `OrderServiceImpl.java` - Transactional order placement
- `UserRepository.java`, `CategoryRepository.java`, `ProductRepository.java` - JPA repositories
- `OrderRepository.java`, `OrderItemRepository.java` - Order repositories
- `InsufficientStockException.java` - Custom exception for out-of-stock
- `CustomUserDetailsService.java` - Spring Security integration

---

## API Module

Package: `com.example.copilot.api`

Contains:

- REST controllers
- Exception handling
- Security configuration
- JWT authentication
- Caching configuration
- Scheduled tasks
- Swagger/OpenAPI configuration
- JPA configuration
- Application entry point

Files:

- `HomeworkApplication.java` - Main Spring Boot class (`@EnableCaching`, `@EnableScheduling`)
- `UserController.java`, `AuthController.java` - User and auth endpoints
- `CategoryController.java` - Category CRUD endpoints
- `ProductController.java` - Product CRUD + search/filter endpoints
- `OrderController.java` - Order placement and retrieval endpoints
- `OrderScheduler.java` - Auto-cancel PENDING orders older than 24h
- `GlobalExceptionHandler.java` - Handles `ResourceNotFoundException`, `InsufficientStockException`, validation errors
- `CacheConfig.java` - `ConcurrentMapCacheManager` for `products` and `categories`
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

## Category API

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/categories` | Yes | Create category (supports parent) |
| GET | `/api/categories` | No | Get all categories (cached) |
| GET | `/api/categories/{id}` | No | Get category by ID |

---

## Product API

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/products` | Yes | Create product |
| GET | `/api/products/{id}` | No | Get product by ID (cached) |
| GET | `/api/products` | No | Search/filter products with pagination |
| PUT | `/api/products/{id}` | Yes | Update product |
| DELETE | `/api/products/{id}` | Yes | Delete product |

Search query parameters: `keyword`, `categoryId`, `minPrice`, `maxPrice`, `page`, `size`

---

## Order API

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/orders` | Yes | Place a new order |
| GET | `/api/orders` | Yes | Get all orders |
| GET | `/api/users/{userId}/orders` | Yes | Get orders by user |

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

- `UserServiceImplTest` — 10 tests covering all user service methods
- `ProductServiceImplTest` — 9 tests covering product CRUD, search, and category assignment
- `OrderServiceImplTest` — 6 tests covering order placement, stock validation, and rollback
- Uses JUnit 5 + Mockito

## Integration Tests

- `UserControllerIntegrationTest` — 8 tests covering all user REST endpoints
- `ProductControllerIntegrationTest` — 4 tests covering product search and auth
- `OrderControllerIntegrationTest` — 5 tests covering order placement, out-of-stock (409), and auth
- Uses SpringBootTest + MockMvc + Spring Security Test

**Total: 42 tests — all passing**

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

# Advanced Features (Day 4)

## Caching

- `@Cacheable("products")` on `getProductById()`
- `@Cacheable("categories")` on `getAllCategories()`
- `@CacheEvict` on `updateProduct()`, `deleteProduct()`, and `placeOrder()` to keep cache consistent

## Native SQL Query

`ProductRepository.findTop5BestSellingProducts()` uses `nativeQuery = true` to join `products` and `order_items` and return top 5 by total quantity sold.

## Scheduled Task

`OrderScheduler` runs every hour (`fixedRate = 3600000`) to find all PENDING orders older than 24 hours and automatically change their status to CANCELLED.

## Transactional Order Placement

`placeOrder()` is `@Transactional` — if any item fails (insufficient stock), the entire transaction rolls back and no partial data is saved.

---

# GitHub Copilot Usage

GitHub Copilot was used throughout the project to assist with:

- Entity and DTO generation
- JPQL and native SQL query generation
- Repository and service implementation
- Transactional order logic
- REST controller generation
- JWT security configuration
- Exception handling
- Caching and scheduling setup
- Swagger setup
- Unit and integration test generation

All generated code was manually reviewed and adjusted where necessary.

---

# Reflection

This project demonstrated how GitHub Copilot can significantly accelerate enterprise application development. However, AI-generated code still requires careful validation, security review, and testing to ensure correctness and maintainability.
