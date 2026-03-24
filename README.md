# Bookstore Management System - REST API (Spring Boot)

A production-style backend REST API for managing books, users, and orders.

## Features
- JWT authentication (`/api/register`, `/api/login`)
- Role-based access control (Customer/Admin) via Spring Security 
- Book management CRUD (`/api/books`)
- Book listing with pagination and title/author search
- Order management (`/api/orders`) with stock updates
- Centralized API error handling with HTTP status codes
- Swagger/OpenAPI docs (`/swagger-ui.html`)
- MySQL-ready persistence with JPA/Hibernate

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Web, Spring Data JPA, Spring Security
- JWT (`jjwt`)
- MySQL (runtime), H2 (tests)
- Springdoc OpenAPI
- JUnit + MockMvc

## Getting Started

### 1) Configure database
Default `src/main/resources/application.yml` is configured for MySQL:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookstore_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
```

Update credentials if needed.

### 2) Run the app
```bash
mvn spring-boot:run
```

### 3) Open API docs
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## API Overview

### Auth
- `POST /api/register` - Register customer/admin
- `POST /api/login` - Login and receive JWT

### Books
- `GET /api/books?page=0&size=10&q=search` - List books (paginated, optional search)
- `GET /api/books/{id}` - Get one book
- `POST /api/books` - Create book (**ADMIN**)
- `PUT /api/books/{id}` - Update book (**ADMIN**)
- `DELETE /api/books/{id}` - Delete book (**ADMIN**)

### Orders
- `GET /api/orders?page=0&size=10` - List orders (**ADMIN**)
- `GET /api/orders/{id}` - Get order (owner or admin)
- `POST /api/orders` - Place order (**CUSTOMER**)
- `PUT /api/orders/{id}/status` - Update order status (**ADMIN**)

## Security
Pass JWT in header:

```http
Authorization: Bearer <token>
```
 
## Testing
```bash
mvn test
```
