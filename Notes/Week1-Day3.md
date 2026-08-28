# Week 1 --- Day 3

## 1. Why do we need a Bank class?

The Bank class manages a collection of accounts. This separates
account-management responsibilities from `Main` and follows separation
of concerns.

## 2. Collections in Java

Java Collections Framework provides ways to store and manage multiple
objects.

-   `List` --- ordered collection, accessed by index.
-   `Set` --- collection designed for unique elements.
-   `Map` --- stores key-value pairs.

## 3. Why use Map?

We use `Map<String, Account>` because the account number uniquely
identifies an account.

``` java
Map<String, Account> accounts;
```

Here: - `String` = account number/key - `Account` = account object/value

Example:

``` text
"ACC001" → Account A
"ACC002" → Account B
```

## 4. What is HashMap?

`HashMap` is a concrete implementation of the `Map` interface. It stores
key-value pairs and uses hashing internally for key-based lookup.

``` java
Map<String, Account> accounts = new HashMap<>();
```

## 5. Why `new HashMap<>()` has no arguments

`new HashMap<>()` creates an empty HashMap. It creates the collection,
not an Account.

The `<>` is the diamond operator. Java infers the generic types from the
context:

``` java
Map<String, Account> accounts = new HashMap<>();
```

is effectively:

``` java
Map<String, Account> accounts =
        new HashMap<String, Account>();
```

## 6. Why initialize the Map?

This declaration only creates a reference:

``` java
private Map<String, Account> accounts;
```

Initially the reference is `null`.

The constructor creates the actual Map:

``` java
public Bank() {
    accounts = new HashMap<>();
}
```

Without initialization, calling `accounts.put(...)` would cause a
`NullPointerException`.

## 7. Map vs HashMap

-   `Map` = interface
-   `HashMap` = implementation

We generally program to the interface:

``` java
Map<String, Account> accounts = new HashMap<>();
```

This keeps the code less coupled to one implementation and allows the
implementation to be changed later, for example to `TreeMap` or
`LinkedHashMap`.

### Interview answer

`Map` is an interface and `HashMap` is an implementation of that
interface. We declare the reference using the interface so our code
depends on the abstraction rather than a specific implementation.

## 8. Why account number is the Map key

The account number is unique for an account, so it is suitable as the
key.

``` text
Account Number → Account Object
```

This allows direct key-based retrieval.

## 9. Bank class and encapsulation

The Bank owns and manages the account collection:

``` java
private Map<String, Account> accounts;
```

It should remain private so other classes cannot directly modify the
collection and bypass Bank's validation or business rules.

## 10. `addAccount()`

We first had:

``` java
public void addAccount(Account account) {
    accounts.put(account.getAccountNumber(), account);
}
```

But `HashMap` does not throw an exception for duplicate keys. A second
`put()` with the same key replaces the previous value.

Therefore we added business validation:

``` java
public void addAccount(Account account) {
    if (accounts.containsKey(account.getAccountNumber())) {
        throw new DuplicateAccountException(
            account.getAccountNumber() + " already exists"
        );
    }

    accounts.put(account.getAccountNumber(), account);
}
```

## 11. DuplicateAccountException

``` java
public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String msg) {
        super(msg);
    }
}
```

It is an unchecked exception used for the duplicate-account business
validation.

## 12. AccountNotFoundException

``` java
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
```

It is used when a valid account number is supplied but no matching
account exists.

## 13. `getAccount()`

``` java
public Account getAccount(String accountNum) {

    if (accounts.containsKey(accountNum)) {
        return accounts.get(accountNum);
    }

    throw new AccountNotFoundException(
        accountNum + " Account number not found"
    );
}
```

Important:

``` java
accounts.containsKey(accountNum)
```

returns `true` or `false`.

``` java
accounts.get(accountNum)
```

returns the Account object or `null`.

They cannot be compared directly because they return different types.

## 14. Exception handling

The Bank defines and throws the business exception:

``` java
throw new AccountNotFoundException(...);
```

The caller can handle it:

``` java
try {
    Account acct = bank.getAccount("123456");
    System.out.println("Account Found");
} catch (AccountNotFoundException e) {
    System.out.println(e.getMessage());
}
```

This follows separation of concerns: the Bank defines the business
failure, while the caller decides how to handle it.

## 15. InvalidAccountException

We distinguished invalid input from a missing account.

`InvalidAccountException` means the input itself is invalid, such as:

``` java
bank.addAccount(null);
```

`AccountNotFoundException` means a valid account number was provided but
no matching account exists.

### Easy way to remember

``` text
InvalidAccount
→ Your input is wrong

AccountNotFound
→ Your input is valid,
  but we don't have that account
```

## Interview Questions & Answers

### Q1. Why do we need a Bank class?

The Bank class manages the collection of accounts. It separates
account-management responsibilities from Main and follows separation of
concerns.

### Q2. Why use Map?

Because the account number uniquely identifies an account and can be
used as the key to retrieve the Account object.

### Q3. What is HashMap?

HashMap is a concrete implementation of the Map interface that stores
key-value pairs and uses hashing for key-based lookup.

### Q4. Why `Map<String, Account>` instead of `HashMap<String, Account>`?

Map is the abstraction/interface and HashMap is the implementation.
Using the interface keeps the code flexible and follows programming to
an interface.

### Q5. Does HashMap throw an exception for duplicate keys?

No. A second `put()` using the same key replaces the existing value.

### Q6. Why do we need duplicate validation?

Because account numbers must be unique in a banking application. We
don't want a second account to silently replace an existing account.

### Q7. Difference between InvalidAccountException and AccountNotFoundException?

InvalidAccountException means the input itself is invalid, such as a
null account. AccountNotFoundException means a valid account number was
supplied but no matching account exists.

### Q8. Why keep the accounts Map private?

To prevent other classes from directly modifying the collection and
bypassing Bank's validation and business rules.

### Q9. What is programming to an interface?

Using an interface as the reference type and a concrete implementation
as the actual object, for example:

``` java
Map<String, Account> accounts = new HashMap<>();
```

This reduces coupling to a particular implementation.

## Day 3 Summary

Today we moved from managing a single Account directly in Main to
introducing a Bank that manages multiple accounts.

``` text
Main
 ↓
Bank
 ↓
Map<String, Account>
 ↓
HashMap
 ↓
Account objects
```

We also introduced business validation for:

-   Duplicate account
-   Account not found
-   Invalid/null account
-   Exception handling
-   Encapsulation
-   Programming to an interface
