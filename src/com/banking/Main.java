package com.banking;

public class Main {

    public static void main(String [] args){
          Customer cust = new Customer(101, "Nikhil", "nikhilshirode45@gmail.com", "7709045180");
        System.out.println(cust.getCustomerEmail());
        System.out.println(cust.getCustomerId());
        System.out.println(cust.getCustomerName());
        System.out.println(cust.getMobileNumber());
    }

}
