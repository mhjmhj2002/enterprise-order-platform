
# SOFTWARE_ENGINEER_PLAYBOOK

**Project:** Enterprise Order Platform  
**Role:** Software Engineer  
**Version:** 1.0  
**Status:** Baseline  
**Owner:** Engineering Manager  
**Applies to:** All implementation tasks and code-related stories

---

# 1. Mission

The Software Engineer is responsible for implementing approved Stories with production-grade quality, respecting the project architecture, domain model, engineering standards and delivery workflow.

The Software Engineer must not behave as a code generator.

The expected behavior is that of a professional engineer who:

- understands the task before coding;
- identifies risks;
- asks questions when requirements are unclear;
- protects the domain model;
- writes tests;
- documents changes;
- opens Pull Requests;
- waits for review;
- publishes releases only after approval.

---

# 2. Current Project Stack

This is the official stack of the project.

Any deviation requires explicit approval from the Tech Lead and Engineering Manager.

| Area | Technology / Version |
|---|---|
| Language | Java 21 LTS |
| Framework | Spring Boot 3.3.x |
| Build Tool | Maven |
| Architecture | Hexagonal Architecture / Ports & Adapters |
| Domain Modeling | Domain-Driven Design |
| Persistence | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Database Migration | Flyway |
| API Style | REST |
| API Documentation | springdoc-openapi / Swagger |
| Mapping | MapStruct |
| Unit Tests | JUnit 5 |
| Mocking | Mockito |
| Integration Tests | Spring Boot Test + Testcontainers + PostgreSQL |
| Logging | SLF4J + Logback |
| Containers | Docker / Docker Compose |
| Git Workflow | Feature Branch + Pull Request |
| Versioning | Semantic Versioning |
| Commits | Conventional Commits |

## Critical Stack Rules

- Do not use Java 17.
- Do not use Java 25.
- Use Java 21 unless an ADR explicitly changes the stack.
- Do not upgrade Spring Boot without approval.
- Do not introduce new dependencies without justification.
- Do not replace Maven.
- Do not use H2 for integration tests when PostgreSQL behavior matters.
- Do not bypass Flyway.
- Do not use `spring.jpa.hibernate.ddl-auto=update`.

---

# 3. Engineering Principles

All implementation must follow these principles.

## SOLID

### Single Responsibility Principle
Each class must have one clear reason to change.

Examples:

- Controller handles HTTP only.
- Use Case orchestrates application flow.
- Aggregate protects domain invariants.
- Repository implementation handles persistence only.

### Open/Closed Principle
Code should be open for extension and closed for modification when business variation is expected.

Do not create abstractions prematurely.

Use this principle where variation is already visible in the domain.

### Liskov Substitution Principle
Do not create inheritance hierarchies that make substituting implementations unsafe.

Prefer composition over inheritance.

### Interface Segregation Principle
Do not create large generic interfaces.

Ports must be specific to the needs of the use case.

### Dependency Inversion Principle
Application and domain must depend on abstractions, not infrastructure implementations.

---

## KISS

Prefer the simplest solution that correctly solves the current problem.

Avoid unnecessary frameworks, abstractions and patterns.

---

## YAGNI

Do not implement future features unless the Story explicitly requires them.

Examples of things not to add unless requested:

- Kafka
- RabbitMQ
- Redis
- OAuth
- Saga
- Outbox
- Kubernetes
- Advanced observability
- Generic frameworks
- Shared libraries

---

## DRY with judgment

Avoid harmful duplication.

However, do not create abstractions too early just to remove small, harmless duplication.

---

## High Cohesion / Low Coupling

Classes should group related behavior and avoid unnecessary dependencies.

A domain object should not know about HTTP, JPA, Kafka, Redis, Spring or external APIs.

---

## Fail Fast

Invalid states should be rejected as early as possible.

Domain invariants must be enforced inside the domain model.

---

## Tell, Don't Ask

Prefer behavior-rich domain objects.

Bad:

```java
if (product.getSkus().isEmpty()) {
    product.setStatus(ACTIVE);
}
```

Better:

```java
product.activate();
```

The aggregate decides whether the transition is valid.

---

# 4. Mandatory Architecture

The project uses Hexagonal Architecture.

Each service must follow this separation:

```text
service-name/
└── src/main/java
    └── com/mercadoaurora/<service>
        ├── domain
        ├── application
        ├── infrastructure
        ├── api
        └── config
```

The exact internal package structure may vary, but responsibilities must not be mixed.

---

# 5. Layer Responsibilities

## 5.1 Domain Layer

The domain layer contains the business model.

Allowed:

- Aggregates
- Entities
- Value Objects
- Domain Events
- Domain Services when truly needed
- Domain exceptions
- Repository ports/interfaces if used by application/domain

Forbidden:

- Spring annotations
- JPA annotations
- HTTP annotations
- Database-specific logic
- JSON serialization concerns
- External API calls
- Framework dependencies

The domain must be testable without Spring.

---

## 5.2 Application Layer

The application layer coordinates use cases.

Allowed:

- Use Cases
- Application services
- Ports
- Transaction boundaries
- Repository interfaces
- Input/output application models when needed

Responsibilities:

- Load aggregate
- Call aggregate behavior
- Persist aggregate
- Coordinate ports
- Translate domain exceptions when necessary

Forbidden:

- Business rules that belong inside aggregates
- HTTP details
- JPA details
- SQL details
- Kafka details
- Framework-heavy logic

---

## 5.3 Infrastructure Layer

Infrastructure implements adapters.

Allowed:

- JPA entities
- Spring Data repositories
- Persistence mappers
- External API clients
- Messaging adapters
- Database configuration
- Flyway migrations

Forbidden:

- Owning business rules
- Deciding domain transitions
- Duplicating domain invariants

Infrastructure adapts the outside world to the application/domain.

---

## 5.4 API Layer

The API layer handles HTTP.

Allowed:

- Controllers
- Request DTOs
- Response DTOs
- API mappers
- HTTP status codes
- Validation annotations
- Swagger/OpenAPI annotations
- Global exception handlers

Forbidden:

- Business rules
- Direct repository access
- JPA entity exposure
- Domain mutation logic
- Transaction orchestration beyond request delegation

Controllers must be thin.

---

# 6. Domain Modeling Rules

## Aggregates

Aggregates must protect invariants.

Only the Aggregate Root should expose methods that mutate aggregate state.

Example:

```java
product.addSku(...)
inventoryItem.reserve(...)
reservation.commit()
```

Avoid public setters in domain objects.

## Entities

Entities have identity and lifecycle.

They may be internal to an aggregate.

Do not expose entity mutation if it violates aggregate invariants.

## Value Objects

Value Objects represent concepts without identity.

They should be immutable where possible.

Examples:

- ProductId
- SkuId
- WarehouseId
- Specification
- Image
- Quantity

## Domain Events

Domain Events represent facts that happened in the domain.

Examples:

- ProductCreated
- StockReserved
- ReservationCommitted

Rules:

- Events may be recorded by aggregates.
- Do not publish events directly from domain objects.
- Do not introduce Kafka or messaging unless the Story explicitly requires it.
- Events may remain in-memory/domain-only until infrastructure is approved.

## Domain Exceptions

Use domain-specific exceptions.

Bad:

```java
throw new RuntimeException("error");
```

Better:

```java
throw new InsufficientStockException(...);
```

---

# 7. REST API Standards

All APIs must use:

```text
/api/v1
```

## Resource naming

Use plural nouns.

Good:

```http
GET /api/v1/products
POST /api/v1/products/{productId}/skus
```

Bad:

```http
POST /api/v1/createProduct
```

## HTTP methods

- `GET` for reading
- `POST` for creation/commands that create sub-resources
- `PUT` for full replacement/update when appropriate
- `PATCH` for partial updates/state transitions
- `DELETE` only when physical deletion is a business requirement

## Error responses

All services must return standardized error responses.

Minimum fields:

```json
{
  "timestamp": "2026-07-08T12:00:00Z",
  "status": 409,
  "error": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock for SKU ABC",
  "path": "/api/v1/inventory/ABC/SP"
}
```

## Status codes

Use:

- `200 OK` for successful reads/updates
- `201 Created` for successful creation
- `400 Bad Request` for validation errors
- `404 Not Found` for missing resources
- `409 Conflict` for domain rule violations
- `500 Internal Server Error` only for unexpected failures

Never expose stack traces in API responses.

---

# 8. Persistence Standards

## Flyway

Every relational database change must use Flyway.

Migration path:

```text
src/main/resources/db/migration
```

Naming:

```text
V1__init_<service>_schema.sql
V2__add_<change>.sql
```

Rules:

- Never edit an already applied migration.
- Do not use `ddl-auto=update`.
- Do not rely on manual database changes.
- Create indexes and constraints intentionally.

## JPA

JPA belongs to infrastructure.

Do not annotate domain objects with JPA unless the architecture explicitly allows it.

Preferred approach:

- Domain model stays clean.
- Persistence model maps to database.
- Mapper converts between persistence and domain.

## Constraints

Enforce important uniqueness rules at the database level when needed.

Example:

- sellerCode unique
- ean unique when provided
- skuId + warehouseId unique

Domain validates behavior.

Database protects consistency.

---

# 9. Mapping Standards

Use MapStruct where appropriate.

Mapping responsibilities:

- API mapper: Request/Response ↔ Application models
- Persistence mapper: JPA entity ↔ Domain model

Do not put mapping logic inside Controllers.

Do not expose JPA entities through REST.

Do not expose internal domain objects if a Response DTO is required.

---

# 10. Testing Standards

Testing is mandatory from the first implementation.

A Story is not Done without tests.

## 10.1 Unit Tests

Use:

- JUnit 5
- AssertJ or standard assertions
- Mockito only when dependencies exist

Unit tests must be fast and isolated.

They must not start Spring.

### Domain unit tests

Mandatory for:

- aggregate invariants
- entity state transitions
- value object validation
- domain exceptions
- edge cases

Examples:

- Product cannot be ACTIVE without active SKU.
- InventoryItem cannot reserve more than available.
- Reservation cannot transition after final state.

## 10.2 Application Tests

Use mocks for ports/repositories.

Application tests must validate use case behavior:

- successful flow
- not found
- conflict/domain errors
- interaction with repository/ports
- no infrastructure dependency

## 10.3 Integration Tests

Use:

- Spring Boot Test
- Testcontainers
- PostgreSQL

Integration tests must validate:

- REST endpoints
- JSON payloads
- HTTP status codes
- persistence
- Flyway migrations
- database constraints

If Docker is unavailable, document this limitation clearly in the PR.

Do not silently skip integration tests without explanation.

## 10.4 Test Naming

Use descriptive names.

Good:

```java
shouldNotActivateProductWithoutActiveSku()
shouldRejectReservationWhenQuantityExceedsAvailableStock()
```

Bad:

```java
test1()
testCreate()
```

## 10.5 Minimum Coverage Expectation

No arbitrary percentage target is required yet.

However, all business rules and relevant failure flows must be tested.

---

# 11. Logging Standards

Use SLF4J.

Do not use:

```java
System.out.println()
```

Rules:

- Log useful business/technical context.
- Do not log sensitive data.
- Do not log passwords, tokens, card data or personal data.
- Avoid noisy logs.
- Use appropriate levels.

Levels:

- `INFO`: relevant business/application events
- `WARN`: unexpected but recoverable situations
- `ERROR`: failures requiring investigation
- `DEBUG`: technical details for development

---

# 12. Dependency Rules

Before adding any dependency, answer:

- What problem does it solve?
- Is the problem real now?
- Can the current stack solve it?
- Is the library mature?
- Is it actively maintained?
- Does it increase build/test/deploy complexity?
- Does it introduce lock-in?

If the dependency affects architecture, request approval before adding it.

---

# 13. Git Workflow

Direct commits to `main` are forbidden.

Mandatory workflow:

1. Create a branch from `main`
2. Implement changes
3. Commit using Conventional Commits
4. Push branch
5. Open Pull Request
6. Link PR to Story
7. Add labels, milestone and project
8. Request review
9. Address comments
10. Merge only after approval
11. Publish release when required
12. Close Story only after release

## Branch naming

Examples:

```text
feature/story-008-bootstrap-inventory-service
fix/story-008-review-adjustments
docs/story-009-update-architecture
```

## Conventional Commits

Examples:

```text
feat(inventory-service): bootstrap inventory domain
fix(catalog-service): remove productId from sku domain
docs(architecture): update service boundaries
test(inventory-service): add reservation domain tests
```

---

# 14. Pull Request Standards

Every PR must include:

## Summary

What was implemented.

## Scope

What is included.

## Out of scope

What was intentionally not implemented.

## Architecture notes

Relevant design decisions.

## Tests

What was tested.

## Documentation

What docs were updated.

## Known limitations

Environment limitations, skipped tests, pending work.

## Checklist

```md
- [ ] Build passes
- [ ] Unit tests pass
- [ ] Application tests pass
- [ ] Integration tests pass or limitation documented
- [ ] Documentation updated
- [ ] ADR updated or created when required
- [ ] CHANGELOG updated
- [ ] PROJECT_HISTORY updated
- [ ] Story linked
- [ ] Release planned
```

---

# 15. Release Workflow

A Story that requires release is not Done until the release is published.

Release naming pattern:

```text
v0.1.0-catalog-service
v0.2.0-inventory-service
v0.3.0-order-service
```

Release notes must include:

- implemented scope
- main changes
- tests
- documentation
- known limitations
- next steps

---

# 16. Documentation Responsibilities

Documentation is part of delivery.

Update when applicable:

- README.md
- CHANGELOG.md
- docs/team/PROJECT_HISTORY.md
- service README.md
- docs/architecture/ARCHITECTURE.md
- docs/architecture/CONTEXT_MAP.md
- docs/architecture/SERVICE_BOUNDARIES.md
- docs/architecture/ARCHITECTURE_NOTES.md
- docs/architecture/ADR/*
- docs/api/postman/*
- OpenAPI/Swagger documentation

Do not leave documentation outdated.

---

# 17. When to Stop and Ask

Stop implementation and ask the Tech Lead / Engineering Manager when:

- The task conflicts with existing architecture.
- The task requires a new dependency.
- The task requires changing the stack.
- A domain rule is ambiguous.
- A test cannot be implemented as requested.
- The implementation requires changing an ADR.
- You are tempted to commit directly to main.
- You need to skip integration tests.
- You found a better solution that changes scope.

Do not make silent architectural decisions.

---

# 18. Forbidden Anti-Patterns

Do not introduce:

- God Class
- Fat Controller
- Anemic Domain Model
- Business rules in Controller
- Business rules in JPA entity when domain model is separate
- Repository called directly from Controller
- Infrastructure dependency inside domain
- Static utility classes for business logic
- Generic `BusinessException` without context
- Catching and swallowing exceptions
- Manual database changes outside Flyway
- Commit directly to main
- Release without review
- Story closed without documentation

---

# 19. Story Delivery Checklist

Before reporting "done", verify:

## Code

- [ ] Code compiles
- [ ] Java 21 is used
- [ ] Architecture respected
- [ ] No forbidden dependency introduced
- [ ] No direct commit to main

## Domain

- [ ] Aggregate protects invariants
- [ ] Business rules are not in Controller
- [ ] Domain tests cover rules
- [ ] State transitions are protected

## Tests

- [ ] Unit tests pass
- [ ] Application tests pass
- [ ] Integration tests pass or limitation documented
- [ ] Failure scenarios tested

## Documentation

- [ ] README updated
- [ ] CHANGELOG updated
- [ ] PROJECT_HISTORY updated
- [ ] Architecture docs updated
- [ ] ADR updated/created when required
- [ ] Postman/OpenAPI updated when applicable

## GitHub

- [ ] Branch created
- [ ] PR opened
- [ ] PR linked to Story
- [ ] Labels/milestone/project added
- [ ] Review requested
- [ ] Merge after approval
- [ ] Release published
- [ ] Story closed

---

# 20. Expected Behavior

The Software Engineer must behave as a senior engineer.

This means:

- challenge unclear requirements;
- keep implementation aligned with business language;
- protect architecture boundaries;
- write tests as part of development;
- communicate trade-offs;
- avoid unnecessary complexity;
- leave the repository better than before.

The goal is not just to make the code work.

The goal is to deliver software that can be maintained, explained, reviewed and evolved.
