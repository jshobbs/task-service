package com.example.taskservice.exception;

import com.example.taskservice.dto.ErrorResponse;
import com.example.taskservice.dto.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles tasks that cannot be found in the task database.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    /**
     * Handles cases where user-service returns 404.
     *
     * Example:
     * POST /tasks with userId = 999
     * when user 999 does not exist.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError("User Not Found");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    /**
     * Handles communication failures with user-service.
     *
     * Example:
     * user-service is down.
     */
    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceUnavailableException(
            UserServiceUnavailableException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        response.setError(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }


    /**
     * Handles validation failures from @Valid annotations.
     *
     * Example:
     * Missing title
     * Missing userId
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ValidationError> validationErrors = new ArrayList<>();

        for (FieldError fieldError :
                ex.getBindingResult().getFieldErrors()) {

            validationErrors.add(
                    new ValidationError(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    )
            );
        }


        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation Failed");
        response.setMessage("One or more validation errors occurred.");
        response.setPath(request.getRequestURI());
        response.setFieldErrors(validationErrors);


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    /**
     * Catch-all handler for unexpected errors.
     *
     * This prevents stack traces and internal details
     * from being exposed to API consumers.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {


        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        response.setError(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        response.setMessage(
                "An unexpected error occurred."
        );
        response.setPath(
                request.getRequestURI()
        );


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}