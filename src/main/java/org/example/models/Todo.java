package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.IOException;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.example.controllers.TodoController.createTodo;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Todo {
    static Random random = new Random();
    protected int id;

    @JsonProperty("text")
    private String text;

    private boolean completed;


    @Override
    public String toString() {
        String formatted = String.format("{\"id\": %d, \"text\": \"%s\", \"completed\": %b}", id, text, completed);
        System.out.println("[DEBUG] Todo toString() called: " + formatted);
        return formatted;
    }


    public Todo id(Integer id) {
        this.id = id;
        return this;
    }


    public Todo description(String text) {
        this.text = text;
        return this;
    }

    public Todo completed(Boolean completed) {
        this.completed = completed;
        return this;
    }


    public static Todo getTodo() {
        return new Todo()
                .id(generateId())
                .description(generateDescription())
                .completed(false);
    }


    public static Integer generateId() {
        return random.nextInt(10_000_001) + 1;
    }


    public static String generateDescription() {
        return "Todo Task: " + random(5, true, false);
    }
}
