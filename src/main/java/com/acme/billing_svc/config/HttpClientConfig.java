package com.acme.billing_svc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000); // ms
        factory.setReadTimeout(2000);    // ms
        return builder.requestFactory(factory).build();
    }
}