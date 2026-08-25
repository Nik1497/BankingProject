# Banking Platform --- Interview Notes

## Week 1 --- Day 1: Java OOP & Banking Domain Model

### 1. What is Encapsulation?

**My understanding:**

By making fields private, other classes cannot directly change them. We
use methods such as getters and controlled update methods to access or
modify the data.

**Interview answer:**

Encapsulation is the practice of keeping an object's state private and
providing controlled access to that state through methods. It prevents
direct modification of internal data and allows us to apply validation
or business rules when the data changes.

------------------------------------------------------------------------

### 2. Why should fields be private?

Private fields prevent other classes from directly accessing or
modifying an object's internal state.

This allows us to control access and add validation or business rules
when values are changed.

------------------------------------------------------------------------

### 3. Why not make fields public?

Public fields allow unrestricted direct modification by code that has
access to the object. This weakens encapsulation and makes it harder to
enforce validation and business rules.

Private fields provide better control over how the object's state is
read or changed.

------------------------------------------------------------------------

### 4. What is the difference between a class and an object?

**My understanding:**

`Customer` is a class, and we created an object from that class called
`cust`.

**Interview answer:**

A class is a blueprint that defines the state and behavior of an object,
while an object is a runtime instance of that class.

``` java
Customer cust = new Customer(...);
```

-   `Customer` → class/type
-   `cust` → reference variable
-   `new Customer(...)` → creates the object

------------------------------------------------------------------------

### 5. What does the `new` keyword do?

**My understanding:**

`new` helps to create an object from a class.

**Interview answer:**

The `new` keyword creates an object and invokes its constructor to
initialize that object.

------------------------------------------------------------------------

### 6. What is the purpose of `this`?

`this` refers to the current object.

``` java
this.customerId = customerId;
```

-   `this.customerId` → object's field
-   `customerId` → constructor parameter

------------------------------------------------------------------------

### 7. Why is `mobileNumber` a `String` instead of an `int`?

A mobile number is an identifier, not a value used for arithmetic.

`String` also supports country codes such as `+919876543210` and avoids
issues with leading zeros and formatting.

------------------------------------------------------------------------

### 8. Why is `accountNumber` a `String` instead of an `int`?

An account number is an identifier, not a number used for mathematical
calculations.

It may be long or contain leading zeros, so `String` is more
appropriate.

------------------------------------------------------------------------

### 9. Why use an enum for `AccountType` instead of String?

An enum provides a fixed and controlled set of valid values.

``` java
public enum AccountType {
    SAVINGS,
    CURRENT
}
```

This prevents inconsistent values such as `"saving"`, `"Savings"`, and
`"SAVING"`.

------------------------------------------------------------------------

### 10. Why use `BigDecimal` for account balance?

`BigDecimal` provides precise decimal arithmetic and is appropriate for
financial calculations.

We should avoid `double` for monetary values because floating-point
arithmetic can introduce precision errors.

``` java
private BigDecimal balance;
```

------------------------------------------------------------------------

### 11. Why does `Account` contain a `Customer` object?

**My understanding:**

We already have a `Customer` class containing customer information.
Instead of writing the customer information again, we can directly use
the existing `Customer` object.

**Interview answer:**

Instead of duplicating customer information inside `Account`, the
account can maintain a reference to the existing `Customer` object.

``` java
private Customer customer;
```

This models the relationship between the account and its owner.

------------------------------------------------------------------------

### 12. Why not store only `customerId` inside Account?

At our current plain Java object-model stage, a `Customer` reference
directly represents the relationship between the account and its owner.

In a real database application, we will also use a customer ID/foreign
key to represent the relationship efficiently. This will be revisited
with JPA/Hibernate.

------------------------------------------------------------------------

### 13. Why didn't we create `setBalance()`?

We don't want arbitrary code to directly change the account balance.

For example:

``` java
account.setBalance(new BigDecimal("999999"));
```

could bypass banking business rules.

Instead, balance changes should happen through controlled operations
such as:

-   `deposit()`
-   `withdraw()`
-   `transfer()`

These operations can validate transactions and enforce business rules.

------------------------------------------------------------------------

## Day 1 Project Structure

``` text
BankingProject
│
├── src
│   └── com.banking
│       ├── Customer.java
│       ├── Account.java
│       ├── AccountType.java
│       └── Main.java
│
├── interview-notes.md
└── .gitignore
```

------------------------------------------------------------------------

## Day 1 Concepts Covered

-   Java 21
-   Classes and objects
-   Object creation with `new`
-   Reference variables
-   Constructors
-   `this` keyword
-   Encapsulation
-   Private fields
-   Getter methods
-   Enum
-   String for identifiers
-   BigDecimal for monetary values
-   Object association
-   Basic banking domain modelling
-   Git and GitHub workflow

------------------------------------------------------------------------

## Day 1 Git Concepts

``` bash
git status
git add .
git commit -m "message"
git push
```

Important distinction:

``` text
git commit
    ↓
Saves changes to local Git history

git push
    ↓
Sends commits to GitHub
```

------------------------------------------------------------------------

## Day 1 Practical Work Completed

-   Java 21 verified
-   IntelliJ project created
-   Git repository created
-   Git identity configured
-   GitHub repository connected
-   Project pushed to GitHub
-   `Customer` class created
-   Encapsulation implemented
-   Constructor implemented
-   Getters implemented
-   `Customer` object created and tested
-   `Account` class created
-   `AccountType` enum created
-   `BigDecimal` selected for balance
-   `Customer` relationship added to `Account`
-   Account constructor implemented
-   Account getters implemented

------------------------------------------------------------------------

## Day 1 Status

**COMPLETE**

Transaction logic was intentionally left for Day 2.

------------------------------------------------------------------------

## Day 2 Preview

-   Deposit
-   BigDecimal arithmetic
-   Input validation
-   Withdrawal
-   Custom exceptions
-   Bank class
-   `Map<String, Account>`
-   Fund transfer
-   Additional interview questions
