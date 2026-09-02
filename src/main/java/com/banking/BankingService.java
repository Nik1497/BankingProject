package com.banking;

import java.math.BigDecimal;

public interface BankingService {
    default void greeting(){
        System.out.println("Hello! Welcome to the bank");
    }
    void transfer(String sourceAccount, String destinationAccount, BigDecimal amount);
}
