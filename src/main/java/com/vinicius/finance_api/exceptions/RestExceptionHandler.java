package com.vinicius.finance_api.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> error404(EntityNotFoundException ex) {
        RestErrorMessage message = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage() );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<List<RequestDto>> error400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors()
                .stream()
                .map(RequestDto::new)
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<RestErrorMessage> error409(EmailAlreadyExistsException ex) {
        RestErrorMessage message = new RestErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    private record RequestDto(String field, String message) {
        public RequestDto (FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<RestErrorMessage> error401(BadCredentialsException ex) {
        RestErrorMessage message = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<RestErrorMessage> errorJwt(JWTVerificationException ex) {
        RestErrorMessage message = new RestErrorMessage(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<RestErrorMessage> error403(AccessDeniedException ex) {
        RestErrorMessage message = new RestErrorMessage(HttpStatus.FORBIDDEN, "Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }





}
