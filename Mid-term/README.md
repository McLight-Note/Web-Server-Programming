# Registration Service

A Spring Boot REST API for user registration.

## Features

- POST endpoint for user registration
- JSON request/response handling
- Input validation
- Health check endpoint

## API Endpoints

### Register User
- **URL**: `POST /api/register`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com"
  }
  ```
- **Response**:
  ```json
  {
    "message": "User 'John Doe' with email 'john.doe@example.com' has been successfully registered!",
    "status": "SUCCESS",
    "userId": "550e8400-e29b-41d4-a716-446655440000"
  }
  ```

### Health Check
- **URL**: `GET /api/health`
- **Response**: `Registration service is running`

## Running the Application

1. Make sure you have Java 17+ installed
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. The application will start on `http://localhost:8080`

## Testing with curl

```bash
# Register a user
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com"
  }'

# Health check
curl http://localhost:8080/api/health
```

## Validation Rules

- **Name**: Required, 2-50 characters
- **Email**: Required, valid email format
