package org.example.application.exceptions;

public class UserTokenInvalidException extends RuntimeException{
    public UserTokenInvalidException(String message){
        super(message);
    }
}
