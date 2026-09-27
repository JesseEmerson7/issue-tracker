# Student Support Ticket System

Starter project for the Software Quality & Security assignment.

## Requirements
- Java 21 or later
- Maven 3.9+

## Run
```bash
mvn spring-boot:run
```
Then open `http://localhost:8080/tickets`.

## Tests
```bash
mvn test
```

## Application behavior
The application provides a small internal support-ticket workflow. Users can list tickets, search tickets, create a ticket, view ticket details, and change ticket status. Sample data is loaded into an in-memory H2 database at startup.

## Assignment context
Treat this as an existing application entering a software quality and security review. Do not assume that the supplied implementation, configuration, tests, or dependencies are complete or appropriate simply because the application runs. Evaluate the application according to the assignment requirements and course materials.

The supplied tests are a limited regression baseline, not a comprehensive specification.
