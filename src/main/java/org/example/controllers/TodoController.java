package org.example.controllers;

import org.example.config.ConfigReader;
import org.example.models.Todo;
import java.io.IOException;
import java.util.List;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TodoController {
    static String baseUri = ConfigReader.getProperty("BASE_URI");
    static String ENDPOINT = ConfigReader.getProperty("ENDPOINT");
    static String CONTENT_TYPE = ConfigReader.getProperty("CONTENT_TYPE");
    static String USERNAME = ConfigReader.getProperty("AUTH_USERNAME");
    static String PASSWORD = ConfigReader.getProperty("AUTH_PASSWORD");

    public static List<Todo> getTodos(int offset, int limit) throws IOException, InterruptedException {
        Response response = given()
                .log().all()
                .baseUri(baseUri)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        List<Todo> todo = response.jsonPath().getList(".", Todo.class);
        System.out.println("Fetched Todos: " + todo);
        return todo;
    }

    public static List<Todo> getTodoInvalOffset(int offset, int limit) throws IOException, InterruptedException {
        Response response = given()
                .log().all()
                .baseUri(baseUri)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(ENDPOINT)
                .then()
                .log().ifError()
                .extract()
                .response();

        List<Todo> todo = response.jsonPath().getList(".", Todo.class);
        System.out.println("Fetched Todos: " + todo);
        return todo;
    }

    public static Todo createTodo(Todo newTodo) throws IOException, InterruptedException {
        given()
                .log().all()
                .baseUri(baseUri)
                .contentType(CONTENT_TYPE)
                .body(newTodo)
                .when()
                .post(ENDPOINT)
                .then()
                .log().ifError();

        return getTodoById(newTodo.getId());
    }


    public static void deleteTodo(Integer todoId) {
        if (todoId == null) {
            throw new IllegalArgumentException("Todo ID cannot be null");
        }

         given()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .baseUri(baseUri)
                .when()
                .delete( ENDPOINT + todoId)
                .then()
                .log().ifError();

        System.out.println("Deleted Todo ID: " + todoId);
    }

    public static Todo getTodoById(int todoId) throws IOException, InterruptedException {
        List<Todo> todos = getTodos(0, 100);
        return todos.stream()
                .filter(todo -> todo.getId() == todoId)
                .findFirst()
                .orElse(null);
    }

    public static void deleteTodoWithInvalidAuth(Integer todoId) {
        given()
                .log().all()
                .auth().preemptive().basic("invalid_username", "invalid_password")
                .baseUri(baseUri)
                .when()
                .delete(String.format("%s%s", ENDPOINT, todoId))
                .then()
                .log().ifError()
                .statusCode(401);

        System.out.println("Attempted to delete with invalid credentials");
    }

    public static void cleaning() throws IOException, InterruptedException {
        int offset = 0;
        int limit = 100;
        List<Todo> todos;

        do {
            todos = getTodos(offset, limit);
            todos.forEach(task -> deleteTodo(task.getId()));
            offset += limit;
        } while (!todos.isEmpty());

        System.out.println("Cleaned up all todos");
    }
}
