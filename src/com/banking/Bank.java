package com.banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        String acctNum = account.getAccountNumber();
        if(acctNum ==null){
            throw new InvalidAccountException("Account number cannot be null");
        }
        if(accounts.containsKey(acctNum)){
            throw new DuplicateAccountException(acctNum+ " already exists");
        }
        accounts.put(acctNum, account);
    }

    public Account getAccount(String accountNum) {
        if (accounts.containsKey(accountNum)) {
            return accounts.get(accountNum);
        }
        throw new AccountNotFoundException(accountNum+ " Account number not found");
    }
}

