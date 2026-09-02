package com.banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }
    //Keeping this  private because we do not want any other class to use this method
    private void validateAccountNumber(String acctNum){
        if(acctNum==null || acctNum.isBlank()){
            throw new InvalidAccountException("Account number cannot be null or blank");
        }
        if(acctNum.length()!=10){
            throw new InvalidAccountException("Account number should consist of 10 digits");
        }
        if(!acctNum.matches("\\d+")){
            throw new InvalidAccountException("Account number should consist only digits");
        }
    }

    public void addAccount(Account account) {
        String acctNum = account.getAccountNumber();
        validateAccountNumber(acctNum);
        if(accounts.containsKey(acctNum)){
            throw new DuplicateAccountException(acctNum+ " already exists");
        }
        accounts.put(acctNum, account);
    }

    public Account getAccount(String accountNum) {
        validateAccountNumber(accountNum);
        if (accounts.containsKey(accountNum)) {
            return accounts.get(accountNum);
        }
        throw new AccountNotFoundException(accountNum+ " Account number not found");
    }

    public void removeAccount(String accountNum){
        validateAccountNumber(accountNum);
        Account account= accounts.remove(accountNum);
        if(account==null){
            throw new AccountNotFoundException(accountNum+ " Account number not found");
        }
    }
}

