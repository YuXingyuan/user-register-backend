# User Registration Backend Service

* This project is built with `Java 21` and `Spring Boot 3.x`, it is packaged with `Maven` and containerized with `Docker`
* It supports adding, editing, reading, (soft) deleting single and multiple user(s)
* It connects to `PostgreSQL` and `Fake-SMTP-Server` as external DB and Email services respectively

## Test

  ```bash
  $ mvn clean test
  ```

## Run

  ```bash
  $ docker-compose up -d
  ```

## Stop

  ```bash
  $ docker-compose down --rmi all -v
  ```

## REST API Documentation

  ```
  http://localhost:8080/swagger-ui.html
  http://localhost:8080/v3/api-docs
  ```

## Email Portal 

Visualize those emails sent

  ```
  http://localhost:8081/
  ```
