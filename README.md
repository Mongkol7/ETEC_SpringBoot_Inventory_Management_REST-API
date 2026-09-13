# E-Commerce REST API (`etec_spring04`)

A robust, enterprise-grade Spring Boot RESTful API for an **E-Commerce / Online Store Management System**. This application provides complete management of product categories, products with Cloudinary image hosting, user accounts, and an order processing engine with real-time stock validation and inventory restocking.

---

## 🚀 Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 4.x (Spring Web, Spring Data JPA, Spring Validation)
- **Database**: PostgreSQL
- **ORM / Persistence**: Hibernate / JPA
- **DTO Mapping**: MapStruct & Lombok
- **Cloud Storage**: Cloudinary SDK (Image upload and media asset management)
- **Build Tool**: Maven

---

## 📁 Project Architecture & Directory Structure

```text
src/main/java/com/example/etec_spring04/
├── config/                  # Cloudinary and application configurations
│   ├── CloudinaryConfig.java
│   └── client/
│       └── CloudinaryService.java
├── controller/              # REST Controllers exposing HTTP endpoints
│   ├── CategoryController.java
│   ├── ProductController.java
│   ├── UserController.java
│   └── OrderController.java
├── dto/                     # Request and Response Data Transfer Objects
│   ├── Request/
│   │   ├── CategoryRequest.java
│   │   ├── ProductRequest.java
│   │   ├── UserRequest.java
│   │   ├── OrderRequest.java
│   │   └── OrderItemRequest.java
│   └── Response/
│       ├── ApiResponse.java
│       ├── CategoryResponse.java
│       ├── ProductResponse.java
│       ├── UserResponse.java
│       ├── OrderResponse.java
│       └── OrderItemResponse.java
├── entity/                  # JPA Entities (mapped to tbl_* tables)
│   ├── Category.java        # tbl_categories
│   ├── Product.java         # tbl_products
│   ├── User.java            # tbl_users
│   ├── Order.java           # tbl_orders
│   └── OrderItem.java       # tbl_order_items
├── exception/               # Custom exceptions and Global Exception Handler
│   ├── BadRequestException.java
│   ├── ResourceNotFoundException.java
│   ├── UnexpectedErrorException.java
│   └── GlobalExceptionHandler.java
├── mapper/                  # MapStruct interface mappers
│   ├── CategoryMapper.java
│   ├── ProductMapper.java
│   ├── UserMapper.java
│   └── OrderMapper.java
├── repository/              # Spring Data JPA repositories with query optimization
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   ├── UserRepository.java
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
└── service/                 # Business logic layer
    ├── CategoryService.java
    ├── ProductService.java
    ├── UserService.java
    ├── OrderService.java
    └── impl/
        ├── CategoryServiceImpl.java
        ├── ProductServiceImpl.java
        ├── UserServiceImpl.java
        └── OrderServiceImpl.java
```

---

## ⚙️ Getting Started & Setup

### 1. Prerequisites
- **JDK 21** or later installed
- **PostgreSQL 14+** running locally or remotely
- **Cloudinary Account** (for product image uploads)

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE etec_springboot04;
```

### 3. Application Configuration
Copy the sample properties file:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update `src/main/resources/application.properties` with your credentials:
```properties
# Server Port
server.port=8082

# Database Credentials
spring.datasource.url=jdbc:postgresql://localhost:5432/etec_springboot04
spring.datasource.username=postgres
spring.datasource.password=your_password

# Cloudinary Configuration
cloudinary.cloud-name=your_cloudinary_cloud_name
cloudinary.api-key=your_cloudinary_api_key
cloudinary.api-secret=your_cloudinary_api_secret
```

### 4. Build and Run
Using the Maven wrapper:

```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

The application will start at `http://localhost:8082`.

---

## 📡 API Endpoints Overview

All responses follow a consistent `ApiResponse<T>` wrapper structure:
```json
{
  "success": true,
  "message": "Operation description",
  "data": { ... },
  "timestamp": "2026-09-13T14:20:00"
}
```

### 1. Categories (`/api/categories`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/categories` | Create a new category |
| `GET` | `/api/categories` | List all categories |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `PUT` | `/api/categories/{id}` | Update an existing category |
| `DELETE` | `/api/categories/{id}` | Delete category |

### 2. Products (`/api/products`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/products` | Create product (JSON or multipart/form-data) |
| `POST` | `/api/products/with-image` | Create product with image upload |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get product details by ID |
| `GET` | `/api/products/category/{categoryId}` | List products under a specific category |
| `PUT` | `/api/products/{id}` | Update product details |
| `PUT` | `/api/products/{id}/image` | Update only product image (`file` parameter) |
| `DELETE` | `/api/products/{id}` | Delete product and remove image from Cloudinary |

### 3. Users (`/api/users`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/users` | Register a new user |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get user by ID |
| `PUT` | `/api/users/{id}` | Update user details |
| `DELETE` | `/api/users/{id}` | Delete user |

### 4. Orders (`/api/orders`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/orders` | Create an order (validates & deducts inventory stock) |
| `GET` | `/api/orders` | List all orders |
| `GET` | `/api/orders/{id}` | Get order by ID |
| `GET` | `/api/orders/user/{userId}` | List all orders for a specific user |
| `PATCH` | `/api/orders/{id}/status?status=COMPLETED` | Update order status |
| `PUT` | `/api/orders/{id}/cancel` | Cancel order (automatically restocks inventory) |

---

## 🛡️ Business Logic Highlights

1. **Inventory Stock Validation & Real-time Alerts**:
   - During order placement (`POST /api/orders`), available stock is verified against the requested quantity.
   - If stock is insufficient, a `400 Bad Request` is returned immediately with details:
     ```json
     {
       "success": false,
       "message": "Insufficient stock for product 'Laptop' (ID: 5). Available: 2, Requested: 5",
       "data": null,
       "timestamp": "2026-09-13T14:25:00"
     }
     ```
2. **Order Cancellation & Restocking**:
   - Cancelling an active order automatically adds back all ordered quantities to each product's stock count.
3. **Cloudinary Integration**:
   - Image assets uploaded via multipart form requests are saved to Cloudinary and their `image_url` and `public_id` are tracked in the database. Deleting a product automatically cleans up the remote Cloudinary image asset.
4. **Performance Optimized Repositories**:
   - JPA entity relationships utilize `FetchType.LAZY` with `JOIN FETCH` / `@EntityGraph` in queries to eliminate `N+1` select issues.
"# ETEC_SpringBoot_Inventory_Management_REST-API" 
