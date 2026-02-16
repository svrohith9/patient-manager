# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2024-01-15

### Added
- Complete CRUD operations for patient management
- UUID-based patient identification
- Email uniqueness validation
- Comprehensive input validation (Jakarta Bean Validation)
- Global exception handling
- Pagination support for patient list endpoint
- API versioning (v1)
- gRPC inter-service communication (Patient → Billing)
- Event-driven architecture with Apache Kafka
- API Gateway with routing
- OpenAPI 3.0 / Swagger documentation
- Spring Boot Actuator (health, metrics, info)
- Circuit breaker pattern (Resilience4j)
- Retry logic for external service calls
- Docker & Docker Compose support
- Multi-stage Docker builds
- Non-root container execution
- Health checks in containers

### Improved
- Project structure following best practices
- Comprehensive error responses
- Logging throughout the application
- Database indexing for better performance
- Extended patient model with additional fields
- Enhanced API documentation

### Security
- Non-root user in Docker containers
- Security headers in API Gateway
- Input validation and sanitization

## [0.0.1] - 2023-01-01

### Initial Release
- Basic CRUD operations
- PostgreSQL integration
- Simple REST API
