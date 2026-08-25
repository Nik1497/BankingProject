package com.banking;

import java.math.BigDecimal;

public class Main {

    public static void main(String [] args){
          Customer cust = new Customer(101, "Nikhil", "nikhilshirode45@gmail.com", "7709045180");
        System.out.println(cust.getCustomerEmail());
        System.out.println(cust.getCustomerId());
        System.out.println(cust.getCustomerName());
        System.out.println(cust.getMobileNumber());

        Account act = new Account("0000011110", new BigDecimal("1000"), AccountType.SAVINGS, cust );

        //act.deposit(new BigDecimal("1000"));
//        try{
//            act.deposit(new BigDecimal("500"));
//            System.out.println("Transaction Completed!");
//        }catch(InvalidAmountException  e){
//            System.out.println(e.getMessage());
//        }

        try{
            act.withdraw(new BigDecimal("-2000"));
            System.out.println("Transaction Completed !");
        }catch(InsufficientBalanceException e){
            System.out.println(e.getMessage());
        }catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        }
    }

}
