package com.example.taskservice.client;

import com.example.taskservice.dto.UserSummary;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.exception.UserServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.ResourceAccessException;

@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(RestClient userServiceRestClient) {
        this.restClient = userServiceRestClient;
    }


    public UserSummary getUserById(Long userId) {

        try {

            return restClient
                    .get()
                    .uri("/users/{id}", userId)
                    .retrieve()
                    .body(UserSummary.class);

        }
        catch (HttpClientErrorException.NotFound ex) {

            throw new UserNotFoundException(
                    "User not found with id: " + userId
            );

        }
        catch (ResourceAccessException ex) {

            throw new UserServiceUnavailableException(
                    "User service is currently unavailable."
            );

        }

    }

}