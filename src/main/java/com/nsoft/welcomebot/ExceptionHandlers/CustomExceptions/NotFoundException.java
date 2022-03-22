package com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage){
        // Creates new RuntimeException with provided string
        super(errorMessage);
    }
}