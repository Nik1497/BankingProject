# Banking Platform — Week 1 Interview Notes

## Week 1 Day 1 — Java OOP & Banking Domain Model

### Encapsulation

**My understanding:**  
By making fields private, other classes cannot directly change them. We use methods such as getters and controlled update methods to access or modify the data.

**Interview answer:**  
Encapsulation is the practice of keeping an object's state private and providing controlled access through methods. It prevents direct modification of internal data and allows validation/business rules to be applied when data changes.

### Why should fields be private?

Private fields prevent other classes from directly accessing or modifying an object's internal state. This allows controlled access and makes it possible to enforce validation and business rules.

### Why not make fields public?

Public fields allow unrestricted direct modification, which weakens encapsulation and makes business rules harder to enforce.

### Class vs Object

**My understanding:**  
`Customer` is a class and `cust` is an object created from that class.

```java
Customer cust = new Customer(...);
```

- `Customer` → class/type
- `cust` → reference variable
- `new Customer(...)` → creates the object

**Interview answer:**  
A class is a blueprint defining state and behavior; an object is a runtime instance of that class.

### What does `new` do?

`new` creates an object and invokes its constructor to initialize it.

### Purpose of `this`

`this` refers to the current object.

```java
this.customerId = customerId;
```

- `this.customerId` → instance field
- `customerId` → constructor parameter

### Why is mobileNumber a String?

A mobile number is an identifier, not a mathematical value. `String` also supports country codes, formatting, and leading zeros.

### Why is accountNumber a String?

An account number is an identifier, not a number used for calculations. It may contain leading zeros or become longer than an integer range.

### Why use enum for AccountType?

An enum provides a fixed, controlled set of valid values.

```java
public enum AccountType {
    SAVINGS,
    CURRENT
}
```

This avoids inconsistent strings such as `"saving"`, `"Savings"`, and `"SAVING"`.

### Why use BigDecimal for balance?

`BigDecimal` provides precise decimal arithmetic and is appropriate for financial calculations. `double` can introduce floating-point precision errors.

### Why does Account contain a Customer object?

**My understanding:**  
We already have a `Customer` class containing customer information. Instead of writing customer information again, we can directly use the existing `Customer` object.

```java
private Customer customer;
```

**Interview answer:**  
The account maintains a reference to the existing Customer object instead of duplicating customer information. This models the relationship between the account and its owner.

### Why not create setBalance()?

We don't want arbitrary code to directly change the balance because that could bypass banking business rules. Balance should be changed through controlled operations such as:

- `deposit()`
- `withdraw()`
- `transfer()`

---

# Week 1 Day 2 — BigDecimal, Deposit, Withdraw & Exceptions

## BigDecimal

### Why BigDecimal instead of double?

**My understanding:**  
BigDecimal is used for banking transactions because it provides precise decimal arithmetic, unlike `double`, which can produce floating-point precision errors.

**Interview answer:**  
`BigDecimal` provides precise decimal arithmetic, which is important for monetary calculations. `double` uses binary floating-point representation and can produce precision errors.

Example:

```text
double:
0.1 + 0.2 → 0.30000000000000004

BigDecimal:
0.1 + 0.2 → 0.3
```

### BigDecimal addition

Do not use:

```java
balance + amount;
```

Use:

```java
balance = balance.add(amount);
```

Other operations include:

```java
balance.add(amount);
balance.subtract(amount);
amount.multiply(rate);
amount.divide(rate);
```

### Is BigDecimal mutable?

No. `BigDecimal` is immutable.

```java
balance.add(amount);
```

does not change the existing object. It returns a new `BigDecimal`, so we normally write:

```java
balance = balance.add(amount);
```

### Why use the String constructor?

Prefer:

```java
new BigDecimal("10000.00");
```

over:

```java
new BigDecimal(10000.00);
```

The String constructor represents the specified decimal value exactly.

### How to compare BigDecimal?

Use:

```java
amount.compareTo(BigDecimal.ZERO)
```

For:

```java
amount.compareTo(balance)
```

- `< 0` → amount < balance
- `== 0` → amount == balance
- `> 0` → amount > balance

**Interview answer:**  
`compareTo()` returns a negative value, zero, or a positive value depending on whether the first BigDecimal is smaller, equal to, or greater than the second.

---

## Local vs Instance Variable

We initially made a mistake by creating:

```java
BigDecimal balance = new BigDecimal("0.0");
```

inside `deposit()`.

That created a local variable separate from the Account field:

```java
private BigDecimal balance;
```

The correct code is:

```java
balance = balance.add(amount);
```

A local variable with the same name as an instance field can also cause variable shadowing.

---

# Deposit

Implemented:

```java
public void deposit(BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException(
            "Deposit amount must be greater than zero"
        );
    }

    balance = balance.add(amount);
}
```

Validation:

```text
-500 → invalid
   0 → invalid
1000 → valid
```

---

# Exceptions

## What is an exception?

**My understanding:**  
If something goes wrong during execution, or validations are not satisfied, the application should communicate the problem so the user/application knows what went wrong.

**Interview answer:**  
An exception represents an abnormal condition during program execution. It communicates failures such as invalid input or business-rule violations instead of silently ignoring them.

An exception does not necessarily display a user-friendly message itself. A higher application layer can handle it and convert it into an appropriate response.

### Should invalid banking operations fail silently?

No.

A real banking application should communicate the failure to the caller, typically by throwing an appropriate exception.

---

## Custom Exceptions

### Why create InvalidAmountException separately?

Create:

```text
InvalidAmountException.java
```

rather than putting the exception inside `Account` or `Main`.

The exception can be reused by:

- `deposit()`
- `withdraw()`
- `transfer()`

Keeping it separate also follows separation of concerns.

### Why extend RuntimeException?

**My original reasoning:**  
The invalid amount occurs during execution.

**Refined interview answer:**  
`InvalidAmountException` represents a validation/business-rule violation that can generally be prevented by providing valid input. Therefore it is appropriate as an unchecked exception.

```java
public class InvalidAmountException extends RuntimeException {
}
```

The compiler does not force callers to catch or declare unchecked exceptions.

---

# Exception Hierarchy

```text
Throwable
│
├── Error
│   └── OutOfMemoryError
│
└── Exception
    │
    ├── Checked Exceptions
    │   └── IOException
    │
    └── RuntimeException
        ├── InvalidAmountException
        ├── InsufficientBalanceException
        └── other unchecked exceptions
```

## Error

`Error` represents serious problems generally not intended to be handled by normal application code.

Examples:

- `OutOfMemoryError`
- `StackOverflowError`

## Checked Exception

Checked exceptions extend `Exception` but not `RuntimeException`.

Examples:

- `IOException`
- `SQLException`

The compiler forces the caller to either handle or declare them.

## Unchecked Exception

Unchecked exceptions extend `RuntimeException`.

Examples:

- `NullPointerException`
- `IllegalArgumentException`
- `IndexOutOfBoundsException`
- `ArithmeticException`

The compiler does not force callers to catch or declare them.

### Important interview rule

Do not classify an exception as checked simply because it happens during execution. All exceptions occur at runtime. The reliable distinction is based on the inheritance hierarchy.

---

# Exception Classification Practice

| Scenario | Correct classification |
|---|---|
| File doesn't exist while reading a file | Checked Exception |
| Withdraw amount is greater than balance | Unchecked Exception |
| JVM runs out of memory | Error |
| Array index is outside its range | Unchecked Exception |

Final answers:

```text
A → Checked Exception
B → Unchecked Exception
C → Error
D → Unchecked Exception
```

---

# Exception Constructor

A constructor must not have a return type.

Incorrect:

```java
public void InvalidAmountException(String msg) {
}
```

Correct:

```java
public InvalidAmountException(String msg) {
    super(msg);
}
```

`super(msg)` passes the message to the parent `RuntimeException` constructor.

---

# Exception Propagation

When this happens:

```java
act.deposit(new BigDecimal("0"));

System.out.println("Transaction completed");
```

the flow is:

```text
deposit(0)
   ↓
validation fails
   ↓
throw InvalidAmountException
   ↓
current method stops
   ↓
exception propagates to caller
   ↓
if no handler exists
   ↓
program terminates
```

### My understanding

Code stops executing once the exception occurs.

### Interview answer

> When an exception is thrown and not handled at the current level, normal execution of the current method stops and the exception propagates up the call stack looking for a handler.

---

# try-catch

Example:

```java
try {
    act.deposit(new BigDecimal("0"));
    System.out.println("Transaction completed");
} catch (InvalidAmountException e) {
    System.out.println(e.getMessage());
}
```

The success message belongs inside `try` because it should execute only when the operation succeeds.

If an exception occurs, the remaining statements in the `try` block are skipped and control moves to the matching `catch`.

### Interview answer

> The try block contains the operation that may throw an exception and the statements that should execute only if that operation succeeds. The catch block handles the exception when the operation fails.

---

# Unhandled Exception and Exit Codes

When `InvalidAmountException` was thrown but only `InsufficientBalanceException` was caught, the exception was not handled.

We observed:

```text
Exception in thread "main" com.banking.InvalidAmountException
```

and:

```text
Process finished with exit code 1
```

### Why did InsufficientBalanceException appear normally?

Because the catch block specifically handled:

```java
catch (InsufficientBalanceException e)
```

When that exception was thrown, the matching catch block handled it and the program completed normally.

```text
exit code 0 → normal completion
exit code 1 → abnormal termination
```

### Interview answer

> A catch block handles a specific exception type. If the thrown exception does not match the catch block's type, it continues propagating up the call stack. If no matching handler is found, the exception remains uncaught and the program terminates.

---

# Withdraw

## Business rules

For:

```java
account.withdraw(amount);
```

we need:

1. Amount must be greater than zero.
2. Account must have sufficient balance.

Flow:

```text
withdraw(amount)
      ↓
amount <= 0?
      ↓
YES → InvalidAmountException
      ↓
NO
      ↓
amount > balance?
      ↓
YES → InsufficientBalanceException
      ↓
NO
      ↓
balance = balance.subtract(amount)
```

## Implemented withdraw()

```java
public void withdraw(BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException(
            "Withdrawal amount must be greater than zero"
        );
    }

    if (amount.compareTo(balance) > 0) {
        throw new InsufficientBalanceException(
            "Insufficient balance for withdrawal"
        );
    }

    balance = balance.subtract(amount);
}
```

### Test cases

If balance is ₹10,000:

```text
Withdraw ₹2,000
→ balance ₹8,000

Withdraw ₹0
→ InvalidAmountException

Withdraw ₹15,000
→ InsufficientBalanceException

Withdraw exactly ₹10,000
→ balance ₹0
```

Withdrawing the entire balance is valid.

---

# InsufficientBalanceException

Implemented:

```java
package com.banking;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
```

It is a separate reusable custom exception because insufficient balance is a banking business-rule violation that can occur in multiple operations.

---

# Separation of Concerns

We discussed why this is not ideal:

```java
public void withdraw(BigDecimal amount) {
    try {
        // withdrawal logic
    } catch (InsufficientBalanceException e) {
        System.out.println("Insufficient balance");
    }
}
```

### My understanding

`Account.withdraw()` is for defining the process, not for the end-user message. The same class can be used by different service layers, which may require different error handling.

### Interview answer

> `Account` should enforce the banking business rules and throw the appropriate exception. It shouldn't decide how the error is presented to the end user because the same domain logic may be used by different service or presentation layers.

Example:

```text
Mobile App
    ↓
REST API
    ↓
Banking Service
    ↓
Account.withdraw()
    ↓
InsufficientBalanceException
    ↓
Exception Handler
    ↓
HTTP 400
"Insufficient balance"
```

This is separation of concerns.

---

# Day 2 Interview Assessment

## Q1. Why BigDecimal?

**My answer:**  
BigDecimal is used for banking transactions because it provides precise decimal arithmetic, unlike double, which can produce floating-point precision errors.

**Interview answer:**  
`BigDecimal` provides precise decimal arithmetic, which is important for monetary calculations. `double` uses binary floating-point representation and can produce precision errors.

## Q2. What does compareTo() return?

**My answer:**  
It returns the comparison between two BigDecimal values.

**Interview answer:**  
`compareTo()` returns a negative value, zero, or a positive value depending on whether the first value is smaller, equal to, or greater than the second.

## Q3. Why RuntimeException?

**My answer:**  
`InvalidAmountException` is an unchecked exception. The compiler does not force it to be handled, hence it extends RuntimeException.

**Interview answer:**  
`InvalidAmountException` represents a validation/business-rule violation, so it is an unchecked exception. Extending `RuntimeException` means callers are not forced by the compiler to catch or declare it.

## Q4. What happens when there is no matching catch?

**My answer:**  
It propagated to the main method and resulted in exit code 1 with the exception shown in the console.

**Interview answer:**  
When an exception is thrown and no matching handler exists at the current level, it propagates up the call stack. If no matching handler is found, the exception remains uncaught and the program terminates abnormally.

## Q5. Why shouldn't Account handle the exception message?

**My answer:**  
`Account.withdraw()` is for defining the process, not for the end-user message. The same class can be used by different service layers, which may require different error handling.

**Interview answer:**  
`Account` should enforce the banking business rules and throw the appropriate exception. A higher layer such as a service, controller, or exception handler should decide how the failure is presented or processed.

---

# Week 1 Day 2 — Completion Checklist

- [x] BigDecimal fundamentals
- [x] BigDecimal precision
- [x] BigDecimal immutability
- [x] `add()`
- [x] `subtract()`
- [x] `compareTo()`
- [x] Local vs instance variables
- [x] Variable shadowing
- [x] `deposit()`
- [x] Deposit validation
- [x] `InvalidAmountException`
- [x] `withdraw()`
- [x] Withdrawal validation
- [x] `InsufficientBalanceException`
- [x] Checked exceptions
- [x] Unchecked exceptions
- [x] Errors
- [x] `RuntimeException`
- [x] Exception hierarchy
- [x] Exception constructors
- [x] `super(msg)`
- [x] Exception propagation
- [x] `try-catch`
- [x] Matching catch types
- [x] Unhandled exceptions
- [x] Exit code 0 vs exit code 1
- [x] Separation of concerns
- [x] Practical testing of successful and failed transactions

# Week 1 Day 2 Status

**COMPLETE ✅**

Next: **Week 1 Day 3**
