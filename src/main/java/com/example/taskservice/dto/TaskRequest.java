package com.example.taskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Request DTO used when creating or updating a task.
 */
public class TaskRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Task title is required.")
    @Size(max = 100, message = "Task title cannot exceed 100 characters.")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotNull(message = "User ID is required.")
    private Long userId;

    public TaskRequest() {
    }

    public TaskRequest(String title,
                       String description,
                       Long userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}