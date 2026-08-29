# Week 1 — Day 4

## Topics Covered

- `removeAccount()`
- `Map.remove()` return value
- `null` handling
- `InvalidAccountException`
- Reusable validation method
- DRY — Don't Repeat Yourself
- `isBlank()` vs `isEmpty()`
- Account-number length and format validation
- Why `accountNumber` is a `String`
- Leading zeros
- Interview questions and answers

## 1. removeAccount()

The Bank should manage account removal.

Initial logic:

```text
removeAccount(accountNumber)
        ↓
Does account exist?
    ↓          ↓
   YES         NO
    ↓           ↓
 remove()    throw AccountNotFoundException
```

Improved implementation using the return value of `remove()`:

```java
public void removeAccount(String accountNum) {
    Account account = accounts.remove(accountNum);

    if (account == null) {
        throw new AccountNotFoundException(
            accountNum + " Account number not found"
        );
    }
}
```

`Map.remove(key)` removes the mapping and returns the previous value. If there was no mapping, it returns `null`.

This avoids doing both `containsKey()` and `remove()`.

## 2. Invalid Account Input

If:

```java
bank.addAccount(null);
```

we should validate before calling:

```java
account.getAccountNumber();
```

Otherwise a `NullPointerException` could occur.

We use `InvalidAccountException` for invalid account input.

## 3. InvalidAccountException vs AccountNotFoundException

### InvalidAccountException

The input itself is invalid.

Examples:
- account object is `null`
- account number is missing
- account number is blank
- account number has an invalid format

Meaning:

> Your input is wrong.

### AccountNotFoundException

A valid account number was supplied, but no matching account exists.

Meaning:

> Your input is valid, but we don't have that account.

## 4. Reusable Account Number Validation

We avoid duplicating validation logic by creating:

```java
private void validateAccountNumber(String accountNum) {
    if (accountNum == null || accountNum.isBlank()) {
        throw new InvalidAccountException(
            "Account number cannot be null or blank"
        );
    }
}
```

This follows:

> DRY — Don't Repeat Yourself

Then `getAccount()` and `removeAccount()` can call the same validation method.

The helper is `private` because it is an internal method of `Bank`.

## 5. isBlank() vs isEmpty()

```java
"".isEmpty()
```

returns `true`.

But:

```java
"   ".isEmpty()
```

returns `false` because spaces are characters.

Whereas:

```java
"   ".isBlank()
```

returns `true`.

Therefore `isBlank()` is useful when whitespace-only user input should be considered invalid.

## 6. Account Number Format

For our project we decided:

```text
✓ Cannot be null
✓ Cannot be blank
✓ Exactly 10 characters
✓ Only digits
✗ No letters
✗ No spaces
```

Examples:

```text
"0000011110" → valid
"1234567890" → valid
"123456789"  → invalid
"12345678901" → invalid
"ABC1234567" → invalid
"12345 6789" → invalid
```

Validation:

```java
if (accountNum.length() != 10) {
    throw new InvalidAccountException(
        "Account number must contain exactly 10 digits"
    );
}

if (!accountNum.matches("\d+")) {
    throw new InvalidAccountException(
        "Account number must contain only digits"
    );
}
```

## 7. Why accountNumber is String

### My answer

> We will keep account number as a String because we are not performing any mathematical operations on it. It is used only for identity purpose, and using int will remove its leading zeroes, which is not acceptable.

### Interview-ready answer

> We keep `accountNumber` as a `String` because we don't perform mathematical operations on it; it is used only as an identifier. Using `int` or `long` can remove leading zeros, which are significant in an account identifier. `String` also lets us validate the required format easily.

Example:

```java
String accountNumber = "0000011110";
```

The leading zeros remain part of the identifier.

## 8. Account Number as Map Key

We use the account number as the key because it uniquely identifies an account.

```java
Map<String, Account> accounts;
```

Conceptually:

```text
Account Number → Account Object

"0000011110" → Account A
"0000011111" → Account B
"0000011112" → Account C
```

## Interview Questions & Answers

### Q1. Why should accountNumber be a String instead of int or long?

Because it is an identifier, not a value used for mathematical operations. Numeric types can lose leading zeros, and String also makes format validation easier.

### Q2. What happens when HashMap.put() receives a duplicate key?

The second value replaces the existing value. HashMap does not automatically throw an exception for duplicate keys.

### Q3. Why do we need DuplicateAccountException?

Account numbers must be unique in a banking application. We should not allow a second account to silently replace an existing account.

### Q4. What does Map.remove() return?

It removes the mapping for the supplied key and returns the previous value. If there was no mapping, it returns `null`.

### Q5. Why can removeAccount() use the return value of remove()?

A non-null returned Account means a mapping existed and was removed. A null return means no account was associated with that key.

### Q6. Difference between InvalidAccountException and AccountNotFoundException?

`InvalidAccountException` means the input itself is invalid. `AccountNotFoundException` means a valid account number was supplied but no matching account exists.

### Q7. Why create validateAccountNumber()?

To avoid duplicated validation logic and follow the DRY principle. If the account-number rules change, we update one method.

### Q8. Why is validateAccountNumber() private?

It is an internal helper method of `Bank`; external classes only need the actual banking operations.

### Q9. Difference between isBlank() and isEmpty()?

`isEmpty()` checks whether the string has zero characters. `isBlank()` also considers whitespace-only strings blank.

## Day 4 Flow

```text
Bank
│
├── private Map<String, Account> accounts
│
├── addAccount()
│     ├── validate account
│     ├── validate account number
│     └── duplicate validation
│
├── getAccount()
│     ├── validate account number
│     └── AccountNotFoundException
│
└── removeAccount()
      ├── validate account number
      └── AccountNotFoundException
```

## Status

**Week 1 Day 4 — COMPLETE ✅**
