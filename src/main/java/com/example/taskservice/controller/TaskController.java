package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/health")
    public String health() {

        logger.debug("Health check request received.");

        return "Task Service is running!";
    }


    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest taskRequest) {

        logger.info(
                "POST /tasks request received for user ID {}.",
                taskRequest.getUserId()
        );

        TaskResponse response =
                taskService.createTask(taskRequest);

        logger.info(
                "POST /tasks completed. Created task ID {}.",
                response.getId()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        logger.info("GET /tasks request received.");

        List<TaskResponse> tasks =
                taskService.getAllTasks();

        logger.info(
                "GET /tasks completed. Returning {} task(s).",
                tasks.size()
        );

        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id) {

        logger.info(
                "GET /tasks/{} request received.",
                id
        );

        TaskResponse response =
                taskService.getTaskById(id);

        logger.debug(
                "Returning task ID {}.",
                response.getId()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest taskRequest) {

        logger.info(
                "PUT /tasks/{} request received.",
                id
        );

        TaskResponse response =
                taskService.updateTask(id, taskRequest);

        logger.info(
                "PUT /tasks/{} completed successfully.",
                id
        );

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        logger.info(
                "DELETE /tasks/{} request received.",
                id
        );

        taskService.deleteTask(id);

        logger.info(
                "DELETE /tasks/{} completed successfully.",
                id
        );

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(
            @PathVariable Long userId) {

        logger.info(
                "GET /tasks/user/{} request received.",
                userId
        );

        List<TaskResponse> tasks =
                taskService.getTasksByUserId(userId);

        logger.info(
                "Returning {} task(s) for user ID {}.",
                tasks.size(),
                userId
        );

        return ResponseEntity.ok(tasks);
    }

}