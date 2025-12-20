# Backend – sCardRecall
This module provides the centralized backend service for the sCardRecall platform.
It handles authentication, data persistence, and recall scheduling using multiple spaced repetition algorithms.

## Responsibilities
- Provide RESTful APIs for all clients
- Handle authentication and authorization using JWT
- Manage flashcards, decks, and review sessions
- Execute recall scheduling logic using pluggable algorithms
- Persist learning progress and review history

## Recall Algorithms
The backend supports multiple spaced repetition algorithms, including:

- Leitner System
- SM-2 (SuperMemo)
- Experimental and future algorithms

Recall algorithms are implemented in an isolated and interchangeable manner.

## Algorithm Design
The recall engine follows a strategy-based design.
Each algorithm implements a common interface and can be extended independently.

## Module Structure
- `controller/`
- `service/`
- `domain/`
- `recall/`
- `repository/`
- `config/`

## Tech Stack
- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose

## Running Locally
The backend service can be run either using Docker Compose (recommended) or directly via Maven.

### Prerequisites
- Java 21
- Docker & Docker Compose
- PostgreSQL (only required if running without Docker)

### Running with Docker Compose (Recommended)
Docker Compose starts the backend service along with required infrastructure services such as the database.

1. Navigate to the project root directory:
   ```bash
   cd sCardRecall
   ```

2. Start the services:
   ```bash
   docker-compose up
   ```

3. The backend API will be available at:
   ```
   http://localhost:8000
   ```

Docker volumes are used to persist database data between restarts.

### Running Without Docker (Local Development)

1. Navigate to the backend module:
   ```bash
   cd backend
   ```

2. Configure the database connection in `application.yml`
   or provide the required environment variables.

3. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The backend API will be available at:
   ```
   http://localhost:8000
   ```

### Environment Configuration
The backend supports configuration via environment variables.

Common variables include:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

When using Docker Compose, these values can be provided via a `.env` file.

### API Documentation
If enabled, Swagger UI can be accessed at:

```
http://localhost:8000/swagger-ui.html
```

---

## Development Status
🚧 This module is under active development.
Core domain models and authentication are implemented.
Recall algorithms and advanced features are being added incrementally.

---

## Notes
- The backend is designed to be client-agnostic.
- All recall logic is handled server-side.