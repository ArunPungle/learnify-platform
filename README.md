<div align="center">


# 🎓 Learnify Platform

### Online Learning Platform — Microservices Architecture

> A full-scale, cloud-native e-learning platform inspired by **Udemy** and **Coursera**, built with **12 Spring Boot 3.x microservices**. Demonstrates distributed systems design, event-driven architecture, and modern DevOps practices.

<br/>

<img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring_Cloud-2023.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Apache_Kafka-3.x-231F20?style=for-the-badge&logo=apachekafka&logoColor=white"/>
<img src="https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
<img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"/>

<br/><br/>

</div>

---

## 📌 Repository Description

> **Learnify** is a microservices-based online learning platform built using **Java 17**, **Spring Boot 3.2**, and **Spring Cloud**. The system features 12 independent services communicating via **OpenFeign** (sync) and **Apache Kafka** (async), with full **JWT authentication**, **Redis caching**, **Flyway migrations**, **Resilience4j circuit breakers**, **distributed tracing via Zipkin**, and **Docker Compose** orchestration.

---

## 📦 Microservices

| # | Service | Port | Responsibility | Key Tech |
|---|---------|------|----------------|----------|
| 1 | **API Gateway** | 8080 | Single entry point, JWT filter, rate limiting, routing | Spring Cloud Gateway, Redis |
| 2 | **Config Server** | 8888 | Centralized configuration management | Spring Cloud Config |
| 3 | **Service Registry** | 8761 | Service discovery & registration | Netflix Eureka |
| 4 | **Auth Service** | 8081 | Login, signup, JWT issuance, refresh tokens, RBAC | Spring Security, JJWT |
| 5 | **User Service** | 8082 | User profiles, instructor management, Redis caching | JPA, Redis |
| 6 | **Course Service** | 8083 | Course CRUD, categories, publish workflow, caching | JPA, Redis, MapStruct |
| 7 | **Lesson Service** | 8084 | Sections, video/text lessons, ordering | JPA, Flyway |
| 8 | **Enrollment Service** | 8085 | Enroll students, track progress, Kafka consumer | Feign, Kafka, Resilience4j |
| 9 | **Review Service** | 8086 | Course ratings (1–5), reviews, average calculation | JPA |
| 10 | **Payment Service** | 8087 | Payment processing, Kafka event publisher | Kafka Producer |
| 11 | **Notification Service** | 8090 | Email notifications via Kafka consumers | Spring Mail, Kafka |
| 12 | **Analytics Service** | 8091 | Revenue tracking, enrollment stats, Kafka consumers | JPA, Kafka |

------

## 🛠 Tech Stack

<table>
<tr><td><strong>Core</strong></td><td>Java 17, Spring Boot 3.2, Maven (multi-module)</td></tr>
<tr><td><strong>Service Mesh</strong></td><td>Spring Cloud 2023 — Config, Eureka, Gateway, OpenFeign</td></tr>
<tr><td><strong>Security</strong></td><td>Spring Security 6, JWT (JJWT 0.12), RBAC (STUDENT / INSTRUCTOR / ADMIN)</td></tr>
<tr><td><strong>Messaging</strong></td><td>Apache Kafka 3.x (async events), OpenFeign (sync RPC)</td></tr>
<tr><td><strong>Persistence</strong></td><td>Spring Data JPA, Hibernate, PostgreSQL 15 (database-per-service)</td></tr>
<tr><td><strong>Caching</strong></td><td>Redis 7 — course catalog, user profiles (TTL: 30 min)</td></tr>
<tr><td><strong>Resilience</strong></td><td>Resilience4j — Circuit Breaker, Retry, Rate Limiter</td></tr>
<tr><td><strong>Migrations</strong></td><td>Flyway — versioned SQL migrations per service</td></tr>
<tr><td><strong>Observability</strong></td><td>Spring Actuator, Micrometer, Zipkin distributed tracing</td></tr>
<tr><td><strong>Docs</strong></td><td>SpringDoc OpenAPI 3 / Swagger UI — per service + gateway aggregation</td></tr>
<tr><td><strong>Code Gen</strong></td><td>Lombok (boilerplate), MapStruct (compile-time DTO mapping)</td></tr>
<tr><td><strong>Testing</strong></td><td>JUnit 5, Mockito, Testcontainers</td></tr>
<tr><td><strong>DevOps</strong></td><td>Docker, Docker Compose (20+ containers)</td></tr>
</table>

---

## 🗃 Database Design

Each service owns its own PostgreSQL schema (database-per-service pattern):

```
 auth_db
 └── users            (id, email, password, role, is_active)
 └── refresh_tokens   (id, token, user_id, expires_at, is_revoked)

 user_db
 └── user_profiles    (id, email, first_name, bio, headline, role)

 course_db
 └── categories       (id, name, slug, parent_id)
 └── courses          (id, title, instructor_id, category_id, price, status, avg_rating)

 lesson_db
 └── sections         (id, course_id, title, order_index)
 └── lessons          (id, section_id, title, type, video_url, order_index)

 enrollment_db
 └── enrollments      (id, student_id, course_id, status, progress_pct) UNIQUE(student,course)

 review_db
 └── reviews          (id, student_id, course_id, rating 1-5, comment) UNIQUE(student,course)

 payment_db
 └── payments         (id, student_id, course_id, amount, status, transaction_id)

 analytics_db
 └── course_analytics (course_id, total_enrollments, total_revenue, completion_rate)
```

---

## 🌐 Service URLs

| Portal | URL |
|--------|-----|
| 🔀 API Gateway | http://localhost:8080 |
| 🗂 Eureka Dashboard | http://localhost:8761 |
| 📋 Swagger — Auth | http://localhost:8081/swagger-ui.html |
| 📋 Swagger — Courses | http://localhost:8083/swagger-ui.html |
| 📋 Swagger — Enrollment | http://localhost:8085/swagger-ui.html |
| 📊 Kafka UI | http://localhost:8092 |
| 🔍 Zipkin Tracing | http://localhost:9411 |

---


## 🔐 Authentication & Authorization

### Roles

| Role | Permissions |
|------|-------------|
| `STUDENT` | Browse courses, enroll, review, track progress |
| `INSTRUCTOR` | Create & publish courses, manage lessons, view analytics |
| `ADMIN` | Full platform access |

### Auth Flow

```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane@example.com",
    "password": "Secure@1234",
    "role": "STUDENT"
  }'

# 2. Login → receive JWT
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"jane@example.com","password":"Secure@1234"}'

# 3. Use JWT in subsequent requests
curl http://localhost:8080/api/courses \
  -H "Authorization: Bearer <your_jwt_token>"
```

---


## 📖 API Documentation

### Core Endpoints

#### Courses
```
GET    /api/courses              → Search & filter published courses
GET    /api/courses/{id}         → Get course details (cached in Redis)
POST   /api/courses              → Create course [INSTRUCTOR]
PUT    /api/courses/{id}         → Update course [INSTRUCTOR]
PATCH  /api/courses/{id}/publish → Publish course [INSTRUCTOR]
GET    /api/courses/popular      → Top courses by enrollment
```

#### Enrollment
```
POST  /api/enrollments           → Enroll in a course (via Feign → Course Service)
GET   /api/enrollments/my        → My enrolled courses
PATCH /api/enrollments/{courseId}/progress → Update lesson progress
```

#### Payments
```
POST /api/payments               → Process payment (publishes Kafka event)
GET  /api/payments/my            → My payment history
GET  /api/payments/{id}          → Payment details
```

#### Reviews
```
POST /api/reviews                → Submit course review (1–5 stars)
GET  /api/reviews/course/{id}    → Course reviews (paginated)
GET  /api/reviews/course/{id}/rating → Average rating
PUT  /api/reviews/{id}           → Update review
```

#### Analytics *(INSTRUCTOR/ADMIN)*
```
GET /api/analytics/courses/{courseId}   → Course stats & revenue
GET /api/analytics/courses/top-revenue  → Top earning courses
```

---

## 🧪 Testing

```bash
# Run all unit tests
mvn test

# Run tests for specific service
cd auth-service && mvn test
cd enrollment-service && mvn test
cd payment-service && mvn test
cd course-service && mvn test

# Run with coverage report
mvn test jacoco:report
```

**Test coverage includes:**
- ✅ `AuthServiceTest` — register, login, duplicate email, invalid credentials
- ✅ `CourseServiceTest` — create, fetch, not-found, publish flow
- ✅ `EnrollmentServiceTest` — enroll, duplicate check, progress update, completion
- ✅ `PaymentServiceTest` — process payment, duplicate payment rejection, Kafka publish

---
## 🏛 Architecture Patterns

| Pattern | Implementation |
|---------|----------------|
| **Database per Service** | Each service has its own PostgreSQL instance |
| **API Gateway** | Single entry point — auth, routing, rate limiting |
| **Event-Driven (Saga)** | Payment → Enrollment choreography via Kafka |
| **Circuit Breaker** | Resilience4j on all Feign clients |
| **CQRS (lite)** | Redis cache for hot read paths |
| **DTO Pattern** | Request/Response DTOs, never expose entities |
| **Repository Pattern** | Spring Data JPA repositories |
| **Compile-time Mapping** | MapStruct — zero reflection DTO mapping |

---

## 🔭 Observability

```bash
# Health check any service
curl http://localhost:808X/actuator/health

# View distributed traces
open http://localhost:9411     # Zipkin UI

# Monitor Kafka topics & consumers
open http://localhost:8092     # Kafka UI

# Metrics (Prometheus-compatible)
curl http://localhost:808X/actuator/metrics
curl http://localhost:808X/actuator/prometheus
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'feat: add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request



