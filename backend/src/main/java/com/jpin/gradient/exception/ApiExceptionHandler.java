package com.jpin.gradient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "NOT_FOUND");
        body.put("message", ex.getMessage());
        return body;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");
        body.put("fieldErrors", ex.getBindingResult().getFieldErrors().stream().map(fe -> {
            Map<String, Object> m = new HashMap<>();
            m.put("field", fe.getField());
            m.put("message", fe.getDefaultMessage());
            return m;
        }).toList());
        return body;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        String message = ex.getMessage();
        if (message != null && message.toLowerCase().contains("not found")) {
            body.put("error", "NOT_FOUND");
            body.put("message", message);
            // Override status to 404
            throw new ResourceNotFoundException(message);
        } else {
            body.put("error", "BAD_REQUEST");
            body.put("message", message != null ? message : "Invalid request");
        }
        return body;
    }
}
