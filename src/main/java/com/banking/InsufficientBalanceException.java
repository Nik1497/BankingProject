package com.banking;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String msg){
        super(msg);
    }
}
