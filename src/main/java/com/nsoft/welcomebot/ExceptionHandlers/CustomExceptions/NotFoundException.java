package com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage){
        super(errorMessage);
    }
}