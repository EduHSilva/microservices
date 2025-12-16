package com.silva.edu.finances.infra.exceptions;

import com.edu.silva.common.DefaultResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(CustomExceptions.EntityNotFoundException.class)
    public ResponseEntity<@NonNull DefaultResponse> handleEntityNotFound(CustomExceptions.EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<@NonNull DefaultResponse> handleGeneric(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultResponse(ex.getMessage()));
    }
}