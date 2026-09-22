package com.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamPracticeTest {

    StreamPractice stream;
    Account account1;
    Account account2;
    Account account3;
    Customer test1;
    Bank bank = new Bank();

    @BeforeEach
    void setup() {
        test1 = new Customer(1, "test1", "test1@gmail.com", "1234567890");
        account1 = new Account("8888800001", new BigDecimal("10000"), AccountType.SAVINGS, test1);
        account2 = new Account("8888800002", new BigDecimal("10000"), AccountType.CURRENT, test1);
        account3 = new Account("8888800003", new BigDecimal("3000"), AccountType.SAVINGS, test1);
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        stream = new StreamPractice(bank);
    }
    @Test
    public void  findLowestSavingBalanceTest(){
        Optional<BigDecimal> result = stream.findLowestSavingsBalance();
        assertEquals(Optional.of(new BigDecimal("3000")), result);
    }

    @Test
    public void  findHighestCurrentBalanceTest(){
        Optional<BigDecimal> result = stream.findHighestCurrentBalance();
        assertEquals(Optional.of(new BigDecimal("1000")), result);
    }

    @Test
    public void  findHighestSavingsAccountTest(){
        Optional<Account> result = stream.findHighestSavingsAccount();
        assertEquals(Optional.of(account1), result);
    }

    @Test
    public void  sortAccountsByBalanceTest(){
        List<Account> result = stream.sortAccountsByBalance();
        assertEquals(new BigDecimal("1000"), result.get(0).getBalance());
        assertEquals(new BigDecimal("3000"), result.get(1).getBalance());
        assertEquals(new BigDecimal("10000"), result.get(2).getBalance());
    }

    @Test
    public void  sortAccountsByBalanceReversedTest(){
        List<Account> result = stream.sortAccountsByBalanceReversed();
        assertEquals(new BigDecimal("10000"), result.get(0).getBalance());
        assertEquals(new BigDecimal("3000"), result.get(1).getBalance());
        assertEquals(new BigDecimal("1000"), result.get(2).getBalance());
    }

    @Test
    public void  sortAccountsByBalanceThenAccountNumTest(){
        List<Account> result = stream.sortAccountsByBalanceThenAccountNum();
        System.out.println(result.get(0).getAccountNumber());
        assertEquals(new BigDecimal("3000"), result.get(0).getBalance());
        System.out.println(result.get(1).getAccountNumber());
        assertEquals(new BigDecimal("10000"), result.get(1).getBalance());
//        assertEquals(new BigDecimal("10000"), result.get(1).getBalance());
//        assertEquals(new BigDecimal("3000"), result.get(2).getBalance());
    }



}
