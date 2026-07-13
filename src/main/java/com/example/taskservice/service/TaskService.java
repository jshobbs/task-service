package com.example.taskservice.service;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;

import java.util.List;

public interface TaskService {

    /**
     * Create a new task.
     *
     * @param taskRequest Task information
     * @return Created task
     */
    TaskResponse createTask(TaskRequest taskRequest);

    /**
     * Retrieve all tasks.
     *
     * @return List of tasks
     */
    List<TaskResponse> getAllTasks();

    /**
     * Retrieve a task by ID.
     *
     * @param id Task ID
     * @return Task
     */
    TaskResponse getTaskById(Long id);

    /**
     * Update an existing task.
     *
     * @param id Task ID
     * @param taskRequest Updated task information
     * @return Updated task
     */
    TaskResponse updateTask(Long id, TaskRequest taskRequest);

    /**
     * Delete a task.
     *
     * @param id Task ID
     */
    void deleteTask(Long id);

    /**
     * Retrieve all tasks assigned to a specific user.
     *
     * @param userId User ID
     * @return List of tasks
     */
    List<TaskResponse> getTasksByUserId(Long userId);

}