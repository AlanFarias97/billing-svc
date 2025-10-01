package com.acme.billing_svc.web.advice;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<?> circuitOpen(CallNotPermittedException ex) {
        return ResponseEntity.status(503).body(Map.of(
                "message","Circuit breaker abierto",
                "timestamp", OffsetDateTime.now().toString()
        ));
    }

    @ExceptionHandler(java.net.SocketTimeoutException.class)
    public ResponseEntity<?> timeout(java.net.SocketTimeoutException ex) {
        return ResponseEntity.status(504).body(Map.of("message","Timeout","ts", OffsetDateTime.now()));
    }
}