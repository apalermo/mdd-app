package com.openclassroom.mddapi.advice;

import com.openclassroom.mddapi.exceptions.BadRequestException;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.exceptions.ForbiddenException;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Un conflit de données est survenu sur le serveur."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "La ressource demandée est introuvable.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Accès refusé.";
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validEx) {
            String validationError = validEx.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + " : " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(validationError));
        }

        if (ex instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Requête invalide."));
    }

    public record ErrorResponse(String message) {
    }
}