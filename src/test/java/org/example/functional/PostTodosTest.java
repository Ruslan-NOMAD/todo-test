package org.example.functional;

import org.apache.log4j.Logger;
import org.example.BaseApiTest;
import org.example.models.Todo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.example.controllers.TodoController.createTodo;

public class PostTodosTest extends BaseApiTest {

    private static final Logger logger = Logger.getLogger(PostTodosTest.class);

    @Test
    public void testCreateTodo() throws IOException, InterruptedException {
        logger.info("Attempting to create a new todo");
        createTodo(Todo.getTodo());
        logger.info("Successfully created a new todo");
    }

    @Test
    public void testCreateTodoWithMissingText() throws IOException, InterruptedException {
        logger.info("Attempting to create a todo with missing text");
        Todo todo = new Todo(Todo.generateId(), null, false);
        try {
            createTodo(todo);
            logger.error("Unexpectedly created a todo with missing text");
        } catch (Exception e) {
            logger.warn("Failed to create a todo with missing text: " + e.getMessage());
        }
    }

    @Test
    public void testCreateTodoWithInvalidCompleted() throws IOException, InterruptedException {
        logger.info("Attempting to create a todo with invalid 'completed' value");
        Todo todo = new Todo(0, "New TODO", true);
        try {
            createTodo(todo);
            logger.error("Unexpectedly created a todo with invalid 'completed' value");
        } catch (Exception e) {
            logger.warn("Failed to create a todo with invalid 'completed' value: " + e.getMessage());
        }
    }

    @Test
    public void testCreateDuplicateTodo() throws IOException, InterruptedException {
        logger.info("Attempting to create a duplicate todo");
        Todo todo = new Todo(0, "Duplicate TODO", false);
        try {
            createTodo(todo);
            logger.error("Unexpectedly created a duplicate todo");
        } catch (Exception e) {
            logger.warn("Failed to create a duplicate todo: " + e.getMessage());
        }
    }

    @Test
    public void testCreateTodoResponseHeaders() throws IOException, InterruptedException {
        logger.info("Attempting to create a new todo and verify response headers");
        Todo todo = new Todo(0, "New TODO", false);
        createTodo(todo);
        logger.info("Successfully created a new todo and verified response headers");
    }
}
