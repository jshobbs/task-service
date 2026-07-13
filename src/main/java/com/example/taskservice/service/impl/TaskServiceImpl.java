package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.ResourceNotFoundException;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {

        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);

        return mapToResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUserId(taskRequest.getUserId());

        Task updatedTask = taskRepository.save(task);

        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {

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