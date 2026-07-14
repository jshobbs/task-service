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

        logger.info("Creating task for user ID {}.",
                taskRequest.getUserId());

        logger.info("Validating user ID {} with user-service.",
                taskRequest.getUserId());

        UserSummary user =
                userServiceClient.getUserById(taskRequest.getUserId());

        logger.info("User ID {} validated successfully.",
                user.getId());

        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());
        task.setCompleted(false);

        logger.debug("Saving new task to the database.");

        Task savedTask = taskRepository.save(task);

        logger.info("Task {} created successfully.",
                savedTask.getId());

        return mapToResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getAllTasks() {

        logger.info("Retrieving all tasks.");

        List<TaskResponse> tasks = taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Retrieved {} task(s).", tasks.size());

        return tasks;
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        logger.info("Retrieving task with ID {}.", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Task with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "Task not found with id: " + id);
                });

        logger.debug("Task with ID {} retrieved successfully.", id);

        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id,
                                   TaskRequest taskRequest) {

        logger.info("Updating task with ID {}.", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Task with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "Task not found with id: " + id);
                });

        logger.info("Validating user ID {} with user-service.",
                taskRequest.getUserId());

        userServiceClient.getUserById(taskRequest.getUserId());

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());

        logger.debug("Saving updated task with ID {}.", id);

        Task updatedTask = taskRepository.save(task);

        logger.info("Task {} updated successfully.", id);

        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {

        logger.info("Deleting task with ID {}.", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Task with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "Task not found with id: " + id);
                });

        logger.debug("Deleting task from the database.");

        taskRepository.delete(task);

        logger.info("Task {} deleted successfully.", id);
    }

    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {

        logger.info("Retrieving tasks for user ID {}.", userId);

        List<TaskResponse> tasks = taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Retrieved {} task(s) for user ID {}.",
                tasks.size(),
                userId);

        return tasks;
    }

    /**
     * Converts a Task entity into a TaskResponse DTO.
     */
    private TaskResponse mapToResponse(Task task) {

        logger.debug("Mapping Task entity with ID {} to TaskResponse DTO.",
                task.getId());

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getUserId()
        );
    }

}