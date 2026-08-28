package com.banking;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String msg){
        super(msg);
    }
}
