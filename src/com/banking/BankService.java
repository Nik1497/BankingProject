package com.banking;

import java.math.BigDecimal;

public class BankService {

   private Bank bank;

    public BankService(Bank bank){

        this.bank = bank;
    }

    public void transfer(String sourceActNum, String destActNum, BigDecimal amount){
        Account source = bank.getAccount(sourceActNum);
        Account destination = bank.getAccount(destActNum);

        source.withdraw(amount);
        destination.deposit(amount);
    }
    
}
