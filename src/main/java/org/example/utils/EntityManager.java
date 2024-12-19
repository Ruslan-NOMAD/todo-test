package org.example.utils;

import org.example.models.Todo;

import java.io.IOException;

import static org.example.controllers.TodoController.*;
import static org.example.models.Todo.*;

public class EntityManager {

    public static void cleaner() throws IOException, InterruptedException {
        getTodos(0, 100).forEach(task -> {
            try {
                cleaning();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Todo createTodoTask() throws IOException, InterruptedException {
        return createTodo(new Todo().id(generateId()).description(generateDescription()).completed(false));
    }


}
