# Pet Service Application

## Overview
This application for managing pets is built with Spring Boot and Java 17. 
The application allows creating, updating, deleting, and retrieving pet information.

## Features
- Create, update, delete, and fetch pets
- Validation for required fields (name, species) and optional fields (age, ownerName)
- In-memory repository for easier testing and demonstration
- Prepared for future database replacement without changing core domain logic
- Swagger/OpenAPI annotations for API documentation and testing
- Centralized exception handling with GlobalExceptionHandler and standardized ApiError responses

# Pet entity:
- id (Long, auto-generated)
- name (String, required)
- species (String, required, e.g., "Dog", "Cat", "Rabbit")
- age (Integer, optional, must be >= 0)
- ownerName (String, optional)
Note: species is a string for simplicity. It could be converted to an enum if stricter domain rules are required.

# Architecture Decisions
DDD & Hexagonal Architecture: Chosen to isolate the domain logic from the database,
making it easier to refactor or replace persistence mechanisms in the future while keeping the core logic 
clean and maintainable.

# Mapper Layer: 
MapStruct is used for mappings between DTOs, domain objects, and entities.

# In-Memory Repository:
Used for the current implementation to satisfy requirements quickly without a real database. 
Switching to a relational or non-relational database would not require changes in the domain layer.

# Tests
Unit Tests: Cover PetService and domain operations.
Controller Tests: Mock MVC tests to verify REST API behavior.
Integration Tests: Verify end-to-end behavior using the in-memory repository.

# API endpoints are available at:
http://localhost:8080/api/v1/pets

# Swagger UI for testing the API:
http://localhost:8080/swagger-ui.html