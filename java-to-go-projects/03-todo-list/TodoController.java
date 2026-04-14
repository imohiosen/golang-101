package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final Map<Long, Todo> todos = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public static void main(String[] args) {
        SpringApplication.run(TodoController.class, args);
    }

    @GetMapping
    public Collection<Todo> getAll() {
        return todos.values();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return todos.get(id);
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        long id = counter.incrementAndGet();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todos.remove(id);
    }
}
