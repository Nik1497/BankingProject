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

    public void deposit(BigDecimal amount){

            if(amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidAmountException("Deposit amount must be greater than zero!");
            }
            balance =  balance.add(amount);
    }

    public void withdraw(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero!");
        }
        if(amount.compareTo(balance)>0){
            throw new InsufficientBalanceException("Insufficient Balance !");
        }
        balance = balance.subtract(amount);
    }
}
