
# Identity Reconciliation REST API

A Spring Boot-based REST API service for identifying and reconciling user identities based on email and phone number combinations.
This system is capable of linking duplicate or related contact records under a unified primary contact.



##  Getting Started

###  Prerequisites

Ensure you have the following installed:

- Java 17  JDK (Java 21 is not supported by Railway.com till now)
- Apache Maven 3.9+
- MySQL 8.0+ (or compatible database)
- (Windows) Git Bash terminal is recommended

---

###  Installation & Running (Windows/Linux/macOS)

Clone the repository and navigate to the project directory:
```sh
git clone https://github.com/yourusername/identity-reconciliation.git
cd identity-reconciliation/
````

Build the project using Maven:

```sh
mvn clean install
```

Run the Spring Boot application:

```sh
mvn spring-boot:run
```

You should see:

```
Started IdentityReconciliationApplication in X.XXX seconds
Listening on port 8080
Database connected: jdbc:mysql://localhost:3306/identity_db
```

---

##  Database Schema

### Table: `CONTACT`

```sql
CREATE TABLE CONTACT (
  id INT AUTO_INCREMENT PRIMARY KEY,
  phone_number VARCHAR(20),
  email VARCHAR(255),
  linked_id INT,
  link_precedence ENUM('PRIMARY', 'SECONDARY') NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  deleted_at DATETIME,
  FOREIGN KEY (linked_id) REFERENCES CONTACT(id)
);
```

---

##  REST API Documentation

###  Endpoint: Identify Contact

**POST** `/identity`
**Production URL:**

```
https://identityreconciliation-production.up.railway.app/identity
```

#### Sample Request

```json
{
  "email": "lorraine@hillvalley.edu",
  "phoneNumber": "123456"
}
```

#### Sample Response (existing contacts found)

```json
{
  "contact": {
    "primaryContactId": 1,
    "emails": ["lorraine@hillvalley.edu", "mcfly@hillvalley.edu"],
    "phoneNumbers": ["123456"],
    "secondaryContactIds": [23]
  }
}
```

#### Sample Response (new contact created)

```json
{
  "contact": {
    "primaryContactId": 45,
    "emails": ["new@example.com"],
    "phoneNumbers": ["9876543210"],
    "secondaryContactIds": []
  }
}
```

---

##  Sample Use Cases

###  Case 1: New Contact Creation

**Request**

```json
{
  "email": "george@hillvalley.edu",
  "phoneNumber": "919191"
}
```

**Response**

```json
{
  "contact": {
    "primaryContactId": 11,
    "emails": ["george@hillvalley.edu"],
    "phoneNumbers": ["919191"],
    "secondaryContactIds": []
  }
}
```

---

###  Case 2: Linking Existing Contacts

**Request**

```json
{
  "email": "george@hillvalley.edu",
  "phoneNumber": "717171"
}
```

**Response**

```json
{
  "contact": {
    "primaryContactId": 11,
    "emails": ["george@hillvalley.edu", "biffsucks@hillvalley.edu"],
    "phoneNumbers": ["919191", "717171"],
    "secondaryContactIds": [27]
  }
}
```

---

##  Configuration

### Database Setup

1. Create the database in MySQL:

```sql
CREATE DATABASE identity_db;
```

2. Configure your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/identity_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Run with JAR

To build and run using the packaged JAR file:

```sh
mvn clean package
java -jar target/identityReconciliation.jar
```

---

##  Deployment

This application is deployed on Railway:

```
Production URL:
https://identityreconciliation-production.up.railway.app
```

### Railway Environment Variables

```
DATABASE_URL=jdbc:mysql://[host]:[port]/[database]
DATABASE_USERNAME=[username]
DATABASE_PASSWORD=[password]
PORT=8080
```

---

## 🧪 Testing

You can test the API using:

* Postman
* cURL

### Example cURL

```sh
curl -X POST \
  https://identityreconciliation-production.up.railway.app/identity \
  -H 'Content-Type: application/json' \
  -d '{"email":"test@example.com","phoneNumber":"1234567890"}'
```







