package com.example.taskservice.client;

import com.example.taskservice.dto.UserSummary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(RestClient userServiceRestClient) {
        this.restClient = userServiceRestClient;
    }

    public UserSummary getUserById(Long userId) {

        return restClient
                .get()
                .uri("/users/{id}", userId)
                .retrieve()
                .body(UserSummary.class);
    }

}