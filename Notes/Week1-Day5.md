# Week 1 — Day 5

## Topics Covered

- Service layer
- `BankService`
- Dependency injection
- Separation of responsibilities
- Transfer operation
- Exception propagation
- Insufficient-balance handling
- Atomicity
- Exception handling vs rollback

---

## 1. Why do we need BankService?

Our application was becoming:

```text
Main
 ↓
Bank
 ↓
Account
```

Higher-level operations such as transfers involve multiple accounts, so we introduced:

```text
Main
 ↓
BankService
 ↓
Bank
 ↓
Account
```

### Responsibilities

```text
Customer
→ Stores customer information

Account
→ Owns account state and performs account-level operations
  such as deposit() and withdraw()

Bank
→ Manages the collection of Account objects
  and provides account lookup/add/remove operations

BankService
→ Coordinates higher-level business operations,
  such as transferring money between accounts
```

---

## 2. Why should transfer logic be in BankService?

A transfer is a higher-level business operation involving multiple accounts.

### My answer

> BankService will decide what to do and then assign Bank to do a specific task.

### Interview-ready answer

> The service layer coordinates business operations. For a transfer, `BankService` coordinates finding the accounts and executing the required operations, while `Bank` remains responsible for managing the account collection and `Account` remains responsible for account-level operations.

---

## 3. Account should protect its own state

We should NOT directly modify the balance from `BankService`.

Avoid:

```java
source.balance = source.balance.subtract(amount);
destination.balance = destination.balance.add(amount);
```

Instead use:

```java
source.withdraw(amount);
destination.deposit(amount);
```

This keeps balance-related rules inside `Account`.

### Principle

> An object should be responsible for protecting and changing its own state.

Therefore:

```text
BankService
→ coordinates the operation

Bank
→ manages/finds accounts

Account
→ manages its own balance
```

This demonstrates encapsulation and separation of responsibilities.

---

## 4. Creating BankService

`BankService` needs a `Bank` dependency:

```java
public class BankService {

    private Bank bank;

    public BankService(Bank bank) {
        this.bank = bank;
    }
}
```

We should NOT create another Bank inside `BankService`.

Incorrect:

```java
Bank bank = new Bank();
```

That could create a different Bank instance from the one containing our accounts.

Instead:

```java
Bank bank = new Bank();
BankService bankService = new BankService(bank);
```

Both now use the same Bank instance.

---

## 5. Dependency Injection

Passing `Bank` into the `BankService` constructor is a simple example of dependency injection.

The service says:

> I need a Bank to work.

The caller provides it.

This is preferable to making `BankService` create its own Bank.

---

## 6. Transfer operation

A transfer follows this sequence:

```text
BankService.transfer()
        ↓
Get source account
        ↓
Get destination account
        ↓
Withdraw from source
        ↓
Deposit into destination
```

Implementation:

```java
public void transfer(String sourceAccountNum,
                     String destinationAccountNum,
                     BigDecimal amount) {

    Account source = bank.getAccount(sourceAccountNum);
    Account destination = bank.getAccount(destinationAccountNum);

    source.withdraw(amount);
    destination.deposit(amount);
}
```

---

## 7. Why get both accounts first?

### My answer

> First we need to confirm whether the accounts exist or not.

Correct.

`BankService` reuses:

```java
bank.getAccount(...)
```

instead of directly accessing the Map.

This also reuses the existing Bank validations:

```text
Invalid account number
        ↓
InvalidAccountException

Valid number but account doesn't exist
        ↓
AccountNotFoundException
```

---

## 8. Why withdraw before deposit?

We call:

```java
source.withdraw(amount);
destination.deposit(amount);
```

not the other way around.

### Reason

If we deposited first and then withdrawal failed, money could be credited to the destination without being debited from the source.

Example:

```text
Source balance = ₹1,000
Transfer = ₹2,000
```

If deposit happened first:

```text
Destination → +₹2,000
Source withdrawal → FAIL
```

This would create an inconsistent financial state.

### My understanding

> If withdrawal fails and deposit already succeeded, then it is a blunder.

---

## 9. Exception propagation during transfer

If:

```java
source.withdraw(amount);
```

throws `InsufficientBalanceException`, then:

```java
destination.deposit(amount);
```

is never reached.

Flow:

```text
source.withdraw()
       ↓
InsufficientBalanceException
       ↓
normal execution stops
       ↓
destination.deposit()
       ↓
NOT executed
```

### My understanding

> If you are not withdrawing, how can you deposit?

### Technical explanation

When `withdraw()` throws an exception, normal sequential execution stops at that statement. Control moves to the nearest matching `catch` block, if one exists. Therefore the following `deposit()` statement is not executed.

---

## 10. Atomicity

A transfer contains two related operations:

```text
Debit source
+
Credit destination
```

If withdrawal succeeds but deposit fails, the system can become inconsistent.

A transfer should behave as one unit of work:

```text
Both succeed
    OR
all changes are rolled back
```

This property is called:

> Atomicity

### Simple definition

> Atomicity means the transaction happens completely or not at all.

Conceptually:

```text
Transfer
   ↓
Withdraw + Deposit
   ↓
Both succeed → COMMIT
Any failure   → ROLLBACK
```

Our current simple Java application does not yet implement database rollback.

Later, when we introduce Spring and database transactions, transaction management will provide proper commit/rollback behavior.

---

## 11. Exception handling vs rollback

These are not the same thing.

### Exception handling

Controls how the application responds when an exception occurs.

```text
Exception
 ↓
method execution stops
 ↓
caller can handle it
```

### Rollback

Reverses changes that were already applied as part of a transaction.

Therefore:

> An exception stopping Java execution does not automatically undo a previous successful operation.

For a real banking application, transaction management is required to guarantee atomicity across database changes.

---

# Interview Questions & Answers

## Q1. Why should transfer logic be in BankService?

Because transfer is a higher-level business operation involving multiple accounts. `BankService` coordinates the operation while `Bank` manages accounts and `Account` manages its own balance.

## Q2. Why shouldn't BankService directly modify Account balance?

It would bypass the business rules and validation inside `Account`. Account should protect and modify its own state through methods such as `withdraw()` and `deposit()`.

## Q3. Why pass Bank into BankService?

`BankService` needs Bank to find and manage accounts. Passing the existing Bank through the constructor ensures the service uses the same Bank instance rather than creating another one.

## Q4. What is dependency injection?

Providing an object's required dependency from outside instead of making the object create that dependency itself.

Example:

```java
Bank bank = new Bank();
BankService service = new BankService(bank);
```

## Q5. Why get both accounts before transferring?

We should first verify that both source and destination accounts exist before performing money movement.

## Q6. Why withdraw before deposit?

If deposit happened first and withdrawal failed, the destination could receive money without the source being debited. Therefore the source must successfully withdraw before the destination is credited.

## Q7. What happens if `withdraw()` throws `InsufficientBalanceException`?

Normal execution of the method stops at `withdraw()`. The following `deposit()` statement is not executed, and the exception propagates to the caller unless handled.

## Q8. What is atomicity?

Atomicity means a transaction is treated as one unit of work: either all required operations succeed or the changes are rolled back.

## Q9. Is exception handling the same as rollback?

No. Exception handling controls how the application responds to an exception. Rollback reverses already-applied transactional changes.

## Q10. What are the responsibilities of Customer, Account, Bank, and BankService?

```text
Customer
→ Customer information

Account
→ Account state and account-level operations

Bank
→ Account collection and account management

BankService
→ Higher-level business operations such as transfer
```

---

# Day 5 Architecture

```text
             Main
               ↓
          BankService
          ↙         ↘
       Bank        Account
        ↓             ↓
   Account Map    balance operations
                     ↓
              deposit / withdraw
```

Transfer:

```text
BankService.transfer()
        ↓
Bank.getAccount(source)
        ↓
Bank.getAccount(destination)
        ↓
source.withdraw(amount)
        ↓
destination.deposit(amount)
```

---

# Day 5 Status

- [x] Service layer
- [x] `BankService`
- [x] Dependency injection
- [x] Separation of responsibilities
- [x] Transfer operation
- [x] Reusing `Bank.getAccount()`
- [x] Reusing `Account.withdraw()`
- [x] Reusing `Account.deposit()`
- [x] Exception propagation
- [x] Insufficient balance during transfer
- [x] Atomicity concept
- [x] Difference between exception handling and rollback
- [x] Interview questions and answers

**Week 1 Day 5 — COMPLETE ✅**
