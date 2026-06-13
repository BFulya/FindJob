# FindJob - Job Finding Application

A comprehensive job finding platform built with Spring Boot backend and React frontend.

## Features

### Backend (Spring Boot)
- **Layered Architecture**: Controller, Service, Repository layers following SOLID principles
- **Spring Security**: JWT-based authentication with password encryption
- **Role-Based Access Control**: Admin (Company) and Job Seeker roles
- **Database**: H2 in-memory database with JPA/Hibernate
- **RESTful APIs**: Complete CRUD operations for jobs, users, and job seekers
- **Filtering & Pagination**: Advanced filtering for jobs and job seekers
- **File I/O**: Save job seeker data to files
- **Logging**: AOP-based logging for all operations
- **Swagger UI**: API documentation at `/swagger-ui/index.html`
- **3-Attempt Login Lock**: Account locking after failed login attempts

### Frontend (React)
- **Modern UI**: Bootstrap 5 with custom styling
- **Authentication**: Login and Register pages
- **Admin Dashboard**: 
  - Post and manage job listings
  - Filter job seekers by criteria (military status, experience, salary, education)
  - Save job seeker data to files
- **Job Seeker Dashboard**:
  - View profile information
  - Search and filter jobs
  - Apply for jobs
- **Responsive Design**: Mobile-friendly interface

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (io.jsonwebtoken)
- Lombok
- Swagger/OpenAPI
- Maven

### Frontend
- React 18
- React Router DOM
- Axios
- Bootstrap 5
- React Bootstrap
- Vite

## Project Structure

```
FindJob/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/findjob/
│   │       │   ├── controller/      # REST controllers
│   │       │   ├── service/         # Business logic
│   │       │   ├── repository/      # Data access layer
│   │       │   ├── entity/          # JPA entities
│   │       │   ├── dto/             # Data transfer objects
│   │       │   ├── security/        # Security configuration
│   │       │   ├── config/          # Application configuration
│   │       │   ├── exception/       # Custom exceptions
│   │       │   ├── aspect/          # AOP logging
│   │       │   └── util/            # Utility classes
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/     # React components
    │   ├── pages/         # Page components
    │   ├── context/        # React context
    │   ├── services/      # API services
    │   ├── App.jsx
    │   ├── main.jsx
    │   └── index.css
    ├── public/
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## Database Schema

### Entities
- **User** (Abstract base class)
  - **Admin** (Company users)
  - **JobSeeker** (Job seeking users)
- **Job** (Job postings)

### Relationships
- Admin (1) ↔ Job (N) - One admin can post multiple jobs
- Job (N) ↔ JobSeeker (N) - Many-to-many relationship for job applications

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- Maven

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Run the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## API Documentation

Once the backend is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui/index.html
```

## Usage

### Registration
1. Navigate to `/register`
2. Choose role: Admin (Company) or Job Seeker
3. Fill in the required information
4. Submit the form

### Login
1. Navigate to `/login`
2. Enter username and password
3. After 3 failed attempts, account will be locked
4. Successful login redirects to appropriate dashboard

### Admin Dashboard
- **My Jobs**: View, create, and delete job postings
- **Filter Job Seekers**: Search candidates by military status, experience, salary, education
- **Save to File**: Export job seeker data to text files

### Job Seeker Dashboard
- **My Profile**: View personal information
- **Filter Jobs**: Search jobs by location, department, salary, experience, military status
- **Apply**: Submit job applications

## Design Patterns Used

1. **Singleton Pattern**: JWT Token Provider
2. **Strategy Pattern**: Security Configuration
3. **Repository Pattern**: Data access layer
4. **Factory Pattern**: Entity creation
5. **Template Method Pattern**: Base entity class
6. **Observer Pattern**: Event logging
7. **Facade Pattern**: Service layer

## SOLID Principles Applied

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes are substitutable for base types
- **Interface Segregation**: Small, focused interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

## OOP Concepts Used

- **Inheritance**: User → Admin, JobSeeker
- **Polymorphism**: Method overriding in service implementations
- **Encapsulation**: Private fields with getters/setters
- **Abstraction**: Abstract base classes and interfaces
- **Interfaces**: Service interfaces for contract definition
- **Abstract Classes**: BaseEntity, User
- **Enums**: Role, MilitaryStatus
- **Stream API**: For data processing
- **Optional**: For null-safe operations

## Clean Code Practices

- Meaningful variable and method names
- Small, focused functions
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple, Stupid)
- Proper error handling
- Comprehensive logging
- Code comments where necessary

## Security Features

- Password encryption using BCrypt
- JWT token-based authentication
- Role-based authorization
- CORS configuration
- Input validation
- SQL injection prevention (JPA)
- XSS protection

## Logging

All operations are logged using AOP:
- Service method calls
- Controller API requests
- Error tracking
- Performance monitoring

## File I/O

Job seeker data can be saved to files:
- Location: Configurable via `file.storage.path`
- Format: Text files with structured data
- Triggered by admin from dashboard

## Database Access

H2 Console available at:
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:findjobdb`
- Username: `sa`
- Password: (empty)

## Future Enhancements

- Email notifications
- File upload for resumes
- Advanced search with Elasticsearch
- Real-time chat between companies and candidates
- Interview scheduling
- Rating and review system
- Analytics dashboard

## License

MIT License
