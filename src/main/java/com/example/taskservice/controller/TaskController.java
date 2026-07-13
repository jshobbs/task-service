package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/health")
    public String health() {
        return "Task Service is running!";
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest taskRequest) {

        return new ResponseEntity<>(
                taskService.createTask(taskRequest),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id) {

        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest taskRequest) {

        return ResponseEntity.ok(
                taskService.updateTask(id, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                taskService.getTasksByUserId(userId));
    }

}