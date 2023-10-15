
# Rate Limiter Service

## Description

This service provides rate limiting capabilities using various algorithms such as Token Bucket, Fixed Window, Leaky Bucket, and more. Built with Spring Boot, the service is easily extendable and customizable.

## Features

- Supports multiple rate-limiting algorithms
- Dynamic endpoint generation based on user's algorithm choice
- User management for rate-limiting configurations
- Spring Security integration for authenticated rate limiting

## Requirements

- PostgreSQL
- Redis (for certain algorithms)

## Configuration

All configurations can be found in `application.properties`.


## Build and Run

\`\`\`bash
mvn clean install
java -jar target/ratelimiter.jar
\`\`\`

## API Endpoints

- `/api/user`: User management API
- `/api/algorithm`: Algorithm choice and configuration
- `/dynamic/{algorithmType}`: Dynamic endpoints for algorithm PoCs

**## it's still work in progress**
