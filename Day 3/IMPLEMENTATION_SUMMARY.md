# Day 3–5 Implementation Summary - Multi-Module E-Commerce System

## ✅ Implementation Complete

Successfully implemented a multi-module Spring Boot 4.x E-Commerce REST API with full TDD and JaCoCo coverage enforcement.

---

## 📦 Project Structure

```
homework/
├── core/                    # Core module (Entities, DTOs, Enums)
│   ├── src/main/java/com/example/copilot/core/
│   │   ├── entity/          # User, Category, Product, Order, OrderItem
│   │   ├── dto/             # UserDTO, CategoryDTO, ProductDTO, OrderDTO, OrderItemDTO, CreateOrderRequestDTO
│   │   └── enums/           # Role, OrderStatus
│   └── pom.xml
│
├── service/                 # Service module (Business Logic)
│   ├── src/main/java/com/example/copilot/service/
│   │   ├── repository/      # UserRepository, CategoryRepository, ProductRepository, OrderRepository
│   │   ├── service/         # UserService, CategoryService, ProductService, OrderService
│   │   ├── implementation/  # *ServiceImpl, CustomUserDetailsService
│   │   └── exception/       # InsufficientStockException
│   └── pom.xml
│
└── api/                     # API module (REST Controllers & Security)
    ├── src/main/java/com/example/copilot/api/
    │   ├── HomeworkApplication.java
    │   ├── controller/      # AuthController, UserController, CategoryController, ProductController, OrderController
    │   ├── dto/             # AuthRequest
    │   ├── exception/       # ResourceNotFoundException, ErrorResponse, GlobalExceptionHandler
    │   ├── scheduler/       # OrderScheduler
    │   ├── security/        # JwtUtil, JwtFilter
    │   └── config/          # SecurityConfig, CacheConfig, OpenApiConfig, JpaConfig
    ├── src/main/resources/
    │   └── application.yml
    ├── src/test/java/com/example/copilot/api/
    │   ├── AbstractIntegrationTest.java
    │   ├── EcommerceWorkflowIntegrationTest.java
    │   ├── controller/      # AuthControllerTest, CategoryControllerTest, UserControllerIntegrationTest,
    │   │                    # ProductControllerIntegrationTest, OrderControllerIntegrationTest
    │   ├── repository/      # UserRepositoryTest, ProductRepositoryTest, OrderRepositoryTest
    │   ├── security/        # JwtUtilTest
    │   └── service/         # UserServiceImplTest, ProductServiceImplTest, OrderServiceImplTest
    └── pom.xml
```

---

## 🎯 Implemented Features

### 1️⃣ **Core Module** — `com.example.copilot.core`
- ✅ `User.java`, `Category.java`, `Product.java`, `Order.java`, `OrderItem.java` - JPA Entities
- ✅ DTOs: `UserDTO`, `CategoryDTO`, `ProductDTO`, `OrderDTO`, `OrderItemDTO`, `CreateOrderRequestDTO`
- ✅ Enums: `Role` (ADMIN, USER, MODERATOR), `OrderStatus` (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

### 2️⃣ **Service Module** — `com.example.copilot.service`
- ✅ Repositories: `UserRepository`, `CategoryRepository`, `ProductRepository`, `OrderRepository`, `OrderItemRepository`
- ✅ `UserServiceImpl` - CRUD + BCrypt password encoding
- ✅ `CategoryServiceImpl` - CRUD with `@Cacheable("categories")`
- ✅ `ProductServiceImpl` - CRUD with `@Cacheable("products")` / `@CacheEvict`, JPQL search
- ✅ `OrderServiceImpl` - Transactional `placeOrder()`, `cancelOrder()` (stock restore), `getOrderById()`
- ✅ `CustomUserDetailsService` - Spring Security integration
- ✅ `InsufficientStockException` - Custom exception

### 3️⃣ **API Module** — `com.example.copilot.api`

#### REST Endpoints
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/login` | No | Generate JWT token |
| POST | `/api/users` | No | Create user |
| GET | `/api/users/{id}` | Yes | Get user by ID |
| GET | `/api/users` | Yes | Get all users |
| PUT | `/api/users/{id}` | Yes | Update user |
| DELETE | `/api/users/{id}` | Yes | Delete user |
| POST | `/api/categories` | Yes | Create category |
| GET | `/api/categories` | No | Get all categories |
| GET | `/api/categories/{id}` | No | Get category by ID |
| POST | `/api/products` | Yes | Create product |
| GET | `/api/products/{id}` | No | Get product by ID |
| GET | `/api/products` | No | Search/filter products with pagination |
| PUT | `/api/products/{id}` | Yes | Update product |
| DELETE | `/api/products/{id}` | Yes | Delete product |
| POST | `/api/orders` | Yes | Place order (transactional, reduces stock) |
| GET | `/api/orders` | Yes | Get all orders |
| GET | `/api/orders/{orderId}` | Yes | Get order by ID |
| PATCH | `/api/orders/{orderId}/cancel` | Yes | Cancel order (restores stock) |
| GET | `/api/users/{userId}/orders` | Yes | Get orders by user |

#### Security
- ✅ `SecurityConfig.java` - Stateless JWT filter chain, HTTP 401 for unauthenticated
- ✅ `JwtUtil.java` - generateToken, getUsernameFromToken, validateToken (JJWT 0.12.3)
- ✅ `JwtFilter.java` - Validates Bearer token on every request

#### Exception Handling
- ✅ `GlobalExceptionHandler.java` - Handles `ResourceNotFoundException` (404), `InsufficientStockException` (409), `IllegalStateException` (400), `MethodArgumentNotValidException` (400), `Exception` (500)

#### Other
- ✅ `OrderScheduler.java` - Auto-cancel PENDING orders older than 24h (`@Scheduled`)
- ✅ `CacheConfig.java` - `ConcurrentMapCacheManager` for `products` and `categories`
- ✅ `OpenApiConfig.java` - Swagger/OpenAPI 3 with JWT security scheme

---

## 🧪 Testing

### Unit Tests (JUnit 5 + Mockito)

| File | Tests | Description |
|------|-------|-------------|
| `UserServiceImplTest` | 10 | CRUD, email lookup, exception paths |
| `ProductServiceImplTest` | 9 | CRUD, search, category assignment |
| `OrderServiceImplTest` | 12 | placeOrder, cancelOrder (TDD), getOrderById, stock validation |
| `JwtUtilTest` | 5 | generateToken, getUsernameFromToken, validateToken (valid/invalid/tampered) |

### Integration Tests (SpringBootTest + MockMvc + Mockito)

| File | Tests | Description |
|------|-------|-------------|
| `UserControllerIntegrationTest` | 10 | All user endpoints + 404 paths |
| `ProductControllerIntegrationTest` | 7 | CRUD, search, auth |
| `OrderControllerIntegrationTest` | 8 | place, cancel, getById, 409, 400, 401 |
| `AuthControllerTest` | 2 | Login failure, validation error |
| `CategoryControllerTest` | 3 | Create, getAll, getById |

### Repository Tests (SpringBootTest + H2)

| File | Tests | Description |
|------|-------|-------------|
| `UserRepositoryTest` | 3 | findByEmail, save |
| `ProductRepositoryTest` | 5 | searchProducts with keyword/category/price |
| `OrderRepositoryTest` | 3 | findByUserId, findByStatusAndOrderDateBefore |

### End-to-End Tests

| File | Tests | Description |
|------|-------|-------------|
| `EcommerceWorkflowIntegrationTest` | 2 | Full workflow: place→cancel (stock restored), place→ship→cancel fails |

### Code Coverage

- **JaCoCo 0.8.13** with minimum **80% line coverage** enforced
- Report: `api/target/site/jacoco/index.html`

**Total: 81/81 tests PASS ✅ — Coverage ≥80% ✅**

---

## 🔐 Validation

### UserDTO Validation
| Field | Annotation | Rule |
|-------|-----------|------|
| name | `@NotBlank` | Required |
| email | `@Email` + `@NotBlank` | Valid email format |
| password | `@Size(min=8)` + `@Pattern` | Min 8 chars, complexity required |
| role | `@NotNull` | Required |

---

## 🚀 Running the Application

### Build
```bash
cd "Day 3/homework"
.\mvnw.cmd clean install -DskipTests
```

### Run
```bash
.\mvnw.cmd -pl api spring-boot:run
```

### Test
```bash
.\mvnw.cmd -pl api test
```

### Test with Coverage Report
```bash
.\mvnw.cmd clean verify
# Open: api/target/site/jacoco/index.html
```

### URLs
| Resource | URL |
|----------|-----|
| API Base | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| API Docs | `http://localhost:8080/v3/api-docs` |
| H2 Console | `http://localhost:8080/h2-console` |

---

## 💾 Database Configuration

- **Type**: H2 File-based Database
- **URL**: `jdbc:h2:file:./data/testdb`
- **Username**: `sa` / **Password**: (empty)
- **DDL Auto**: `update`
- **Dialect**: H2Dialect (auto-detected by Hibernate 7)

---

## 🔑 JWT Configuration

- **Secret**: configured in `application.yml`
- **Expiration**: 24 hours (86400000 ms)
- **Algorithm**: HS512
- **Header**: `Authorization: Bearer <token>`

---

## 📋 Dependencies

### Core Module
- `jakarta.persistence-api`
- `jakarta.validation-api`
- `lombok`

### Service Module
- `spring-boot-starter-data-jpa`
- `spring-security-crypto`
- `lombok`

### API Module
- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-validation`
- `spring-boot-starter-data-jpa`
- `jjwt-api / jjwt-impl / jjwt-jackson` (v0.12.3)
- `springdoc-openapi-starter-webmvc-ui` (v2.7.0)
- `h2` (runtime)
- `lombok`
- `spring-boot-starter-test` (test)
- `spring-security-test` (test)

---

## ✨ Key Technologies

- **Java 26**
- **Spring Boot 4.0.6**
- **Hibernate 7.x**
- **Maven Multi-Module**
- **JWT / JJWT 0.12.3**
- **H2 File-based Database**
- **Springdoc OpenAPI 2.7.0**
- **Lombok**
- **JUnit 5 / Mockito**

---

## ⚠️ Known Constraints

- `@EntityScan` annotation is not available in Spring Boot 4.x — entity scanning is configured manually in `JpaConfig.java` via `LocalContainerEntityManagerFactoryBean`
- `HibernateJpaAutoConfiguration` is excluded in `application.yml` to prevent conflict with custom JPA config
- Spring Security 6.x returns HTTP 403 by default for unauthenticated requests — `HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)` is configured to return HTTP 401 instead

---

## 🎓 Learning Outcomes

✅ Multi-module Maven project structure  
✅ Layered architecture (core / service / api)  
✅ Proper Java package naming convention (`com.example.copilot.*`)  
✅ JWT authentication with Spring Security  
✅ Manual JPA configuration across modules  
✅ REST API best practices with `ResponseEntity`  
✅ DTO validation and centralized exception handling  
✅ Swagger/OpenAPI 3 documentation  
✅ Spring Data JPA repositories with custom JPQL queries  
✅ BCrypt password encoding  
✅ Caching with `@Cacheable` / `@CacheEvict`  
✅ Scheduled tasks with `@Scheduled`  
✅ TDD (Red-Green-Refactor) for `cancelOrder` feature  
✅ Unit testing with JUnit 5 + Mockito  
✅ Integration testing with MockMvc + Spring Security Test  
✅ Repository testing with `@SpringBootTest`  
✅ End-to-end workflow testing  
✅ JaCoCo code coverage enforcement (≥80%)  
