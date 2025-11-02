package com.example.pets.infrastructure.web.error;

import com.example.pets.domain.exceptions.PetNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(PetNotFoundException ex, HttpServletRequest req) {
        List<String> details = List.of("Missing Pet ID: " + ex.getPetId());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        List<String> details = List.of(ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Invalid request", req.getRequestURI(), details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        List<String> details = List.of(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req.getRequestURI(), details);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String path, List<String> details) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );
        return ResponseEntity.status(status).body(body);
    }
}

