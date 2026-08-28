package com.banking;

public class InvalidAccountException extends RuntimeException{
    public InvalidAccountException(String msg){
        super(msg);
    }
}
