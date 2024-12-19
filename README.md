# TODO App Automated Tests

## Overview

This project provides automated tests for a simple TODO application.

## Technologies Used
![Java](https://img.shields.io/badge/Java-17-brightgreen)
![JUnit5](https://img.shields.io/badge/JUnit-5-brightgreen)
![RESTAssured](https://img.shields.io/badge/RESTAssured-v5-brightgreen)
![Jackson](https://img.shields.io/badge/Jackson-databind-brightgreen)
![SLF4j](https://img.shields.io/badge/SLF4J-v1-brightgreen)
![Java WebSockets](https://img.shields.io/badge/Java-WebSockets-brightgreen)

## Project Structure

* `src/main/java`: Contains the main application code (not used in this test project).
* `src/test/java`: Contains the test classes and utilities.
    * `functional`: Contains functional tests for the TODO application endpoints.
    * `performance`: Contains performance tests for the TODO application endpoints.
    * `ws`: Contains websocket tests for the TODO application endpoints.
* `src/test/resources`: Can be used for test data or configuration files.

```src
├── main
│   └── java
│       └── org
│           └── example
│               ├── config
│               │   └── ConfigReader.java
│               ├── controllers
│               │   └── TodoController.java
│               ├── models
│               │   └── Todo.java
│               ├── utils
│               │   └── EntityManager.java
│               └── Main.java
│               
│               
└── test
    └── java
        └── org
           └── example
               ├── functional
               │   ├── DeleteTodosTest.java
               │   ├── GetTodosTest.java
               │   ├── PostTodosTest.java
               │   └── PutTodosTest.java
               ├── performance
               │   └── PerformanceTest.java
               ├── ws
               │   └── WSTest.java
               └── BaseApiTest

## Running the Tests

1. **Build the project:** `mvn clean install`
2. **Run the tests:** `mvn test`

## Test Cases

### GET /todos

1. **Verify successful retrieval of all TODOs.**
2. **Verify retrieval of TODOs with offset.**
3. **Verify retrieval of TODOs with limit.**
4. **Verify retrieval of TODOs with offset and limit.**
5. **Verify handling of invalid offset/limit values.**

### POST /todos

1. **Verify successful creation of a new TODO.**
2. **Verify handling of missing required fields.**
3. **Verify handling of invalid field values.**
4. **Verify handling of duplicate TODO creation.**
5. **Verify response headers.**

### PUT /todos/:id

1. **Verify successful update of an existing TODO.**
2. **Verify handling of missing required fields.**
3. **Verify handling of invalid field values.**
4. **Verify handling of updating a non-existent TODO.**
5. **Verify response headers.**

### DELETE /todos/:id

1. **Verify successful deletion of an existing TODO.**
2. **Verify handling of deleting a non-existent TODO.**
3. **Verify handling of missing Authorization header.**
4. **Verify handling of invalid Authorization credentials.**
5. **Verify response headers.**

### /ws

1. **Verify successful WebSocket connection.**
2. **Verify receiving updates on new TODO creation.**
3. **Verify handling of connection errors.**
4. **Verify message format.**
5. **Verify handling of invalid messages.**

## Additional Test Cases (Checklist for Future Implementation)

* **GET /todos:**
    * Test with various combinations of valid and invalid query parameters.
    * Test with a large number of TODOs.
* **POST /todos:**
    * Test with various valid and invalid TODO data.
    * Test with special characters and unicode in TODO text.
* **PUT /todos/:id:**
    * Test updating individual fields of a TODO.
    * Test with various valid and invalid TODO data.
* **DELETE /todos/:id:**
    * Test with various authorization scenarios.
* **/ws:**
    * Test with multiple concurrent WebSocket connections.
    * Test with different types of WebSocket messages.

## Performance Test Cases

* **POST /todos:**
    * Measure response times for creating a new TODO under different load conditions.
    * Measure throughput of the POST /todos endpoint.
    * Identify performance bottlenecks.

## Notes

* The application can be run with `VERBOSE=1` to see more logs.
* The focus is on the quality of automated tests and project structure. The load test is an additional task.