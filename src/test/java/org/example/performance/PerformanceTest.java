package org.example.performance;

import org.apache.log4j.Logger;
import org.example.BaseApiTest;
import org.example.controllers.TodoController;
import org.example.models.Todo;
import org.example.utils.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTest extends BaseApiTest {

    private static final Logger logger = Logger.getLogger(PerformanceTest.class);
    @Test
    public void testPostTodosPerformance() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                Todo todo = new Todo(0, "Performance Test TODO", false);

                try {
                    logger.info("Creating task: " + todo);

                    TodoController.createTodo(todo);
                } catch (Exception e) {
                    logger.error("Error creating task: ", e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("Total time: " + totalTime + " ms");
    }

}
