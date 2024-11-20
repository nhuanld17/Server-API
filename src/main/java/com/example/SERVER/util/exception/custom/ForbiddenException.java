package com.example.SERVER.util.exception.custom;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super("Forbidden");
    }
    public ForbiddenException(String message){
        super(message);
    }
}
