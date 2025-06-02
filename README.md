# Wallet App

## Overview

The Wallet Service is a RESTful API built with Spring Boot to manage digital wallets. It supports wallet creation, deposits, withdrawals, balance inquiries (including historical balance), and funds transfer between users. It includes idempotency control and Kafka-based audit logging.

---

## 📦 Technologies Used

- Java 21
- Spring Boot 3.3.1
- Apache Kafka (Confluent)
- Docker & Docker Compose
- Swagger (Springdoc OpenAPI)
- Jakarta Validation

---

## 🛠️ How to Run

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
- Database (web console): `http://localhost:8088/h2-console`
   add JDBC URL: `jdbc:h2:mem:walletdb`

---

## 🧪 Running Tests

Unit and integration tests are executed with:

```bash
./mvnw test
```

---

## ✅ API Endpoints

1. Create Wallet  
   POST /wallet/{userId}  
   Creates a wallet for the given user.

   Path Parameters:
   - userId: UUID of the user

   Response: 201 Created
   Example:
   "5f6e8b2c-d5e6-45a6-b109-9878e4c7f11c"

2. Get Current Balance  
   GET /wallet/{userId}/balance  
   Returns the current balance of the user's wallet.

   Response: 200 OK
   Example:
   {
     "userId": "uuid",
     "balance": 100.00
   }

3. Get Historical Balance  
   GET /wallet/{userId}/historical-balance?at=yyyy-MM-dd HH:mm:ss  
   Returns the wallet balance at a specific past datetime.

   Query Parameters:
   - at: Datetime in format yyyy-MM-dd HH:mm:ss

   Response: 200 OK
   Example:
   {
     "userId": "uuid",
     "balance": 50.00
   }

4. Deposit Funds  
   POST /wallet/{userId}/deposit  
   Deposits a value into the user’s wallet.

   Headers:
   - Idempotency-Key: UUID for duplicate request prevention

   Request Body:
   {
     "amount": 100.00,
     "description": "Test deposit"
   }

   Response: 200 OK

5. Withdraw Funds  
   POST /wallet/{userId}/withdraw  
   Withdraws a value from the user’s wallet.

   Headers:
   - Idempotency-Key: UUID for duplicate request prevention

   Request Body:
   {
     "amount": 50.00,
     "description": "Withdrawal"
   }

   Response: 200 OK

6. Transfer Funds  
   POST /wallet/transfer  
   Transfers funds between two user wallets.

   Headers:
   - Idempotency-Key: UUID for duplicate request prevention

   Request Body:
   {
     "fromUserId": "uuid",
     "toUserId": "uuid",
     "amount": 20.00,
     "description": "Transfer"
   }

   Response: 200 OK

---

## ⚙️ Non-Functional Requirements

- **Idempotency**: Supported via `Idempotency-Key` in headers
- **Kafka Logging**: All transactions are logged asynchronously to Kafka
- **Validation**: Inputs are validated using Jakarta Bean Validation
- **Scalability-ready**: Modular architecture and stateless operations
- **Observability-ready**: Logs key events; easy to plug into observability tools
- **Error Handling**:
  Although the system performs basic exception handling, not all errors are centralized or exposed. Some internal exceptions are intentionally not returned to the client to avoid leaking sensitive information. A centralized error handler (e.g., via @ControllerAdvice) is planned for future improvements, ensuring standardized and secure error responses across the system.
- ---

## 📌 Design Choices

- **Spring Boot**: was chosen for rapid development, robustness, and integration with Kafka and validation frameworks.
- **Kafka**: is used for audit logging to decouple logging from business logic and allow future integration with data analytics.
- **Idempotency Control**: ensures safe retries of operations like deposits or transfers using a custom strategy at the service layer.
- **Docker Compose**: simplifies running the whole stack locally, including Kafka and Kafka UI.
- **One Wallet per User**: The system is designed so that each user can have only one wallet. This decision was made to simplify balance management, ensure transaction integrity, and improve traceability.
The current system architecture assumes a one-to-one relationship between User and Wallet.

---

## ⚠️ Trade-offs

Due to time constraints, the following compromises were made:

- **Security**: No authentication or authorization was implemented, assuming a trusted internal environment.
- **Architecture**: Although the intention was to follow SOLID principles, the full refactoring to adhere strictly to these principles was not completed due to lack of time.
- **Tests**: Only basic tests included; more scenarios and edge cases could be covered.
- **Database**: Replace H2 with PostgreSQL or another production-grade DB.

---

## 📅 Date

June 2025

---

