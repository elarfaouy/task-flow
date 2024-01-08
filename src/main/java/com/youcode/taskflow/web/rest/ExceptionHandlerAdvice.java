package com.youcode.taskflow.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleCustomValidationException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("message", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleCustomValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation Failed");
        response.put("errors", new HashMap<>());

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Object errors = response.get("errors");
            errors = ((Map<String, Object>) errors).put(error.getField(), error.getDefaultMessage());
        }


        return ResponseEntity
                .badRequest()
                .body(response);
    }
}
