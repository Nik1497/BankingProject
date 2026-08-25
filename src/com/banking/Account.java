package com.banking;

import java.math.BigDecimal;

public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private Customer customer;


    public Account(String accountNumber, BigDecimal balance, AccountType accountType, Customer customer){
        this.accountNumber= accountNumber;
        this.balance=balance;
        this.accountType = accountType;
        this.customer=customer;
    }
    public String getAccountNumber(){
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Customer getCustomer() {
        return customer;
    }
}
