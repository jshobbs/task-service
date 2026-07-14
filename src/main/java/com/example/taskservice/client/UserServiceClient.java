package com.example.taskservice.client;

import com.example.taskservice.dto.UserSummary;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.exception.UserServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class UserServiceClient {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceClient.class);

    private final RestClient restClient;

    public UserServiceClient(RestClient userServiceRestClient) {
        this.restClient = userServiceRestClient;
    }

    public UserSummary getUserById(Long userId) {

        logger.info("Calling user-service to retrieve user with ID {}.", userId);

        try {

            UserSummary user = restClient
                    .get()
                    .uri("/users/{id}", userId)
                    .retrieve()
                    .body(UserSummary.class);

            logger.info("Successfully retrieved user {} from user-service.", userId);

            return user;

        } catch (HttpClientErrorException.NotFound ex) {

            logger.warn("User-service returned 404 for user ID {}.", userId);

            throw new UserNotFoundException(
                    "User not found with id: " + userId,
                    ex
            );

        } catch (ResourceAccessException ex) {

            logger.error("Unable to communicate with user-service.", ex);

            throw new UserServiceUnavailableException(
                    "User service is currently unavailable.",
                    ex
            );

        }
    }

}