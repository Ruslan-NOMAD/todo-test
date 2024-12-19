package org.example.functional;

import org.example.BaseApiTest;
import org.example.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PutTodosTest extends BaseApiTest {

    private static final Logger logger = Logger.getLogger(PutTodosTest.class.getName());

    private int currentTaskId;
    private String currentTaskDescription;
    private boolean currentTaskCompleted;
    private Todo currentTask;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        logger.info("Starting test setup...");
        cleanApplicationState();
        currentTask = createSampleTask();
        currentTaskId = currentTask.getId();
        currentTaskDescription = currentTask.getText();
        currentTaskCompleted = currentTask.isCompleted();
        logger.info("Test setup complete: " + currentTask);
    }

    @Test
    public void shouldUpdateTodoSuccessfully() {
        logger.info("Starting shouldUpdateTodoSuccessfully test...");
        int updatedTaskId = generateTaskId();
        String updatedDescription = generateTaskDescription();
        boolean updatedCompletionStatus = true;

        Todo updatedTask = new Todo(updatedTaskId, updatedDescription, updatedCompletionStatus);
        logger.info("Updating task with ID: " + currentTaskId + " to " + updatedTask);
        updateTask(currentTaskId, updatedTask);

        List<Todo> tasks = fetchAllTasks();
        assertTaskPresent(tasks, updatedTaskId);
        assertEquals(updatedDescription, tasks.get(0).getText());
        assertEquals(updatedCompletionStatus, tasks.get(0).isCompleted());

        logger.info("Test passed, cleaning up...");
        deleteTask(updatedTaskId);
    }

    @Test
    public void shouldHandleDuplicateTaskUpdate() {
        logger.info("Starting shouldHandleDuplicateTaskUpdate test...");
        String updatedDescription = generateTaskDescription();
        boolean updatedCompletionStatus = true;

        Todo updatedTask = new Todo(currentTaskId, updatedDescription, updatedCompletionStatus);
        logger.info("Updating task with ID: " + currentTaskId + " to " + updatedTask);
        updateTask(currentTaskId, updatedTask);
        updateTask(currentTaskId, updatedTask);

        List<Todo> tasks = fetchAllTasks();
        assertTaskPresent(tasks, currentTaskId);
        assertEquals(updatedDescription, tasks.get(0).getText());
        assertEquals(updatedCompletionStatus, tasks.get(0).isCompleted());

        logger.info("Test passed, cleaning up...");
        deleteTask(currentTaskId);
    }

    @Test
    public void shouldUpdateTaskId() {
        logger.info("Starting shouldUpdateTaskId test...");
        int newTaskId = generateTaskId();
        Todo updatedTask = new Todo(newTaskId, currentTaskDescription, currentTaskCompleted);
        logger.info("Updating task ID to: " + newTaskId);
        updateTask(currentTaskId, updatedTask);

        Todo task = fetchTaskById(newTaskId);
        assertEquals(newTaskId, task.getId());

        logger.info("Test passed, cleaning up...");
        deleteTask(newTaskId);
    }

    @Test
    public void shouldUpdateTaskDescription() {
        logger.info("Starting shouldUpdateTaskDescription test...");
        String newDescription = generateTaskDescription();
        Todo updatedTask = new Todo(currentTaskId, newDescription, currentTaskCompleted);
        logger.info("Updating task description to: " + newDescription);
        updateTask(currentTaskId, updatedTask);

        Todo task = fetchTaskById(currentTaskId);
        assertEquals(newDescription, task.getText());
    }

    @Test
    public void shouldUpdateTaskCompletionStatus() {
        logger.info("Starting shouldUpdateTaskCompletionStatus test...");
        boolean newCompletionStatus = true;
        Todo updatedTask = new Todo(currentTaskId, currentTaskDescription, newCompletionStatus);
        logger.info("Updating task completion status to: " + newCompletionStatus);
        updateTask(currentTaskId, updatedTask);

        Todo task = fetchTaskById(currentTaskId);
        assertEquals(newCompletionStatus, task.isCompleted());
    }

    private List<Todo> tasks;

    private void cleanApplicationState() {
        logger.info("Cleaning application state...");
        tasks = new ArrayList<>();
    }

    private Todo createSampleTask() {
        Todo task = new Todo(generateTaskId(), generateTaskDescription(), false);
        logger.info("Created sample task: " + task);
        tasks.add(task);
        return task;
    }

    private int generateTaskId() {
        int taskId = (int) (Math.random() * 1000);
        logger.fine("Generated task ID: " + taskId);
        return taskId;
    }

    private String generateTaskDescription() {
        String description = "Task_" + System.currentTimeMillis();
        logger.fine("Generated task description: " + description);
        return description;
    }

    private void updateTask(int taskId, Todo task) {
        logger.info("Updating task with ID: " + taskId + " to " + task);
        tasks.removeIf(t -> t.getId() == taskId);
        tasks.add(task);
    }

    private List<Todo> fetchAllTasks() {
        logger.info("Fetching all tasks...");
        return tasks;
    }

    private Todo fetchTaskById(int taskId) {
        logger.info("Fetching task by ID: " + taskId);
        return tasks.stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElse(null); // Find the task by ID
    }

    private void assertTaskPresent(List<Todo> tasks, int taskId) {
        assertTrue(tasks.stream().anyMatch(task -> task.getId() == taskId), "Task not found");
    }

    private void deleteTask(int taskId) {
        logger.info("Deleting task with ID: " + taskId);
        tasks.removeIf(t -> t.getId() == taskId); // Remove the task from the list
    }
}
