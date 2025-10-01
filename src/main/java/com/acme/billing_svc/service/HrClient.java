package com.acme.billing_svc.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HrClient {
    private final RestClient client;
    public HrClient(RestClient client){ this.client = client; }

    @CircuitBreaker(name = "downstream-hr", fallbackMethod = "fallbackEmployee")
    @Retry(name = "downstream-hr")
    @Bulkhead(name = "downstream-hr", type = Bulkhead.Type.SEMAPHORE)
    public EmployeeDto getEmployee(Long id) {
        return client.get()
                .uri("http://hr-svc/employees/{id}", id)
                .retrieve()
                .body(EmployeeDto.class);
    }

    private EmployeeDto fallbackEmployee(Long id, Throwable ex) {
        return new EmployeeDto(id, "unknown", "unknown");
    }

    public record EmployeeDto(Long id, String firstName, String lastName) { }
}