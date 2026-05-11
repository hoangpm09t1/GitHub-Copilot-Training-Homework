# Day 5 Plan: Comprehensive Testing & Quality Assurance

## Tổng quan codebase hiện tại

Project Day 3/4 là multi-module Maven (`core`, `service`, `api`) với:
- **Entities**: User, Category, Product, Order, OrderItem
- **Services**: UserService, CategoryService, ProductService, OrderService
- **Controllers**: AuthController, UserController, ProductController, CategoryController, OrderController
- **Repositories**: UserRepository, CategoryRepository, ProductRepository, OrderRepository, OrderItemRepository
- **Tests hiện có**: Unit tests cho 3 services + Integration tests cho 3 controllers (chưa dùng Testcontainers)

---

## Step 1: Setup Testing Infrastructure (15 phút)

### 1.1 Thêm dependencies vào `api/pom.xml`

```xml
<!-- Testcontainers -->
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>mysql</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <scope>test</scope>
</dependency>
```

### 1.2 Tạo base class `AbstractIntegrationTest`

- File: `api/src/test/java/.../AbstractIntegrationTest.java`
- Dùng `@Testcontainers` + `@Container` để spin up MySQL container
- Dùng `@DynamicPropertySource` để inject JDBC URL/credentials vào Spring context
- Copilot prompt: `// Create a Spring Boot integration test base class using Testcontainers for MySQL`

### 1.3 Tạo `application-test.properties`

- File: `api/src/test/resources/application-test.properties`
- Cấu hình Hibernate DDL, logging cho test environment

---

## Step 2: TDD - Implement Order Cancellation (25 phút)

### 2.1 Viết test TRƯỚC (Red phase)

File: `service/src/test/java/.../OrderServiceImplTest.java` — thêm:

| Test method | Scenario |
|---|---|
| `testCancelOrder_Success` | Order PENDING → CANCELLED, stock được hoàn trả |
| `testCancelOrder_AlreadyShipped` | Order SHIPPED → throw exception |
| `testCancelOrder_NotFound` | orderId không tồn tại → throw `ResourceNotFoundException` |

### 2.2 Implement feature (Green phase)

- Thêm `cancelOrder(Long orderId)` vào `OrderService` interface
- Implement trong `OrderServiceImpl`:
  - Tìm order, kiểm tra status có phải PENDING/PROCESSING không
  - Đổi status → CANCELLED
  - Loop qua `OrderItem`, hoàn trả `quantity` về `Product.stock`
  - Save order

### 2.3 Refactor (Refactor phase)

- Kiểm tra logic edge cases, clean up code

---

## Step 3: Unit Tests - Service Layer (25 phút)

Dùng `@ExtendWith(MockitoExtension.class)`, mock repositories.

### 3.1 `UserServiceImplTest`

| Test method | Scenario |
|---|---|
| `testAddUser_Success` | Email mới, encode password, trả về UserDTO |
| `testAddUser_DuplicateEmail` | Email đã tồn tại → throw exception |
| `testGetUserById_Found` | Tìm thấy → trả về UserDTO |
| `testGetUserById_NotFound` | Không tìm thấy → throw exception |
| `testUpdateUser_Success` | Cập nhật thành công |
| `testDeleteUser_Success` | Xoá thành công, verify `deleteById` được gọi |

### 3.2 `ProductServiceImplTest`

| Test method | Scenario |
|---|---|
| `testCreateProduct_Success` | Tạo product, category hợp lệ |
| `testCreateProduct_CategoryNotFound` | Category không tồn tại → throw exception |
| `testSearchProducts` | Filter theo name/category/price range |
| `testUpdateStock_Success` | Cập nhật stock |
| `testUpdateStock_InsufficientStock` | Stock âm → throw `InsufficientStockException` |

### 3.3 `OrderServiceImplTest`

| Test method | Scenario |
|---|---|
| `testPlaceOrder_Success` | Đặt hàng thành công, stock bị trừ |
| `testPlaceOrder_InsufficientStock` | Stock không đủ → throw exception |
| `testPlaceOrder_ProductNotFound` | Product không tồn tại → throw exception |
| `testCancelOrder_Success` | Order PENDING → CANCELLED, stock hoàn trả |
| `testCancelOrder_AlreadyShipped` | Order SHIPPED → throw exception |
| `testGetOrdersByUser_Success` | Lấy orders của user |

---

## Step 4: Integration Tests (35 phút)

### 4.1 Repository Tests (`@DataJpaTest` + Testcontainers)

Files mới cần tạo:

| File | Nội dung test |
|---|---|
| `ProductRepositoryTest` | `findByNameContaining`, `findByCategoryId`, filter giá |
| `UserRepositoryTest` | `findByEmail` |
| `OrderRepositoryTest` | `findByUserId`, query theo status |

### 4.2 Controller Tests (`@WebMvcTest` + `@MockBean`)

**`UserControllerTest`**

| Method | Endpoint | Expected |
|---|---|---|
| GET | `/api/users` | 200 + list |
| GET | `/api/users/{id}` | 200 hoặc 404 |
| POST | `/api/users` | 201 hoặc 400 (validation) |
| PUT | `/api/users/{id}` | 200 hoặc 404 |
| DELETE | `/api/users/{id}` | 204 |

**`ProductControllerTest`**

| Method | Endpoint | Expected |
|---|---|---|
| GET | `/api/products` | 200 + list |
| GET | `/api/products/search?name=...` | 200 + filtered list |
| POST | `/api/products` | 201 hoặc 400 |
| PUT | `/api/products/{id}/stock` | 200 hoặc 409 (insufficient) |

**`OrderControllerTest`**

| Method | Endpoint | Expected |
|---|---|---|
| POST | `/api/orders` | 201 hoặc 409 |
| GET | `/api/orders/{id}` | 200 hoặc 404 |
| PATCH | `/api/orders/{id}/cancel` | 200 hoặc 400 |

### 4.3 End-to-End Test (`@SpringBootTest` + Testcontainers)

File: `EcommerceWorkflowIntegrationTest.java`

Kịch bản:
1. Register user mới
2. Create category + product (stock = 10)
3. Place order (quantity = 3) → verify stock còn 7, status = PENDING
4. Cancel order → verify stock hoàn về 10, status = CANCELLED
5. Place order thứ 2, thử cancel khi đã SHIPPED → verify exception

---

## Step 5: Code Coverage với JaCoCo (10 phút)

### 5.1 Thêm JaCoCo plugin vào `parent pom.xml`

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.12</version>
  <executions>
    <execution>
      <goals><goal>prepare-agent</goal></goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>verify</phase>
      <goals><goal>report</goal></goals>
    </execution>
    <execution>
      <id>check</id>
      <goals><goal>check</goal></goals>
      <configuration>
        <rules>
          <rule>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.80</minimum>
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### 5.2 Chạy và phân tích

```bash
mvn clean verify
# Mở target/site/jacoco/index.html
```

Nếu coverage < 80%, ưu tiên bổ sung tests cho:
- `GlobalExceptionHandler`
- `CategoryService`
- Security filters (nếu thời gian cho phép)

---

## Thứ tự thực hiện & ước tính thời gian

| Step | Task | Thời gian |
|---|---|---|
| 1 | Setup Testcontainers + dependencies | 15 phút |
| 2 | TDD Order Cancellation (test → code → refactor) | 25 phút |
| 3 | Unit tests Service layer | 25 phút |
| 4 | Integration tests (Repo + Controller + E2E) | 35 phút |
| 5 | JaCoCo setup + phân tích coverage | 10 phút |
| | **Tổng** | **~110 phút** |

---

## Câu hỏi lý thuyết cần trả lời

### 1. `@SpringBootTest` vs `@WebMvcTest`

- **`@SpringBootTest`**: Load toàn bộ application context (tất cả beans, security, database...). Dùng cho integration test khi cần kiểm tra tương tác giữa nhiều layer.
- **`@WebMvcTest`**: Chỉ load web layer (controllers, filters, `@ControllerAdvice`). Services, repositories không được khởi tạo — phải dùng `@MockBean`. Dùng khi muốn test controller logic riêng lẻ, nhanh hơn và nhẹ hơn.

### 2. AAA Pattern (Arrange - Act - Assert)

- **Arrange**: Chuẩn bị data, khởi tạo mock, setup điều kiện ban đầu
- **Act**: Gọi method/endpoint cần test
- **Assert**: Kiểm tra kết quả trả về, trạng thái sau khi thực thi, hoặc verify mock interactions

Lợi ích: Cấu trúc nhất quán giúp test dễ đọc, dễ debug khi fail, và dễ maintain khi business logic thay đổi.

### 3. H2 vs Testcontainers

- **H2**: In-memory database với dialect riêng. Một số SQL syntax, data type, constraint của MySQL không tương thích H2, dẫn đến test pass nhưng fail trên production.
- **Testcontainers**: Spin up container MySQL thật trong Docker. Môi trường test giống production 100%, phát hiện được các lỗi liên quan đến dialect, index, constraint, hay stored procedure đặc thù của MySQL.
