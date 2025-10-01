package com.acme.billing_svc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class AuthForwardingConfig {

    @Bean
    RestClient.Builder restClientBuilderWithAuth() {
        return RestClient.builder().requestInterceptor((request, body, execution) -> {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                var auth = attrs.getRequest().getHeader("Authorization");
                if (auth != null) {
                    request.getHeaders().set("Authorization", auth);
                }
            }
            return execution.execute(request, body);
        });
    }
}
