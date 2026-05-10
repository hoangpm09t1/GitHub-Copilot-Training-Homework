# AI Development Plan - Day 4 E-commerce Feature Implementation

## Project Goal

Extend the existing multi-module Spring Boot application with core e-commerce functionalities using GitHub Copilot.

The system must support:

- Product Catalog Management
- Hierarchical Categories
- Advanced Product Search
- Order Management
- Inventory Management
- Transactional Business Logic
- Pagination and Filtering
- JPQL and Native SQL Queries

---

# Existing Multi-Module Structure

```text
homework/
│
├── core/
├── service/
└── api/
```

---

# Package Structure

```text
com.example.copilot
├── api
│   ├── controller
│   ├── exception
│   ├── security
│   └── config
│
├── core
│   ├── entity
│   ├── dto
│   └── enums
│
└── service
    ├── repository
    ├── service
    └── implementation
```

---

# Technologies

- Spring Boot 3.x
- Maven Multi-Module
- Spring Data JPA
- Hibernate
- GitHub Copilot
- JPQL
- Native SQL
- Pageable Pagination
- Spring Transactions
- JWT Authentication
- Postman

---

# Step 1 - Product Catalog System

## Core Module

Package:

```text
com.example.copilot.core
```

---

# Product Entity

Create:

```text
Product.java
```

Fields:

| Field | Type |
|---|---|
| id | Long |
| name | String |
| description | String |
| price | BigDecimal |
| stockQuantity | Integer |

Relationships:

```java
@ManyToOne
private Category category;
```

Requirements:

- Use Lombok
- Use JPA annotations
- Add validation annotations

---

# Category Entity

Create:

```text
Category.java
```

Fields:

| Field | Type |
|---|---|
| id | Long |
| name | String |

Hierarchical relationship:

```java
@ManyToOne
private Category parent;

@OneToMany
private Set<Category> children;
```

Requirements:

- Self-referencing hierarchy
- Parent-child category structure

---

# DTO Classes

Create:

```text
ProductDTO.java
CategoryDTO.java
```

Requirements:

- Use validation annotations
- Do not expose entities directly
- Support request/response handling

---

# Step 2 - Repository Layer

## Service Module

Package:

```text
com.example.copilot.service.repository
```

---

# Repositories to Create

```text
ProductRepository.java
CategoryRepository.java
```

---

# ProductRepository Requirements

Use:

```java
@Query
```

Create advanced JPQL search query supporting:

- keyword search
- category filtering
- minimum price
- maximum price
- pagination

---

# Search Filters

| Filter | Description |
|---|---|
| keyword | search name/description |
| categoryId | category filtering |
| minPrice | minimum price |
| maxPrice | maximum price |

Pagination:

```java
Pageable pageable
```

Return type:

```java
Page<Product>
```

---

# Example Copilot Prompt

```java
// Create a JPQL query to search products by keyword,
// categoryId, minPrice, and maxPrice with pagination
```

---

# Step 3 - Product Service Layer

## Files to Create

```text
ProductService.java
ProductServiceImpl.java
```

---

# ProductService Responsibilities

Methods:

```text
addProduct
updateProduct
deleteProduct
getProductById
getAllProducts
searchProducts
```

Responsibilities:

- DTO ↔ Entity conversion
- Validation
- Pagination handling
- Exception handling

---

# Step 4 - Order Management System

## Core Module

Create entities:

```text
Order.java
OrderItem.java
```

---

# Order Entity

Fields:

| Field | Type |
|---|---|
| id | Long |
| orderDate | LocalDateTime |
| status | OrderStatus |

Relationships:

```java
@ManyToOne
private User user;

@OneToMany
private List<OrderItem> orderItems;
```

---

# OrderStatus Enum

Create:

```text
OrderStatus.java
```

Values:

```text
PENDING
CONFIRMED
SHIPPED
DELIVERED
CANCELLED
```

---

# OrderItem Entity

Fields:

| Field | Type |
|---|---|
| id | Long |
| quantity | Integer |
| price | BigDecimal |

Relationships:

```java
@ManyToOne
private Order order;

@ManyToOne
private Product product;
```

---

# Important Business Rule

The `price` field in `OrderItem` must store the product price snapshot at purchase time.

Do NOT fetch the live product price later.

---

# DTO Classes

Create:

```text
OrderDTO.java
OrderItemDTO.java
CreateOrderRequestDTO.java
```

---

# CreateOrderRequestDTO Requirements

Must contain:

- userId
- productId
- quantity

Support multiple order items.

---

# Step 5 - Order Repository Layer

## Repositories to Create

```text
OrderRepository.java
OrderItemRepository.java
```

---

# Step 6 - Transactional Order Service

## Files to Create

```text
OrderService.java
OrderServiceImpl.java
```

---

# Critical Requirement

The `placeOrder()` method MUST use:

```java
@Transactional
```

---

# placeOrder() Workflow

```text
1. Create Order
2. Validate product stock
3. Reduce stockQuantity
4. Save updated Product
5. Create OrderItem
6. Save OrderItem
7. Link OrderItems to Order
8. Commit transaction
```

---

# Custom Exception

Create:

```text
InsufficientStockException.java
```

Throw exception when:

```text
requested quantity > available stock
```

---

# Transaction Requirement

If any order item fails:

- rollback entire transaction
- do not partially save data

---

# Step 7 - REST API Layer

## ProductController

Create endpoints:

| Method | Endpoint |
|---|---|
| POST | `/api/products` |
| GET | `/api/products/{id}` |
| GET | `/api/products` |
| PUT | `/api/products/{id}` |
| DELETE | `/api/products/{id}` |

---

# Product Search Endpoint

Endpoint:

```text
GET /api/products
```

Query parameters:

```text
keyword
categoryId
minPrice
maxPrice
page
size
```

---

# OrderController

Endpoints:

| Method | Endpoint |
|---|---|
| POST | `/api/orders` |
| GET | `/api/orders` |
| GET | `/api/users/{userId}/orders` |

---

# Step 8 - Validation and Exception Handling

Use validation annotations:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Min`

Continue using:

```java
@ControllerAdvice
```

Handle:

- ResourceNotFoundException
- InsufficientStockException
- ValidationException

---

# Step 9 - Postman Testing

Update Postman collection with tests for:

## Product APIs

- Create category
- Create product
- Search products
- Pagination
- Filtering combinations

---

## Order APIs

- Successful order placement
- Out-of-stock order
- Order history retrieval
- Stock reduction verification

Export updated Postman collection JSON.

---

# Step 10 - Optional Advanced Features

## Native SQL Query

Implement report:

```text
Top 5 best-selling products
```

Use:

```java
nativeQuery = true
```

---

# Caching

Use:

```java
@Cacheable
```

Targets:

```text
findProductById
findAllCategories
```

---

# Scheduled Task

Use:

```java
@Scheduled
```

Requirement:

```text
Automatically cancel orders
in PENDING status older than 24 hours
```

---

# GitHub Copilot Usage Strategy

Use Copilot for:

- Entity relationship generation
- JPQL query generation
- DTO generation
- Repository methods
- Transactional service logic
- REST controller generation
- Pagination logic
- Exception handling
- Test generation

---

# Recommended Copilot Workflow

```text
Create empty file
→ Write detailed comments
→ Trigger Copilot
→ Review generated code
→ Fix imports/errors
→ Build project
→ Run application
→ Test with Postman
```

---

# Build Commands

## Build project

```bash
mvn clean install
```

---

## Run API module

```bash
cd api
..\mvnw spring-boot:run
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
- Updated GitHub repository
- Updated Postman collection JSON
- Updated README.md
- Product Catalog implementation
- Order Management implementation
- JPQL queries
- Transactional services
- Unit tests
- Integration tests
```

---

# Development Notes

- Review all AI-generated code carefully
- Validate entity relationships
- Prevent circular JSON serialization issues
- Ensure transactional integrity
- Verify inventory updates
- Test edge cases thoroughly
- Keep modules clean and maintainable