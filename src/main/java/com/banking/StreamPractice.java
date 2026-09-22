package com.banking;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

public class StreamPractice {
     private Bank bank;
    public StreamPractice(Bank bank){
        this.bank =  bank;
    }

    public Optional<BigDecimal> findLowestSavingsBalance(){

        Predicate<Account> savings =account->account.getAccountType()==AccountType.SAVINGS;
        Optional <BigDecimal> balance = bank.getAccounts().stream().filter(savings).map(acct->acct.getBalance()).min(Comparator.naturalOrder());
        return balance;
    }

    public Optional<BigDecimal> findHighestCurrentBalance(){
        Predicate<Account> current =account->account.getAccountType()==AccountType.CURRENT;
        Optional <BigDecimal> balance = bank.getAccounts().stream().filter(current).map(acct->acct.getBalance()).min(Comparator.naturalOrder());
        return balance;
    }

    public Optional<Account> findHighestSavingsAccount(){
        Predicate <Account>  saving = account->account.getAccountType()==AccountType.SAVINGS;
        Optional<Account> results = bank.getAccounts().stream().filter(saving).max(Comparator.comparing(act->act.getBalance()));
    return results;
    }

    public List<Account> sortAccountsByBalance(){
        List<Account> lists = bank.getAccounts().stream().sorted(Comparator.comparing(Account::getBalance)).toList();
        return lists;
    }

    public List<Account> sortAccountsByBalanceReversed(){
        List<Account> lists = bank.getAccounts().stream().sorted(Comparator.comparing(Account::getBalance).reversed()).toList();
        return lists;
    }

    public List<Account> sortAccountsByBalanceThenAccountNum(){
        List<Account> lists = bank.getAccounts().stream().sorted(Comparator.comparing(Account::getBalance).thenComparing(Account::getAccountNumber)).toList();
        return lists;
    }
}
