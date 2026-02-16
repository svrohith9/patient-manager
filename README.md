# Patient Manager API

[![Java Version](https://img.shields.io/badge/Java-21-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)
[![API Docs](https://img.shields.io/badge/API-Docs-OpenAPI-blue)](http://localhost:8080/swagger-ui.html)

A production-grade microservice architecture for managing patient records with comprehensive features including REST APIs, gRPC communication, event-driven architecture, and cloud-native deployment capabilities.

## 🏗️ Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   API Gateway   │────▶│  Patient Service │────▶│  Billing Service│
│    (:4000)      │     │    (:8080)        │────▶│    (:9001)       │
└─────────────────┘     └────────┬─────────┘     └─────────────────┘
                                 │
                                 ▼
                        ┌──────────────────┐
                        │   Kafka          │
                        │  (Event Bus)     │
                        └────────┬─────────┘
                                 │
                                 ▼
                        ┌──────────────────┐
                        │ Analytics Service│
                        │   (:8082)        │
                        └──────────────────┘
```

## ✨ Features

### Core Features
- ✅ Complete CRUD operations for patient management
- ✅ UUID-based unique identification
- ✅ Email uniqueness validation with conflict handling
- ✅ Comprehensive input validation (Jakarta Bean Validation)
- ✅ Global exception handling with standardized error responses
- ✅ Pagination support for list endpoints
- ✅ API versioning (v1)

### Microservices Features
- ✅ gRPC inter-service communication (Patient → Billing)
- ✅ Event-driven architecture with Apache Kafka
- ✅ Service discovery ready (configurable)
- ✅ API Gateway with routing

### Production Features
- ✅ OpenAPI 3.0 / Swagger documentation
- ✅ Spring Boot Actuator (health, metrics, info)
- ✅ Circuit breaker pattern (Resilience4j)
- ✅ Request/response logging
- ✅ Docker & Docker Compose support
- ✅ Kubernetes manifests ready
- ✅ CI/CD pipeline configuration

## 🛠️ Tech Stack

### Core
- **Java** 21
- **Spring Boot** 3.4.4
- **Spring Data JPA** with PostgreSQL
- **Maven** 3.9+

### Communication
- **gRPC** for synchronous inter-service calls
- **Apache Kafka** for asynchronous event streaming

### Infrastructure
- **PostgreSQL** 17 for data persistence
- **Docker** & **Docker Compose** for containerization
- **Kubernetes** manifests included

### Observability
- **Spring Boot Actuator** for health checks and metrics
- **OpenAPI 3.0** for API documentation
- **Resilience4j** for circuit breaker pattern

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose (for containerized setup)
- PostgreSQL 17+ (for local development)

### Local Development Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/svrohith9/patient-manager.git
cd patient-manager
```

#### 2. Database Configuration

The application uses PostgreSQL. Configure via environment variables or `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/patientdb
    username: postgres
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

#### 3. Run with Maven

```bash
# Build all services
mvn clean package -DskipTests

# Run Patient Service
cd patient-service
mvn spring-boot:run

# Run in separate terminals:
# Billing Service (cd billing-service && mvn spring-boot:run)
# Analytics Service (cd analytics-service && mvn spring-boot:run)
# API Gateway (cd api-gateway && mvn spring-boot:run)
```

#### 4. Run with Docker Compose

```bash
# From project root
docker-compose -f docker-compose.yaml up -d
```

This starts:
- PostgreSQL database
- Zookeeper & Kafka
- Patient Service (:8080)
- Billing Service (:9001 gRPC, :4001 HTTP)
- API Gateway (:4000)

## 📚 API Documentation

Once running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 📖 API Endpoints

### Patient Service

| Method | Endpoint | Description | Pagination |
|--------|----------|-------------|------------|
| GET | `/api/v1/patients` | List all patients | ✅ Yes |
| GET | `/api/v1/patients/{uuid}` | Get patient by ID | - |
| POST | `/api/v1/patients` | Create new patient | - |
| PUT | `/api/v1/patients/{uuid}` | Update patient | - |
| DELETE | `/api/v1/patients/{uuid}` | Delete patient | - |

### Query Parameters for List Endpoint

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | int | 0 | Page number (0-indexed) |
| `size` | int | 20 | Page size |
| `sort` | String | id,asc | Sort field,direction |

### API Gateway Routes

| Service | Gateway Endpoint |
|---------|-------------------|
| Patient Service | http://localhost:4000/patients/v1/** |

### Actuator Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health check |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Metrics |
| `/actuator/prometheus` | Prometheus format |

## 📝 Request/Response Examples

### Create Patient

**POST** `/api/v1/patients`

```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "phoneNumber": "+1-555-123-4567",
  "birthDate": "1990-01-15",
  "registeredDate": "2024-01-15",
  "gender": "FEMALE",
  "address": "456 Elm Street, Springfield, IL 62701"
}
```

**Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "phoneNumber": "+1-555-123-4567",
  "birthDate": "1990-01-15",
  "registeredDate": "2024-01-15",
  "gender": "FEMALE",
  "address": "456 Elm Street, Springfield, IL 62701"
}
```

### List Patients (Paginated)

**GET** `/api/v1/patients?page=0&size=10&sort=lastName,asc`

**Response** (200 OK):
```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "first": true,
  "last": false
}
```

### Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "A patient with email john@example.com already exists",
  "path": "/api/v1/patients"
}
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring profile | dev |
| `SERVER_PORT` | Server port | 8080 |
| `SPRING_DATASOURCE_URL` | Database URL | jdbc:postgresql://localhost:5432/postgres |
| `SPRING_DATASOURCE_USERNAME` | DB username | postgres |
| `SPRING_DATASOURCE_PASSWORD` | DB password | postgres |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | localhost:9092 |
| `BILLING_SERVICE_URL` | Billing gRPC URL | localhost:9001 |

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

## 📦 Project Structure

```
patient-manager/
├── patient-service/           # Main patient CRUD service
│   ├── src/main/java/...
│   ├── src/main/proto/...    # Protocol Buffer definitions
│   ├── src/main/resources/   # Configuration
│   ├── Dockerfile
│   └── docker-compose.yaml
├── billing-service/          # gRPC billing service
│   ├── src/main/java/...
│   ├── src/main/proto/...
│   └── Dockerfile
├── analytics-service/         # Kafka consumer for analytics
│   ├── src/main/java/...
│   ├── src/main/proto/...
│   └── Dockerfile
├── api-gateway/               # Spring Cloud Gateway
│   └── src/main/...
├── k8s/                       # Kubernetes manifests
├── docker-compose.yaml        # Full stack compose
├── CHANGELOG.md
├── CONTRIBUTING.md
└── README.md
```

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- gRPC team for efficient inter-service communication
- Apache Kafka community for event streaming

---

🩺 Built by [Rohith](https://github.com/svrohith9) with ❤️
