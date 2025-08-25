# 🚗 CarAutoRox Demo Project

This project is a **Spring Boot–based API platform** that supports:

* **Versioned REST APIs** (V1 & V2)
* **MCP protocol tools** (CarToolV1, CarToolV2, JwtAuthManager)

It demonstrates **dynamic car data management**, **API versioning**, and **authentication via API keys and JWT** — all in memory, no database required.

---

## ✨ Features

* ✅ **REST APIs with Swagger UI**
* ✅ **MCP tool protocol support**
* ✅ **Versioned APIs (v1 & v2)** with independent data models
* ✅ **In-memory data store** (TemporaryCarDatabase)
* ✅ **JWT & API key authentication** with interceptor validation
* ✅ **Profile-based execution** → choose REST mode, MCP mode, or both

---

## ⚙️ Tech Stack

* Java 17+
* Spring Boot 3.x
* Maven
* Swagger (springdoc-openapi)
* JWT (io.jsonwebtoken)

---

## 🚀 Getting Started

### 1. Build

```bash
mvn clean package -DskipTests
```

### 2. Run (REST APIs)

```bash
java -jar target/demo-*.jar
# or
mvn spring-boot:run
```

### 3. Swagger UI

Once the app is running:
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔑 Authentication

You can access APIs using either:

* **JWT Token**

  ```
  Authorization: Bearer <JWT_TOKEN>
  ```
* **API Key**

  ```
  X-API-KEY: USER-ID:SECURITY-CODE
  ```

---

## 📖 REST API Flow

### **Version 1 (V1)**

* `GET /v1/info` → API version status
* `GET /v1/cars/all` → All cars (id, name, odometer)
* `GET /v1/cars/info/{id}` → Fetch car by ID
* `POST /v1/cars/add` → Add new car (strict validation)
* `PUT /v1/cars/update/{id}` → Update only carOdometer

### **Version 2 (V2)**

* `GET /v2/initial` → Initially loaded cars
* `POST /v2/add` → Add new car (full model with RC & chassis)
* `GET /v2/new` → Only newly added cars
* `GET /v2/all` → All cars (initial + new)

---

## 🔌 MCP Protocol Flow

This project can also act as an **MCP server** exposing tool handlers:

* `CarToolV1` → Interacts with V1 car service
* `CarToolV2` → Interacts with V2 car service
* `JwtAuthManager` → Validates JWT & auth flows

### Run in MCP Mode

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mcp
```

### Configure Client

Point your MCP client to start this server:

```json
{
  "name": "CarDemo",
  "command": "java",
  "args": ["-jar", "target/demo-<version>.jar", "--spring.profiles.active=mcp"]
}
```

This lets the client discover and call your tools directly over stdio JSON-RPC.

---

## 📂 Project Structure

```
src/main/java/com/carautorox/demo/
├── version1/        # V1 API models, DTOs, controllers, services
├── version2/        # V2 API models, DTOs, controllers, services
├── authentication/  # JWT utilities & interceptors
├── config/          # Swagger, Jackson, token store config
├── databasestorage/ # In-memory TemporaryCarDatabase
└── mcp/             # MCP tool handlers (CarToolV1, CarToolV2, JwtAuthManager)
```

---

## ✅ Next Steps

* Run APIs with REST mode → Test using Swagger or curl.
* Switch to MCP profile → Use tools in MCP clients (like ChatGPT, Claude, or custom).
* Extend with **V3 APIs** reusing the same data store and tool structure.

---

👉 This README covers **both REST API usage and MCP protocol support** so developers can pick the mode that best fits their workflow.


