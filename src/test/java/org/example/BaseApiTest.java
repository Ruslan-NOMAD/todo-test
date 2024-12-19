package org.example;

import org.example.controllers.TodoController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.example.utils.EntityManager.cleaner;

public class BaseApiTest {
    protected TodoController todoController;


    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        todoController = new TodoController();
    }

    @AfterEach
    public void cleaning() throws IOException, InterruptedException {
        cleaner();
    }

}
