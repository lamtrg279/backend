@workspace Here is the core context and architecture for the project in this repository:

Project Name: Digital Print Job & Inventory Ledger
Domain: Streamlined web-based manufacturing enterprise system (Print Management & B2B Invoicing).

Tech Stack:

Backend: Java 21, Spring Boot 3.x, Spring Data JPA, Hibernate, PostgreSQL

Infrastructure: Docker & Docker Compose (print_ledger_db on port 5432)

Frontend (Target): React (TypeScript), Tailwind CSS

Testing: JUnit 5, Mockito

Core Domain Architecture & Modules:

Job Intake & Price Calculator: REST endpoints processing job specifications and dynamic material cost/pricing logic.

Inventory Reconciliation Engine: Automated transactional event handlers managing stock thresholds, reservations, and rollbacks.

Production Status Dashboard: Order lifecycle state management (QUEUED, IN_PRODUCTION, COMPLETED).

Coding Guidelines & Constraints:

Follow standard Spring Boot layered enterprise architecture (Controller → Service → Repository → Entity/DTO).

Enforce strict transactional integrity for database writes (@Transactional).

Use Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder) to minimize boilerplate.

Use PostgreSQLDialect and Spring Data JPA method conventions.
