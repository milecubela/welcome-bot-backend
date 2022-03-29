package com.nsoft.welcomebot.ExceptionHandlers;


import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.ResponseModels.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;

import java.util.List;

// Global exception handler
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {


    // Handles all exceptions thrown by spring @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder string = new StringBuilder();
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors)
            string.append("Field ").append(error.getField()).append(" is invalid. ").append(error.getDefaultMessage()).append(". ");
        ApiError apiError = new ApiError(string.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Throw this exception when something doesn't exist in the database by ID lookup
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ApiError apiError = new ApiError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    // Throw this exception when Username is not found in Spring security context
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(UsernameNotFoundException ex) {
        ApiError apiError = new ApiError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = BadTokenException.class)
    public ResponseEntity<Object> handleBadTokenException(BadTokenException ex) {
        ApiError apiError = new ApiError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }
}
