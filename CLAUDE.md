# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**whatsapp-automation-backend** - A Spring Boot 4.0.5 backend service for WhatsApp automation, built with Java 17.

### Technology Stack
- Spring Boot 4.0.5 (WebMVC, Actuator, Kafka)
- Java 17 (via Gradle toolchain)
- Gradle 9.4.0 (wrapper included)
- JUnit Jupiter (JUnit 5)
- Lombok
- Kafka integration for messaging

### Architecture
Standard Spring Boot application with package structure under `com.dariodussin.whatsappautomationbackend`. The project currently contains only the application bootstrap class, indicating it's in early development stages. Expected future structure:
- `controller/` - REST API endpoints
- `service/` - Business logic
- `domain/` or `model/` - Entity/model classes
- `repository/` - Data access layer
- `config/` - Configuration classes
- `dto/` - Data transfer objects

## Common Development Tasks

### Build & Run
```bash
# Build the project (includes tests)
./gradlew build

# Build without tests
./gradlew assemble

# Build executable jar
./gradlew bootJar

# Run the application
./gradlew bootRun
```

### Testing
```bash
# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.dariodussin.whatsappautomationbackend.WhatsappAutomationBackendApplicationTests"

# Run a single test method
./gradlew test --tests "com.dariodussin.whatsappautomationbackend.WhatsappAutomationBackendApplicationTests.contextLoads"

# Run tests with verbose output
./gradlew test --info

# Run tests without build (faster)
./gradlew test --no-build-cache
```

### Code Quality & Inspection
```bash
# Generate Javadoc
./gradlew javadoc

# Clean build artifacts
./gradlew clean
```

### Dependencies
```bash
# List all dependencies
./gradlew dependencies

# Get insight into a specific dependency
./gradlew dependencyInsight --dependency spring-boot-starter
```

## Project Conventions

### Package Structure
All source code under `src/main/java/com/dariodussin/whatsappautomationbackend/`
Tests under `src/test/java/com/dariodussin/whatsappautomationbackend/`

### Java Version
Java 17 is enforced via Gradle toolchain. The project will automatically use Java 17 even if a different JDK is installed.

### Testing
- Use JUnit Jupiter (JUnit 5) annotations (`@Test`, `@SpringBootTest`, etc.)
- Integration tests should use `@SpringBootTest`
- Controllers can use `@WebMvcTest` for slice tests
- Test classes typically end with `Tests` suffix

### Configuration
- Application properties: `src/main/resources/application.properties`
- Spring Boot Actuator is available for monitoring
- Kafka is configured on the classpath; configure brokers in `application.properties`

### IDE Setup
- Import as Gradle project
- Enable annotation processing for Lombok
- Use Java 17 SDK

## Notes
- Project is in early stages (first commit) - only application bootstrap exists
- Kafka test starter is included for testing messaging components
- No custom Gradle tasks beyond Spring Boot defaults
