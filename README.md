# Agenda Voting

Spring Boot 3 / Java 21 study project for managing agendas, voting sessions, and votes.

## Overview

Agenda Voting is a REST API that allows agendas to be created and voting sessions to be opened for those agendas. Once a session is open, participants can cast a vote using their CPF.

The project demonstrates:

* Spring Boot 3
* Java 21
* Spring Data MongoDB
* MongoDB
* Bean Validation
* OpenAPI / Swagger
* Docker and Docker Compose
* Testcontainers
* Unit and integration tests

## Requirements

To run the project locally, you need:

* Java 21
* Maven
* Docker and Docker Compose

MongoDB can either be provided by Docker Compose or run separately.

## Running the application

The easiest way to start the application and MongoDB is with Docker Compose:

```bash
docker compose up --build
```

The application will be available at:

```text
http://localhost:8085
```

Swagger UI:

```text
http://localhost:8085/swagger-ui/index.html
```

## Configuration

The application uses environment variables for external configuration.

### MongoDB

`MONGODB_URI`

Default:

```text
mongodb://localhost:27017/agendaVoting
```

Example:

```bash
MONGODB_URI=mongodb://localhost:27017/agendaVoting
```

### CPF service

`CPF_CONSULT_URL`

This configures the external CPF provider used by the application.

If not provided, the application uses the historical CPF-provider URL configured by the project.

## Testing

Run the complete Maven verification lifecycle with:

```bash
mvn verify
```

For the regular test suite:

```bash
mvn test
```

The project contains unit tests as well as integration tests.

Tests following the `*IT` naming convention cover MVC, HTTP-client, and repository integration scenarios.

### MongoDB integration tests

The MongoDB repository integration test uses Testcontainers to start a temporary MongoDB instance.

When Docker is available, the MongoDB container is started automatically during the test.

When Docker is unavailable, the Testcontainers test is skipped.

## Business Rules

### Voting sessions

* Only one voting session can be created for each agenda.
* The default voting session duration is one minute.
* When an explicit duration is provided, it must be greater than zero.
* A vote can only be submitted while the corresponding voting session is open.

### Voting

* Each CPF can vote only once in a given voting session.
* A CPF may vote again in a different voting session.
* MongoDB enforces the uniqueness rule using a compound unique index on:

    * `votingSessionId`
    * `cpf`
* The database constraint also protects the application against concurrent submissions.

## API Documentation

The API is documented using OpenAPI.

When the application is running, access the interactive documentation at:

```text
http://localhost:8085/swagger-ui/index.html
```

## Project Structure

The project follows a layered structure:

```text
src/
├── main/
│   ├── java/
│   │   └── com/agendavoting/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── enums/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       └── application.yml
└── test/
    └── java/
        └── com/agendavoting/
```

## Tech Stack

| Technology          | Purpose                         |
| ------------------- | ------------------------------- |
| Java 21             | Programming language            |
| Spring Boot 3       | Application framework           |
| Spring Data MongoDB | MongoDB persistence             |
| MongoDB             | Database                        |
| Maven               | Build and dependency management |
| Docker              | Containerization                |
| Docker Compose      | Local environment               |
| Testcontainers      | Integration testing             |
| JUnit 5             | Testing                         |
| Mockito             | Mocking                         |
| OpenAPI / Swagger   | API documentation               |

## Build

Create the application artifact with:

```bash
mvn clean package
```

Skip tests when necessary:

```bash
mvn clean package -DskipTests
```

## License

This project is intended for study and educational purposes.
