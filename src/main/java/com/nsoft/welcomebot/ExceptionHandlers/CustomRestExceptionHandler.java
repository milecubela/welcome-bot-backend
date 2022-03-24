package com.nsoft.welcomebot.ExceptionHandlers;

import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;
import com.nsoft.welcomebot.Models.ResponseModels.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Global exception handler
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

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
