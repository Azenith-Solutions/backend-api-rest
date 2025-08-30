package com.azenithsolutions.backendapirest.v2.infrastructure.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ControllerHandler extends RuntimeException{
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalException(IllegalArgumentException exception){
        Map<String, String> error = Map.of("error", "Ocorreu um erro de argumento inváldo: %s".formatted(exception.getMessage()));
        return ResponseEntity.status(400).body(error);
    }
}
