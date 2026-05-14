# MediHelp - Backend

MediHelp is a comprehensive medical appointment management system designed to streamline healthcare services. This backend application is built with Spring Boot, providing RESTful APIs for the frontend application and managing the core business logic.

## Project Review

Watch the project review video: [MediHelp Project Review](https://youtu.be/wZAdwhOXHgg)

## Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Appointment Management**: CRUD operations for appointments with scheduling and status tracking
- **Doctor Management**: Admin functionality to manage doctor profiles and availability
- **Payment Processing**: Integration for handling appointment payments
- **Admin Panel**: Administrative controls for system management
- **Security**: Spring Security implementation with custom filters
- **Database Integration**: JPA/Hibernate for data persistence

## Tech Stack

- **Framework**: Spring Boot 3.4.1
- **Java Version**: 17
- **Database**: JPA/Hibernate (configurable)
- **Security**: Spring Security
- **Build Tool**: Maven
- **API**: RESTful Web Services

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL/PostgreSQL (or your preferred database)

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd medihelp-backend
   ```

2. Configure the database:
   - Update `src/main/resources/application.properties` with your database configuration
   - Create the database if it doesn't exist

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/lk/ijse/medihelpbackend/
│   │   ├── MedihelpBackendApplication.java    # Main application class
│   │   ├── advicer/                          # Global exception handlers
│   │   ├── config/                           # Configuration classes
│   │   │   ├── JwtFilter.java                # JWT authentication filter
│   │   │   ├── WebSecurityConfig.java        # Security configuration
│   │   │   └── ...
│   │   ├── controller/                       # REST controllers
│   │   │   ├── AdminController.java          # Admin operations
│   │   │   ├── AppointmentController.java    # Appointment management
│   │   │   ├── AuthController.java           # Authentication
│   │   │   ├── DoctorController.java         # Doctor management
│   │   │   ├── PaymentController.java        # Payment processing
│   │   │   └── UserController.java           # User management
│   │   ├── dto/                              # Data Transfer Objects
│   │   ├── entity/                           # JPA Entities
│   │   ├── repo/                             # Repository interfaces
│   │   ├── service/                          # Business logic services
│   │   └── util/                             # Utility classes
│   └── resources/
│       └── application.properties            # Application configuration
└── test/                                     # Test classes
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Appointments
- `GET /api/appointments` - Get all appointments
- `POST /api/appointments` - Create new appointment
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment

### Doctors
- `GET /api/doctors` - Get all doctors
- `POST /api/doctors` - Add new doctor
- `PUT /api/doctors/{id}` - Update doctor
- `DELETE /api/doctors/{id}` - Delete doctor

### Admin
- `GET /api/admin/users` - Get all users
- `POST /api/admin/doctors` - Manage doctors

### Payment
- `POST /api/payment/process` - Process payment

## Configuration

Update `application.properties` with your database and other configurations:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/medihelp
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

## Running Tests

```bash
./mvnw test
```

## Building for Production

```bash
./mvnw clean package
```

The JAR file will be created in the `target/` directory.

