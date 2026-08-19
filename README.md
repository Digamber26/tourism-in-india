# Tourism in India — Web-Based Tourism Management System (Backend)

A full-stack tourism management application backend built with **Java, Spring Boot, REST APIs, JDBC, and MySQL**, following MVC architecture. Manages destinations, travel packages, user enquiries/bookings, and user registration/login end to end.

## Tech Stack
- Java 17
- Spring Boot 3.3 (Web, JDBC, Validation)
- Plain JDBC via `JdbcTemplate` (no ORM) for full control over SQL and CRUD operations
- MySQL
- BCrypt password hashing for secure authentication
- Maven

## Project Structure
```
src/main/java/com/tourism/app
 ├── model/          # Destination, TravelPackage, Enquiry, User
 ├── dao/            # DAO interfaces
 ├── dao/impl/        # JdbcTemplate-based DAO implementations
 ├── controller/      # REST controllers (Destination, TravelPackage, Enquiry, Auth)
 ├── dto/             # Request/response DTOs (ApiResponse, RegisterRequest, LoginRequest, UserResponse)
 └── exception/        # Custom exceptions + global exception handler
src/main/resources
 ├── application.properties
 ├── schema.sql        # Table definitions
 └── data.sql          # Sample seed data
```

## Setup

1. **Create the database** (MySQL must be running locally):
   ```sql
   CREATE DATABASE tourism_db;
   ```

2. **Configure credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   ```

3. **Run the app**:
   ```bash
   mvn spring-boot:run
   ```
   Tables are created automatically from `schema.sql` and seeded with sample data from `data.sql` on startup.

4. The API is now live at `http://localhost:8080`.

## API Endpoints

### Destinations
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/destinations` | List all destinations |
| GET | `/api/destinations/{id}` | Get a destination by id |
| POST | `/api/destinations` | Create a destination |
| PUT | `/api/destinations/{id}` | Update a destination |
| DELETE | `/api/destinations/{id}` | Delete a destination |

### Travel Packages
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/packages` | List all packages (optional `?destinationId=`) |
| GET | `/api/packages/{id}` | Get a package by id |
| POST | `/api/packages` | Create a package |
| PUT | `/api/packages/{id}` | Update a package |
| DELETE | `/api/packages/{id}` | Delete a package |

### Enquiries / Bookings
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/enquiries` | List all enquiries |
| GET | `/api/enquiries/{id}` | Get an enquiry by id |
| POST | `/api/enquiries` | Submit a new enquiry (customer-facing) |
| PATCH | `/api/enquiries/{id}/status?status=CONTACTED` | Update enquiry status (admin-facing) |
| DELETE | `/api/enquiries/{id}` | Delete an enquiry |

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user (password stored as BCrypt hash) |
| POST | `/api/auth/login` | Log in with email + password |

### Example: Create a destination
```bash
curl -X POST http://localhost:8080/api/destinations \
  -H "Content-Type: application/json" \
  -d '{"name":"Kerala Backwaters","state":"Kerala","description":"Houseboats and lush greenery.","category":"Nature"}'
```

### Example: Register a user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Digamber Kalambe","email":"digamber@example.com","password":"secret123"}'
```

## Notes
- All responses are wrapped in a consistent `{ success, message, data }` envelope via `ApiResponse`.
- Validation errors, not-found errors, and duplicate-resource errors are handled centrally in `GlobalExceptionHandler` and return appropriate HTTP status codes (400, 404, 409).
- Passwords are never stored or returned in plain text.
- This backend is designed to pair with an HTML5/CSS3 frontend that consumes these REST endpoints (as described in the project's frontend layer).
