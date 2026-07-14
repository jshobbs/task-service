package com.example.taskservice.service.impl;

import com.example.taskservice.client.UserServiceClient;
import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.dto.UserSummary;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.ResourceNotFoundException;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserServiceClient userServiceClient) {

        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {

        logger.info("Validating user with id {}", taskRequest.getUserId());

        UserSummary user =
                userServiceClient.getUserById(taskRequest.getUserId());

        logger.info("User {} {} found",
                user.getFirstName(),
                user.getLastName());

        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);

        logger.info("Task {} created successfully",
                savedTask.getId());

        return mapToResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getAllTasks() {

        logger.info("Retrieving all tasks");

        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        logger.info("Retrieving task {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id));

        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id,
                                   TaskRequest taskRequest) {

        logger.info("Updating task {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id));

        logger.info("Validating new user {}", taskRequest.getUserId());

        userServiceClient.getUserById(taskRequest.getUserId());

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());

        Task updatedTask = taskRepository.save(task);

        logger.info("Task {} updated", id);

        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {

        logger.info("Deleting task {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id));

        taskRepository.delete(task);

        logger.info("Task {} deleted", id);
    }

    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {

        logger.info("Retrieving tasks for user {}", userId);

        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Converts a Task entity into a TaskResponse DTO.
     */
    private TaskResponse mapToResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getUserId()
        );
    }

}