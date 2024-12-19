package org.example.functional;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.apache.log4j.Logger;
import org.example.BaseApiTest;
import org.example.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.example.controllers.TodoController.getTodoInvalOffset;
import static org.example.controllers.TodoController.getTodos;
import static org.example.utils.EntityManager.createTodoTask;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTodosTest extends BaseApiTest {
    private static final Logger logger = Logger.getLogger(GetTodosTest.class);

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        logger.info("Starting setup: creating a new todo");
        RestAssured.defaultParser = Parser.JSON;
        Todo todo = createTodoTask();
        if (todo == null || todo.getId() == 0) {
            throw new RuntimeException("Todo creation failed during setup.");
        }
        int todoId = todo.getId();
        logger.info("Created new todo with ID: " + todoId);
    }

    @Test
    public void testGetAllTodos() throws IOException, InterruptedException {
        logger.info("Testing retrieval of all todos");
        List<Todo> todos = getTodos(0, 100);
        logger.info("Successfully retrieved all todos: " + todos.size());
    }

    @Test
    public void testGetTodosWithOffset() throws IOException, InterruptedException {
        int offset = 1;
        int limit = 0;
        logger.info("Testing retrieval of todos with offset: " + offset);
        List<Todo> todos = getTodos(offset, limit);
        assertEquals(limit, todos.size(), "Expected " + limit + " todos.");
        logger.info("Successfully retrieved todos with offset " + offset + ", count: " + todos.size());
    }

    @Test
    public void testGetTodosWithLimit() throws IOException, InterruptedException {
        int limit = 1;
        createTodoTask();
        logger.info("Testing retrieval of todos with limit: " + limit);
        List<Todo> todos = getTodos(0, limit);
        assertEquals(limit, todos.size(), "Expected " + limit + " todo.");
        logger.info("Successfully retrieved todos with limit " + limit + ", count: " + todos.size());
    }

    @Test
    public void testGetTodosWithOffsetAndLimit() throws IOException, InterruptedException {
        int offset = 1;
        int limit = 1;
        createTodoTask();
        createTodoTask();
        logger.info("Testing retrieval of todos with offset: " + offset + " and limit: " + limit);
        List<Todo> todos = getTodos(offset, limit);
        assertEquals(limit, todos.size(), "Expected " + limit + " todos.");
        logger.info("Successfully retrieved todos with offset " + offset + " and limit " + limit + ", count: " + todos.size());
    }

    @Test
    public void testGetTodosWithInvalidOffset(){
        int invalidOffset = -1;
        int limit = 3;
        logger.info("Testing retrieval of todos with invalid offset: " + invalidOffset);
        try {
            getTodoInvalOffset(invalidOffset, limit);
            logger.error("Expected failure for invalid offset: " + invalidOffset);
        } catch (Exception e) {
            logger.warn("Failed to retrieve todos with invalid offset " + invalidOffset + ", error: " + e.getMessage());
        }
    }
}
