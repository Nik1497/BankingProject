package com.banking;

public class Customer {
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String mobileNumber;


    public Customer(int customerId, String customerName, String customerEmail, String mobileNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.mobileNumber = mobileNumber;
    }
}
