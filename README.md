# Wallet App

## Overview

The Wallet Service is a RESTful API built with Spring Boot to manage digital wallets. It supports wallet creation, deposits, withdrawals, balance inquiries (including historical balance), and funds transfer between users. It includes idempotency control and Kafka-based audit logging.

---

##  Technologies Used

- Java 21
- Spring Boot 3.3.1
- Apache Kafka (Confluent)
- Docker & Docker Compose
- Swagger (Springdoc OpenAPI)
- Jakarta Validation

---

##  How to Run

### Prerequisites

- Docker and Docker Compose installed

### Steps

1. Clone the repository:
```bash
git clone https://github.com/martini-rodrigo/wallet
```

2. Start the project using Docker Compose:
```bash
docker-compose up --build
```

3. Access the API documentation:
- Swagger UI: `http://localhost:8088/swagger-ui.html`
- Kafka UI: `http://localhost:9090`
- Database: `http://localhost:8088/h2-console`

---

##  Running Tests

Unit and integration tests are executed with:

```bash
./mvnw test
```


---

##  Non-Functional Requirements

- **Idempotency**: Supported via `Idempotency-Key` in headers
- **Kafka Logging**: All transactions are logged asynchronously to Kafka
- **Validation**: Inputs are validated using Jakarta Bean Validation
- **Scalability-ready**: Modular architecture and stateless operations
- **Observability-ready**: Logs key events; easy to plug into observability tools

---

##  Design Choices

- **Spring Boot** was chosen for rapid development, robustness, and integration with Kafka and validation frameworks.
- **Kafka** is used for audit logging to decouple logging from business logic and allow future integration with data analytics.
- **Idempotency Control** ensures safe retries of operations like deposits or transfers using a custom strategy at the service layer.
- **Docker Compose** simplifies running the whole stack locally, including Kafka and Kafka UI.

---

##  Trade-offs

Due to time constraints, the following compromises were made:

- **Security**: No authentication or authorization was implemented, assuming a trusted internal environment.
- **Architecture**: Although the intention was to follow SOLID principles, the full refactoring to adhere strictly to these principles was not completed due to lack of time.
- **Tests**: Only basic tests included; more scenarios and edge cases could be covered.
---

