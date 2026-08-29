package com.banking;

import java.math.BigDecimal;

public class Main {

    public static void main(String [] args){
          Customer cust = new Customer(101, "Nikhil", "nikhilshirode45@gmail.com", "7709045180");
//        System.out.println(cust.getCustomerEmail());
//        System.out.println(cust.getCustomerId());
//        System.out.println(cust.getCustomerName());
//        System.out.println(cust.getMobileNumber());

        Account act = new Account("0000011110", new BigDecimal("1000"), AccountType.SAVINGS, cust );
        Account act1 = new Account(null, new BigDecimal("1000"), AccountType.SAVINGS, cust );
            Account act2 = new  Account("", new BigDecimal("1000"), AccountType.CURRENT, cust);
        //act.deposit(new BigDecimal("1000"));
//        try{
//            act.deposit(new BigDecimal("500"));
//            System.out.println("Transaction Completed!");
//        }catch(InvalidAmountException  e){
//            System.out.println(e.getMessage());
//        }

//        try{
//            act.withdraw(new BigDecimal("-2000"));
//            System.out.println("Transaction Completed !");
//        }catch(InsufficientBalanceException e){
//            System.out.println(e.getMessage());
//        }catch (InvalidAmountException e) {
//            System.out.println(e.getMessage());
//        }

           Bank bank = new Bank();
           try{
               bank.addAccount(act);
               bank.addAccount(act1);
           }catch(DuplicateAccountException e){
               System.out.println(e.getMessage());
           }catch(InvalidAccountException e){
               System.out.println(e.getMessage());
           }

        try{
            Account acct = bank.getAccount("12345");
            System.out.println(acct +"Account Found");
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());
        }catch(InvalidAccountException e){
            System.out.println(e.getMessage());
        }
        try{
            bank.removeAccount("");
            System.out.println("account removed");
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());
        }catch(InvalidAccountException e){
            System.out.println(e.getMessage());
        }

        try{
            bank.removeAccount("");
            System.out.println("account removed");
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());
        }catch(InvalidAccountException e){
            System.out.println(e.getMessage());
        }
    }
}
