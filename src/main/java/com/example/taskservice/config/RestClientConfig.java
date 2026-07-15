package com.example.taskservice.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {


    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;


    @Value("${user-service.connect-timeout}")
    private int connectTimeout;


    @Value("${user-service.read-timeout}")
    private int readTimeout;


    @Bean
    public RestClient userServiceRestClient() {


        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectionRequestTimeout(
                                Timeout.ofMilliseconds(connectTimeout))
                        .setResponseTimeout(
                                Timeout.ofMilliseconds(readTimeout))
                        .build();


        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .build();


        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);


        return RestClient.builder()
                .baseUrl(userServiceBaseUrl)
                .requestFactory(factory)
                .build();
    }

}