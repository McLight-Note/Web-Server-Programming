# BookSwap Marketplace - Presentation Guide

## Page 1: Project Overview & Architecture

### Key Points to Highlight:
- **Technology Stack**: Spring Boot 3.2.0, Java 17, JWT, H2 Database, Swagger
- **Architecture**: RESTful API with layered architecture
- **Main Features**: Book swapping marketplace with authentication, search, and transaction management

### Show:
- Project structure (Maven)
- `pom.xml` - dependencies (JWT, JPA, Security, HATEOAS, Swagger)
- Package structure: model, repository, service, controller, dto, security, config

---

## Page 2: Data Models & Relationships

### Key Points:
- Three main entities: User, Book, SwapRequest
- Enum types for status management
- Soft delete pattern (deleted flag)
- JPA relationships (ManyToOne, OneToMany)

### Show Files:
- `model/User.java` - User entity with role enum
- `model/Book.java` - Book entity with BookStatus enum
- `model/SwapRequest.java` - SwapRequest with SwapRequestStatus enum
- `model/BookStatus.java` & `model/SwapRequestStatus.java` - Enums

### Highlight:
- Soft delete implementation (`deleted` boolean)
- Status enums (AVAILABLE, RESERVED, SWAPPED / PENDING, ACCEPTED, REJECTED)
- Relationships between entities

---

## Page 3: Security & JWT Authentication

### Key Points:
- JWT-based stateless authentication
- Role-based access control (USER/ADMIN)
- Password encryption with BCrypt
- Security filter chain configuration

### Show Files:
- `security/SecurityConfig.java` - Security configuration
- `security/JwtUtil.java` - JWT token generation and validation
- `security/JwtAuthenticationFilter.java` - Filter for JWT validation
- `security/UserDetailsServiceImpl.java` - User details service

### Highlight:
- Security filter chain setup
- JWT token generation/validation logic
- Password encoding
- Protected vs public endpoints

---

## Page 4: Repository Layer & Custom Queries

### Key Points:
- Spring Data JPA repositories
- Custom query methods for search
- Partial search implementation (LIKE queries)
- Soft delete filtering

### Show Files:
- `repository/BookRepository.java` - Custom search query
- `repository/UserRepository.java` - User lookup methods
- `repository/SwapRequestRepository.java` - Swap request queries

### Highlight:
```java
@Query("SELECT b FROM Book b WHERE b.deleted = false " +
       "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
       "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
       "AND (:status IS NULL OR b.status = :status) " +
       "AND (:ownerId IS NULL OR b.owner.id = :ownerId)")
List<Book> searchBooks(...);
```

---

## Page 5: Service Layer & Business Logic

### Key Points:
- Transaction management (@Transactional)
- Business rule validation
- Status transition validation
- ISBN validation

### Show Files:
- `service/BookService.java` - Book management logic
- `service/SwapRequestService.java` - Swap request handling with transactions
- `service/AuthService.java` - Authentication logic

### Highlight:
- Transactional swap acceptance (rejects other pending requests)
- Status transition rules
- ISBN duplicate checking
- Owner authorization checks

---

## Page 6: REST Controllers & API Endpoints

### Key Points:
- RESTful API design
- HATEOAS implementation
- Request validation
- Swagger annotations

### Show Files:
- `controller/BookController.java` - Book endpoints
- `controller/SwapRequestController.java` - Swap endpoints
- `controller/AuthController.java` - Auth endpoints
- `controller/UserController.java` - User endpoints

### Highlight:
- All 7 required endpoints:
  - `GET /api/books` - Search with filters
  - `POST /api/books` - Create book
  - `PATCH /api/books/{id}` - Update status
  - `POST /api/swaps` - Create swap request
  - `PATCH /api/swaps/{id}/accept` - Accept swap
  - `PATCH /api/swaps/{id}/reject` - Reject swap
  - `GET /api/users/{id}/books` - Get owner's books

---

## Page 7: HATEOAS Implementation

### Key Points:
- Hypermedia links in responses
- Self, owner, swap, book links
- RESTful navigation

### Show Code:
```java
private void addHateoasLinks(BookResponse book) {
    Link selfLink = WebMvcLinkBuilder.linkTo(BookController.class)
            .slash(book.getId())
            .withSelfRel();
    book.add(selfLink);
    
    Link ownerLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(UserController.class)
                    .getUserBooks(book.getOwnerId()))
            .withRel("owner");
    book.add(ownerLink);
}
```

### Highlight:
- HATEOAS links in BookResponse and SwapRequestResponse
- How clients can navigate the API using links

---

## Page 8: Validation & DTOs

### Key Points:
- Request/Response DTOs
- Bean validation annotations
- ISBN format validation
- Input sanitization

### Show Files:
- `dto/BookRequest.java` - ISBN validation pattern
- `dto/AuthRequest.java` - Email validation
- All DTO classes

### Highlight:
```java
@Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
         message = "Invalid ISBN format")
private String isbn;
```

---

## Page 9: Testing & Documentation

### Key Points:
- Repository tests with TestEntityManager
- Controller tests with MockMvc
- Swagger/OpenAPI documentation
- Test coverage

### Show Files:
- `test/repository/BookRepositoryTest.java` - Repository tests
- `test/controller/BookControllerTest.java` - Controller tests
- `config/OpenApiConfig.java` - Swagger configuration

### Highlight:
- Test cases for search functionality
- Test cases for authentication
- Swagger UI access at `/swagger-ui.html`
- How to run tests: `mvn test`

---

## Bonus: Configuration & Application Properties

### Show:
- `application.properties` - Database, JWT, logging configuration
- Environment variables support
- H2 console configuration

---

## Quick Demo Flow:

1. **Start Application**: `mvn spring-boot:run`
2. **Show Swagger UI**: http://localhost:8080/swagger-ui.html
3. **Register User**: POST /api/auth/register
4. **Login**: POST /api/auth/login (get JWT token)
5. **Create Book**: POST /api/books (with JWT)
6. **Search Books**: GET /api/books?title=Gatsby
7. **Create Swap Request**: POST /api/swaps
8. **Accept Swap**: PATCH /api/swaps/{id}/accept

---

## Key Technical Highlights to Mention:

1. ✅ **Soft Delete Pattern** - All entities use `deleted` flag
2. ✅ **JWT Authentication** - Stateless, secure token-based auth
3. ✅ **Transactional Operations** - Swap acceptance is atomic
4. ✅ **Partial Search** - LIKE queries for title/author
5. ✅ **Status Management** - Validated state transitions
6. ✅ **HATEOAS** - Hypermedia links for API navigation
7. ✅ **Validation** - ISBN format, email, required fields
8. ✅ **Role-Based Access** - USER/ADMIN roles
9. ✅ **API Documentation** - Swagger/OpenAPI integration
10. ✅ **Testing** - Repository and controller tests

