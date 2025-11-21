# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Tombola is a Spring Boot 4.0 web application for managing raffles/lotteries. It allows users to create tombolas and manage prizes with unique numbers. The application uses PostgreSQL for production and H2 for testing, with Thymeleaf templates for the UI and Spring Security for authentication.

## Build and Development Commands

### Build and Test
- **Build project**: `./mvnw clean install`
- **Run tests**: `./mvnw test`
- **Run tests with coverage**: `./mvnw clean verify -Pcoverage`
- **Run single test class**: `./mvnw test -Dtest=ClassName`
- **Run single test method**: `./mvnw test -Dtest=ClassName#methodName`

### Code Quality
- **Format check**: `./mvnw spring-javaformat:validate`
- **Auto-format code**: `./mvnw spring-javaformat:apply`

### Run Application
- **Start application**: `./mvnw spring-boot:run`
- Application runs on port **8085**
- Default login: username=`user`, password=`tombola`

### Database Setup
PostgreSQL is required for production. Use Docker to run it:
```bash
docker run --name tombola-postgres -p5435:5432 -e POSTGRES_DB=tombola -e POSTGRES_USER=tombola -e POSTGRES_PASSWORD=tombola -d postgres
```

Tests use H2 in-memory database (no setup required).

## Architecture

### Package Structure
The codebase follows a layered architecture with clear separation of concerns:

- **`boundary`**: Web layer - Spring MVC controllers and view models that handle HTTP requests/responses
- **`control`**: Repository layer - Spring Data JPA repositories for data access
- **`entity`**: Domain model - JPA entities with validation and auditing
- **`configuration`**: Spring configuration classes for security and MVC setup

### Core Domain Model

**Tombola-Prize Relationship**:
- One Tombola has many Prizes (one-to-many)
- Each Prize has a unique number within its Tombola (enforced by `UX_PRIZE` constraint on `number` + `tombola_id`)
- Both entities use JPA auditing (`@CreatedDate`, `@LastModifiedDate`)

### Session Management
The application uses HTTP sessions to track the currently selected Tombola:
- When a user selects a Tombola via `/tombolas/{id}/select`, it's stored in the session
- The PrizesController reads this session attribute to show/filter prizes for the current Tombola
- If no Tombola is in session, users are redirected to `/tombolas`

### Security Configuration
- Form-based authentication with in-memory user store (development setup)
- Public access: `/css/**`, `/images/**`, `/actuator/health`
- Requires `ROLE_USER` for all other endpoints
- Login page: `/login`

### Testing Approach
- Controller tests extend `ControllerTest` base class
- Uses MockMvc with HtmlUnit WebClient for integration testing
- Tests use `@SpringBootTest` and `@AutoConfigureMockMvc`
- Default locale set to German for tests

## Important Notes

### Code Formatting
This project uses Spring Java Format. All code must pass `spring-javaformat:validate` before commits. Run `./mvnw spring-javaformat:apply` to auto-format if validation fails.

### JPA Configuration
- `spring.jpa.hibernate.ddl-auto=update` - schema auto-updated from entities
- `spring.sql.init.mode=always` - data.sql runs on startup
- `spring.jpa.defer-datasource-initialization=true` - SQL runs after Hibernate schema creation

### Sonar Exclusions
`TombolaApplication.java` is excluded from SonarCloud analysis (see pom.xml).