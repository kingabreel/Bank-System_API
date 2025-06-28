# Bank System API

This is a RESTful API for a basic banking system, built with Spring Boot and secured with JWT authentication. It supports user registration, login, account creation, wallet and card operations, and Pix-based transactions.

## Technologies
- Java 17+
- Spring Boot 3+
- Spring Security (JWT-based)
- JPA
- PostgreSQL
- Maven

## Security Configuration
- Stateless session management (SessionCreationPolicy.STATELESS)
- CORS enabled
- CSRF disabled
- JWT token required for authenticated routes

### Public routes:
- GET /v1/account
- POST /v1/account/**

### Authenticated routes:
- /v1/transaction/**
- /v1/card/**
- /v1/pix/**

## Authentication

### Register
- POST /v1/account/register
  Request body:
  ```json
  {
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@mail.com",
  "password": "your_password",
  "cellphone": "123456789"
  }
  ```

  ### Login
- POST /v1/account/login
  Request body:
  ```json
  {
  "email": "john.doe@mail.com",
  "password": "your_password",
  }
  ```
## Endpoints
- Transactions
  GET /v1/transaction
  Returns the authenticated user's wallet info.
  
  POST /v1/transaction/add?amount=100.00
  Adds money to the user's wallet.
  
  POST /v1/transaction/withdraw?amount=50.00
  Withdraws money from the user's wallet.
  
  POST /v1/transaction/transfer?toUserId=<uuid>&amount=10.00
  Transfers money between users.

- Cards
  POST /v1/card
  Creates a new card.
  
  POST /v1/card/pay
  Request body:
  ```json
  {
    "cardId": "uuid",
    "payment": 100.00,
    "installments": 3,
    "receiverId": "uuid"
  }
  ```

- Pix
  POST /v1/pix
  Register a new Pix key.
  Request body:
  ```json
  {
  "pixKey": "user@email.com",
  "pixType": "EMAIL"
  }
  ```

  POST /v1/pix/transfer
  Transfers money using a Pix key.
  Request body:
  ```json
  {
    "pixKey": "user@email.com",
    "pixType": "EMAIL",
    "amount": 100.00
  }
  ```

## Entities Overview
User: Holds personal info, account, role, and status.
Account: Linked to one user, wallet, agency, and Pix.
Wallet: Holds balance and cards.
Agency: Represents the bank agency.
Pix: Holds a list of embedded Pix keys.
Card: Linked to a wallet, used for payments.

## Running the Project
Configure your application.properties or .yml with database and JWT secret.

Run the application with:
```bash
./mvnw spring-boot:run
```

Use Postman or Curl to test the endpoints.
