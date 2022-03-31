package com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadTokenException extends RuntimeException{
    private final String message;
}
