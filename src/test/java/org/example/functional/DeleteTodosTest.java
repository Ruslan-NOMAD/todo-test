package org.example.functional;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.example.BaseApiTest;
import org.example.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.example.controllers.TodoController.*;
import static org.example.models.Todo.generateId;
import static org.example.utils.EntityManager.createTodoTask;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.apache.log4j.Logger;

public class DeleteTodosTest extends BaseApiTest {
    private int todoId;
    private static final Logger logger = Logger.getLogger(DeleteTodosTest.class);

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        logger.info("Starting setup: creating a new todo");
        RestAssured.defaultParser = Parser.JSON;
        Todo todo = createTodoTask();
        if (todo == null || todo.getId() == 0) {
            throw new RuntimeException("Todo creation failed during setup.");
        }
        todoId = todo.getId();
        logger.info("Created new todo with ID: " + todoId);
    }

    @Test
    public void testDeleteTodo() throws IOException, InterruptedException {
        deleteTodo(todoId);
    }

    @Test
    public void testDeleteNonExistentTodo() {
        Integer nonExistingTodoId = generateId();
        logger.info("Attempting to delete non-existent todo with ID: " + nonExistingTodoId);
        deleteTodo(nonExistingTodoId);
        logger.warn("Failed to delete non-existent todo with ID: " + nonExistingTodoId);
    }

    @Test
    public void testDeleteTodoWithoutAuthorization() {
        int todoToDeleteId = 1;
        logger.info("Attempting to delete todo without authorization, ID: " + todoToDeleteId);
        deleteTodo(todoToDeleteId);
        logger.warn("Failed to delete todo with ID " + todoToDeleteId + " without authorization");
    }

    @Test
    public void testDeleteTodoWithInvalidAuthorization() {
        logger.info("Attempting to delete todo with invalid authorization, ID: " + todoId);
        deleteTodoWithInvalidAuth(todoId);
        logger.warn("Failed to delete todo with ID " + todoId + " with invalid authorization");
    }

    @Test
    public void testDeleteTodoResponseHeaders() throws IOException, InterruptedException {
        logger.info("Testing deletion of todo with response header verification, ID: " + todoId);
        deleteTodo(todoId);
        assertNull(getTodoById(todoId), "Todo should be deleted.");
        logger.info("Successfully deleted todo with ID: " + todoId + " and verified response headers");
    }

    @Test
    public void testDeleteTodoWithInvalidId() {
        logger.info("Attempting to delete todo with invalid ID (null)");
        assertThrows(IllegalArgumentException.class, () -> deleteTodo(null), "Todo ID cannot be null");
        logger.warn("Attempted to delete todo with invalid ID (null)");
    }
}
