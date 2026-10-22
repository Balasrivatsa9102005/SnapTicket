# 🚌 Snap Ticket - Bus Booking System

A full-stack Bus Booking System built with **Spring Boot, React, MySQL, and JWT authentication**. The application allows users to browse and book bus seats while administrators manage buses, routes, and trips through role-based access control.

## ✨ Features

### 🔐 Authentication & Authorization
- User registration and login.
- JWT-based authentication for secured API requests.
- Role-based access control for `USER` and `ADMIN`.
- Password encryption before storing credentials.
- Users can access and manage their own profiles and bookings.
- Admin-only access to bus, route, and trip management operations.

### 🚌 Bus Management
- Create, view, update, and delete buses.
- Store bus number, operator name, bus type, and seat capacity.
- Automatically generate seats when a bus is created.
- Support different bus types, including seater and sleeper configurations.

### 🛣️ Route Management
- Create and manage bus routes.
- Store source, destination, and route distance.
- Validate route details and prevent duplicate routes.

### 📅 Trip Management
- Schedule trips using buses and routes.
- Store travel date, departure time, and arrival time.
- Validate trip schedules.
- Prevent overlapping trips for the same bus on the same date.

### 🎟️ Booking Management
- Book one or multiple seats for a trip.
- Validate seat IDs and ensure seats belong to the selected bus.
- Prevent duplicate bookings of the same seat for the same trip.
- View booking details and retrieve a user's bookings.
- Cancel existing bookings.
- Enforce booking ownership so users cannot access or cancel another user's bookings.

### 💻 Frontend
- Interactive React-based user interface.
- Connects to the Spring Boot REST APIs.
- Supports authenticated API requests using JWT.
- Provides user-facing booking workflows and administrative management screens.

## 🛠️ Tech Stack

| Layer | Technologies |
|---|---|
| Frontend | React, JavaScript, HTML, CSS |
| Backend | Java, Spring Boot |
| API | RESTful APIs, JSON |
| Security | Spring Security, JWT |
| Database | MySQL |
| ORM | Spring Data JPA, Hibernate |
| Validation | Jakarta Bean Validation |
| Testing | Postman |
| Development Tools | Eclipse, MySQL Workbench, VS Code |

## 🏗️ Architecture

```text
React Frontend
      |
      | HTTP / JSON + JWT
      v
Spring Boot REST API
      |
      +---- Controllers
      |
      +---- Services
      |       |
      |       +---- Business Logic
      |       +---- Validation
      |       +---- Authorization Checks
      |
      +---- Spring Data JPA Repositories
                      |
                      v
                  MySQL Database
```

## 📂 Project Structure

The project is organized into a backend and frontend.

```text
Bus-Booking-System/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/busbooking/
│   │       │   ├── controllers/
│   │       │   ├── services/
│   │       │   ├── repositories/
│   │       │   ├── entities/
│   │       │   ├── dto/
│   │       │   ├── security/
│   │       │   └── exceptions/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
│
└── README.md
```


## ⚙️ Prerequisites

Install the following before running the application:

- Java Development Kit (JDK)
- Maven, or use the Maven Wrapper included with the backend
- Node.js and npm
- MySQL Server
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd Bus-Booking-System
```

Replace `<YOUR_REPOSITORY_URL>` with your Git repository URL.

### 2. Configure the Database

Create a MySQL database:

```sql
CREATE DATABASE bus_booking;
```

Update `application.properties` in the backend:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bus_booking
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Set the `DB_USERNAME` and `DB_PASSWORD` environment variables to your local MySQL credentials.

Configure your JWT signing key using the property or environment variable expected by your existing JWT implementation. Use a secure secret and never commit credentials or signing keys to Git.

> **Note:** Keep your database and JWT configuration consistent with the properties already used by your application.

### 3. Run the Backend

Navigate to the backend directory:

```bash
cd backend
```

Run the application using Maven:

```bash
mvn spring-boot:run
```

The backend will start on the configured Spring Boot port, typically:

```text
http://localhost:8080
```

### 4. Run the Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite in the terminal, typically:

```text
http://localhost:5173
```

Ensure the frontend API base URL points to the running backend. If the frontend and backend use different origins, configure CORS in Spring Security for the frontend development URL.

## 🔑 Authentication Flow

1. A new user registers through the registration endpoint.
2. The user logs in with their email and password.
3. The backend validates the credentials and issues a JWT.
4. The frontend includes the token in requests to protected endpoints:

```http
Authorization: Bearer <JWT_TOKEN>
```

5. Spring Security validates the token and applies the application's authorization rules.
6. The backend checks resource ownership for user-specific operations.

Admin accounts should be provisioned securely. Public registration should not allow a client to assign itself the `ADMIN` role.



## 🔒 Security & Validation

- JWT-based stateless authentication.
- Role-based authorization for administrative operations.
- Password hashing through Spring Security's password encoder.
- Input validation for incoming request DTOs.
- Duplicate email and bus number checks.
- Seat-to-bus and booking-to-user validation.
- Duplicate seat-booking prevention.
- Ownership checks for booking and profile operations.
- Server-side enforcement of authorization rules.

## 🔮 Future Enhancements

- Online payment integration.
- Email and SMS booking confirmations.
- Booking history and downloadable tickets.
- Seat maps with interactive seat selection.
- Search and filtering by date, route, and bus type.
- Fare calculation based on route distance and bus type.
- Support for overnight trips.
- Automated unit and integration tests.
- Deployment with Docker and a cloud-hosted database.

## 👨‍💻 Author

**Panigrahi Bala Srivatsa**

B.Tech — Computer Science and Engineering (AI & ML)

VIT-AP University

---

If you found this project useful, consider giving the repository a ⭐.
