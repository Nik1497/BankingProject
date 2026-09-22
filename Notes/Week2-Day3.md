# Banking Platform — Interview Notes

# Week 2 — Day 3: Comparator, Sorting & Finding Min/Max

## 1. Day 3 Objective

Today we continued working with the Java Stream API.

The main focus was:

```text
Stream
   ↓
Comparator
   ↓
sorted()
   ↓
min()
   ↓
max()
   ↓
comparing()
   ↓
reversed()
   ↓
thenComparing()
```

The important goal was not just memorizing methods, but understanding:

> What type is flowing through the Stream at every step?

This became especially important when working with `Stream<Account>` versus `Stream<BigDecimal>`.

---

## 2. What is Comparator?

A `Comparator` is used to define how two objects should be compared for ordering.

```java
import java.util.Comparator;
```

For example:

```text
Account A → balance = 5000
Account B → balance = 3000
```

If we want to sort them by balance:

```text
3000
5000
```

Java needs to know how to compare two `Account` objects. That comparison logic is provided by a `Comparator`.

---

## 3. Comparator vs Comparable

### Comparable

`Comparable` defines the object's natural ordering.

```java
class Employee implements Comparable<Employee>
```

The class itself defines how it should normally be compared.

### Comparator

`Comparator` allows us to define external/custom ordering.

```java
Comparator<Account> byBalance =
        Comparator.comparing(Account::getBalance);
```

The `Account` class does not need to know about this particular sorting requirement.

The same object can therefore be sorted in multiple ways:

```text
Account by balance
Account by account number
Account by customer name
Account by account type
```

---

## 4. sorted()

`sorted()` is an intermediate Stream operation.

```java
stream.sorted()
```

It returns another Stream.

```text
sorted()
    ↓
Intermediate operation
```

It does not immediately produce the final result.

---

## 5. Natural Ordering

For values that already have a natural ordering:

```java
Comparator.naturalOrder()
```

For example:

```java
Stream<BigDecimal>
```

can be sorted using:

```java
.sorted(Comparator.naturalOrder())
```

For numbers:

```text
1000
2000
3000
5000
```

This is ascending order.

---

## 6. Reverse Natural Ordering

For descending order:

```java
Comparator.reverseOrder()
```

Example:

```java
.sorted(Comparator.reverseOrder())
```

Result:

```text
5000
3000
2000
1000
```

Therefore:

```java
Comparator.naturalOrder()
```

means ascending, while:

```java
Comparator.reverseOrder()
```

means descending.

---

## 7. Sorting Accounts by Balance

Our stream contains:

```java
Stream<Account>
```

An `Account` itself does not directly have a natural numeric ordering based on balance.

So we tell Java:

> Compare Accounts using their balance.

```java
Comparator.comparing(Account::getBalance)
```

Therefore:

```java
bank.getAccounts().stream()
    .sorted(Comparator.comparing(Account::getBalance))
    .toList();
```

The flow is:

```text
Collection<Account>
       ↓
stream()
       ↓
Stream<Account>
       ↓
sorted(...)
       ↓
Stream<Account>
       ↓
toList()
       ↓
List<Account>
```

---

## 8. Why Comparator.comparing()?

Suppose we have:

```java
Account
```

containing:

```java
private BigDecimal balance;
```

We want to compare Accounts based on:

```java
account.getBalance()
```

So:

```java
Comparator.comparing(Account::getBalance)
```

means:

> Create a Comparator for Account objects by comparing the value returned from `getBalance()`.

---

## 9. Lambda Version vs Method Reference

Lambda form:

```java
Comparator.comparing(account -> account.getBalance())
```

Method-reference form:

```java
Comparator.comparing(Account::getBalance)
```

These are effectively equivalent:

```java
account -> account.getBalance()
```

and:

```java
Account::getBalance
```

The method reference is shorter and readable.

---

## 10. IntelliJ Type Inference Issue

Sometimes this:

```java
Comparator.comparing(account -> account.getBalance())
```

can result in `account` being inferred as `Object`.

One solution is:

```java
Comparator.comparing((Account account) -> account.getBalance())
```

But the cleaner solution in our case was:

```java
Comparator.comparing(Account::getBalance)
```

### Interview takeaway

Method references can sometimes make generic type inference clearer and the code cleaner.

---

## 11. Sorting Accounts — Ascending Balance

Our method:

```java
public List<Account> sortAccountsByBalance() {
    return bank.getAccounts().stream()
            .sorted(Comparator.comparing(Account::getBalance))
            .toList();
}
```

Suppose:

```text
Account 101 → ₹5000
Account 102 → ₹1000
Account 103 → ₹3000
```

Result:

```text
Account 102 → ₹1000
Account 103 → ₹3000
Account 101 → ₹5000
```

Important:

The result is still:

```java
List<Account>
```

We are sorting Account objects, not converting them into balances.

---

## 12. Important Difference: map() vs sorted()

### Using map()

```java
.map(Account::getBalance)
```

changes:

```text
Stream<Account>
```

into:

```text
Stream<BigDecimal>
```

### Using sorted()

```java
.sorted(Comparator.comparing(Account::getBalance))
```

keeps:

```text
Stream<Account>
```

The order changes, but the element type does not.

Therefore:

```text
map()
→ transforms elements

sorted()
→ changes element ordering
```

---

## 13. Sorting Accounts — Descending Balance

```java
public List<Account> sortAccountsByBalanceDescending() {
    return bank.getAccounts().stream()
            .sorted(
                Comparator.comparing(Account::getBalance)
                          .reversed()
            )
            .toList();
}
```

Example:

```text
5000
3000
1000
```

instead of:

```text
1000
3000
5000
```

---

## 14. reversed()

`reversed()` reverses the ordering of an existing Comparator.

```java
Comparator.comparing(Account::getBalance)
```

means:

```text
Ascending balance
```

Then:

```java
.reversed()
```

changes it to:

```text
Descending balance
```

So:

```java
Comparator.comparing(Account::getBalance)
          .reversed()
```

means:

> Compare Accounts by balance in descending order.

---

## 15. Comparator.naturalOrder() vs comparing()

If we already converted Accounts into balances:

```java
.map(Account::getBalance)
```

we have:

```text
Stream<BigDecimal>
```

We can use:

```java
.sorted(Comparator.naturalOrder())
```

because `BigDecimal` has natural ordering.

But if we still have:

```text
Stream<Account>
```

we need:

```java
Comparator.comparing(Account::getBalance)
```

because Java needs to know which Account property should determine the ordering.

### Remember

```text
Stream<BigDecimal>
        ↓
naturalOrder()
```

but:

```text
Stream<Account>
        ↓
comparing(Account::getBalance)
```

---

## 16. min()

`min()` is a terminal Stream operation.

It finds the minimum element according to a Comparator.

Example:

```java
Stream<BigDecimal>
```

can use:

```java
.min(Comparator.naturalOrder())
```

The return type is:

```java
Optional<BigDecimal>
```

---

## 17. Why does min() return Optional?

Because the Stream might be empty.

If there are no Accounts, there is no minimum balance.

Instead of returning:

```java
null
```

Java uses:

```java
Optional.empty()
```

So:

```java
Optional<BigDecimal>
```

represents:

```text
value exists
OR
value doesn't exist
```

---

## 18. Finding Lowest Savings Balance

We implemented:

```java
public Optional<BigDecimal> findLowestSavingsBalance() {
    Predicate<Account> savings =
            account -> account.getAccountType() == AccountType.SAVINGS;

    Optional<BigDecimal> balance =
            bank.getAccounts().stream()
                    .filter(savings)
                    .map(Account::getBalance)
                    .min(Comparator.naturalOrder());

    return balance;
}
```

Stream flow:

```text
Collection<Account>
        ↓
stream()
        ↓
Stream<Account>
        ↓
filter(SAVINGS)
        ↓
Stream<Account>
        ↓
map(Account::getBalance)
        ↓
Stream<BigDecimal>
        ↓
min()
        ↓
Optional<BigDecimal>
```

---

## 19. Finding Highest Current Balance

Similarly:

```java
public Optional<BigDecimal> findHighestCurrentBalance() {
    Predicate<Account> current =
            account -> account.getAccountType() == AccountType.CURRENT;

    return bank.getAccounts().stream()
            .filter(current)
            .map(Account::getBalance)
            .max(Comparator.naturalOrder());
}
```

The difference:

```java
min()
```

finds the smallest.

```java
max()
```

finds the largest.

---

## 20. min() vs max()

| Method | Purpose | Return |
|---|---|---|
| `min()` | Smallest element | `Optional<T>` |
| `max()` | Largest element | `Optional<T>` |

Both are terminal operations and both require a Comparator.

---

## 21. Important: findFirst() vs min()

Suppose:

```text
1000
5000
2000
```

Then:

```java
.findFirst()
```

returns:

```text
1000
```

because it is the first element in the current stream order.

But:

```java
.min(Comparator.naturalOrder())
```

returns:

```text
1000
```

because it is the minimum value.

Now suppose stream order is:

```text
5000
1000
2000
```

Then:

```java
.findFirst()
```

returns:

```text
5000
```

while:

```java
.min(...)
```

returns:

```text
1000
```

### Therefore

> `findFirst()` means first element according to stream order.

> `min()` means smallest element according to the Comparator.

---

## 22. Finding the Highest Savings Account

We wanted the actual Account having the highest savings balance.

Instead of:

```text
Stream<BigDecimal>
```

we kept:

```text
Stream<Account>
```

and used:

```java
Optional<Account> result =
        accounts.stream()
                .filter(savings)
                .max(Comparator.comparing(Account::getBalance));
```

---

## 23. Why Didn't We Use map(Account::getBalance) Here?

If we write:

```java
.map(Account::getBalance)
```

the Stream becomes:

```text
Stream<BigDecimal>
```

Then:

```java
.max(...)
```

returns:

```text
Optional<BigDecimal>
```

We would know the highest balance, but we would lose the Account object.

Instead, we kept:

```text
Stream<Account>
```

and told Java how to compare Accounts:

```java
Comparator.comparing(Account::getBalance)
```

Therefore:

```text
Stream<Account>
      ↓
max(comparator)
      ↓
Optional<Account>
```

This lets us access:

```java
result.get().getAccountNumber()
result.get().getBalance()
result.get().getCustomer()
```

---

## 24. Very Important Pattern

### Need the highest/lowest VALUE?

```java
.map(Account::getBalance)
.max(Comparator.naturalOrder())
```

Result:

```java
Optional<BigDecimal>
```

### Need the Account having the highest/lowest VALUE?

Do not map.

```java
.max(Comparator.comparing(Account::getBalance))
```

Result:

```java
Optional<Account>
```

This is one of the most useful lessons from Day 3.

---

## 25. Sorting vs Finding

### Find one account

```java
.max(...)
```

Result:

```text
Optional<Account>
```

### Sort all accounts

```java
.sorted(...)
.toList()
```

Result:

```text
List<Account>
```

For example:

```java
.max(Comparator.comparing(Account::getBalance))
```

means:

> Give me the one Account with the highest balance.

Whereas:

```java
.sorted(Comparator.comparing(Account::getBalance))
.toList()
```

means:

> Give me all Accounts ordered by balance.

---

## 26. thenComparing()

Sometimes two Accounts have the same balance.

Example:

```text
Account 1005 → ₹5000
Account 1002 → ₹5000
Account 1008 → ₹3000
```

If we only use:

```java
Comparator.comparing(Account::getBalance)
```

both ₹5000 accounts have the same primary comparison value.

We can provide a secondary sorting rule using:

```java
.thenComparing(...)
```

---

## 27. thenComparing() Example

We used:

```java
Comparator.comparing(Account::getBalance)
        .thenComparing(Account::getAccountNumber)
```

Meaning:

### Primary sorting

```text
balance
```

### Secondary sorting

```text
accountNumber
```

So Java effectively does:

```text
First compare balance
        ↓
If balance is different
        ↓
Use balance ordering

If balance is equal
        ↓
Compare account number
```

---

## 28. Example of thenComparing()

Suppose:

```text
Account       Balance
0000010005    5000
0000010002    5000
0000010008    3000
```

Ascending balance with account number tie-breaker:

```text
0000010008 → 3000
0000010002 → 5000
0000010005 → 5000
```

For the two ₹5000 accounts, Java then compares:

```text
accountNumber
```

Therefore:

```text
0000010002
```

comes before:

```text
0000010005
```

---

## 29. Multiple thenComparing()

We can chain them:

```java
Comparator.comparing(Account::getBalance)
        .thenComparing(Account::getAccountNumber)
        .thenComparing(account -> account.getCustomer().getCustomerName());
```

Conceptually:

```text
1. Balance
       ↓ tie
2. Account Number
       ↓ tie
3. Customer Name
```

This is called multi-level sorting.

---

## 30. Comparator Chaining

General pattern:

```java
Comparator.comparing(primaryProperty)
        .thenComparing(secondaryProperty)
        .thenComparing(thirdProperty);
```

This is useful in real-world applications.

For example:

```text
Bank accounts
    ↓
Balance
    ↓
Account number
```

or:

```text
Employees
    ↓
Department
    ↓
Salary
    ↓
Employee name
```

---

## 31. thenComparing() vs reversed()

These are different concepts.

### reversed()

Changes the direction of an existing Comparator.

```java
Comparator.comparing(Account::getBalance)
        .reversed()
```

Means:

```text
Balance descending
```

### thenComparing()

Adds another comparison rule.

```java
Comparator.comparing(Account::getBalance)
        .thenComparing(Account::getAccountNumber)
```

Means:

```text
Balance first
Account number if balance is equal
```

---

## 32. Combining Both

We can also combine them:

```java
Comparator.comparing(Account::getBalance)
        .reversed()
        .thenComparing(Account::getAccountNumber);
```

Conceptually:

```text
1. Balance descending
2. If balance is equal
3. Account number ascending
```

---

## 33. Our sortAccountsByBalance() Method

The cleaner final implementation:

```java
public List<Account> sortAccountsByBalance() {
    return bank.getAccounts().stream()
            .sorted(Comparator.comparing(Account::getBalance))
            .toList();
}
```

### Type flow

```text
bank.getAccounts()
        ↓
Collection<Account>
        ↓
stream()
        ↓
Stream<Account>
        ↓
sorted(...)
        ↓
Stream<Account>
        ↓
toList()
        ↓
List<Account>
```

---

## 34. Descending Version

```java
public List<Account> sortAccountsByBalanceDescending() {
    return bank.getAccounts().stream()
            .sorted(
                Comparator.comparing(Account::getBalance)
                        .reversed()
            )
            .toList();
}
```

Type flow remains:

```text
Stream<Account>
        ↓
Stream<Account>
        ↓
List<Account>
```

Only the ordering changes.

---

## 35. Testing Our Sorting Method

We initially made a testing mistake.

The method returns:

```java
List<Account>
```

but we tried to compare it directly with:

```text
List<Integer>
```

That is incorrect.

We learned:

> When a method returns objects, inspect the object's properties rather than pretending the list contains primitive values.

For example:

```java
assertEquals(
        new BigDecimal("1000"),
        result.get(0).getBalance()
);
```

Then verify positions 1 and 2 similarly.

This verifies the actual sorting behavior.

---

## 36. Testing thenComparing()

We added accounts with the same balance.

For example:

```text
Account A → ₹5000
Account B → ₹5000
```

Then we verified that the account number determines their relative ordering.

This verifies:

```text
Primary comparison
        +
Tie-breaker comparison
```

---

## 37. Stream Operations Covered So Far

### Intermediate operations

```java
filter()
map()
sorted()
```

### Terminal operations

```java
collect()
toList()
toSet()
forEach()
count()
findFirst()
anyMatch()
allMatch()
min()
max()
```

### Comparator tools

```java
Comparator.naturalOrder()
Comparator.reverseOrder()
Comparator.comparing()
Comparator.reversed()
Comparator.thenComparing()
```

---

## 38. Important Stream Type Tracking

This is probably the single most important practical skill we developed during Week 2.

Always ask:

> What type is my Stream currently holding?

Example:

```java
bank.getAccounts().stream()
```

means:

```text
Stream<Account>
```

Then:

```java
.map(Account::getBalance)
```

becomes:

```text
Stream<BigDecimal>
```

Then:

```java
.min(Comparator.naturalOrder())
```

becomes:

```text
Optional<BigDecimal>
```

Another example:

```java
bank.getAccounts().stream()
```

```text
Stream<Account>
```

then:

```java
.max(Comparator.comparing(Account::getBalance))
```

becomes:

```text
Optional<Account>
```

Notice the difference.

---

## 39. Full Day 3 Concept Map

```text
                    Java Stream API
                          │
                          ▼
                    Stream<Account>
                          │
             ┌────────────┴────────────┐
             │                         │
           map()                    sorted()
             │                         │
             ▼                         ▼
      Stream<BigDecimal>         Stream<Account>
             │                         │
        ┌────┴────┐             Comparator
        │         │                    │
       min()     max()          comparing()
        │         │                    │
        ▼         ▼              reversed()
Optional<BigDecimal>             thenComparing()
```

---

## 40. Interview Questions — Day 3

### Q1. What is Comparator?

**Answer:**

> `Comparator` is a functional interface used to define custom ordering between objects. It allows objects to be sorted based on specific properties without requiring the class itself to define that ordering.

### Q2. Difference between Comparable and Comparator?

**Answer:**

> `Comparable` defines the natural ordering of a class through `compareTo()`, whereas `Comparator` defines custom or external ordering through `compare()`.

### Q3. What does sorted() do?

**Answer:**

> `sorted()` is an intermediate Stream operation that returns a Stream containing the elements in sorted order according to their natural ordering or a supplied Comparator.

### Q4. What is Comparator.comparing()?

**Answer:**

> `Comparator.comparing()` creates a Comparator based on a property extracted from an object.

Example:

```java
Comparator.comparing(Account::getBalance)
```

This compares Accounts using their balance.

### Q5. What does reversed() do?

**Answer:**

> `reversed()` reverses the ordering defined by an existing Comparator.

Example:

```java
Comparator.comparing(Account::getBalance)
        .reversed();
```

### Q6. What does thenComparing() do?

**Answer:**

> `thenComparing()` adds a secondary comparison rule that is used when the primary comparison results in a tie.

Example:

```java
Comparator.comparing(Account::getBalance)
        .thenComparing(Account::getAccountNumber);
```

### Q7. Difference between findFirst() and min()?

**Answer:**

> `findFirst()` returns the first element according to the current stream order, whereas `min()` determines the smallest element according to the supplied Comparator.

### Q8. Why does min() return Optional?

**Answer:**

> Because the Stream may be empty. In that case there is no minimum value, so `Optional.empty()` represents the absence of a result.

### Q9. How do you find the Account with the highest balance?

```java
accounts.stream()
        .max(Comparator.comparing(Account::getBalance));
```

Result:

```java
Optional<Account>
```

### Q10. How do you find only the highest balance?

```java
accounts.stream()
        .map(Account::getBalance)
        .max(Comparator.naturalOrder());
```

Result:

```java
Optional<BigDecimal>
```

---

## 41. Most Important Day 3 Difference

### Highest balance

```java
.map(Account::getBalance)
.max(Comparator.naturalOrder())
```

Result:

```text
Optional<BigDecimal>
```

### Account with highest balance

```java
.max(Comparator.comparing(Account::getBalance))
```

Result:

```text
Optional<Account>
```

The difference is whether we map away the Account object.

---

## 42. Common Mistakes We Encountered

### Mistake 1 — Wrong return type

Trying to compare:

```text
List<Account>
```

with:

```text
List<Integer>
```

**Lesson:** Check the actual return type.

### Mistake 2 — Losing the Account object

Using:

```java
.map(Account::getBalance)
```

when we actually needed the Account.

**Lesson:** Don't use `map()` if you need the original object in the final result.

### Mistake 3 — Confusing findFirst() with minimum

```java
findFirst()
```

does not mean minimum.

It means:

```text
first according to current stream order
```

### Mistake 4 — Type inference issue

```java
Comparator.comparing(account -> account.getBalance())
```

could result in `account` being inferred as `Object`.

We solved it with:

```java
Comparator.comparing(Account::getBalance)
```

### Mistake 5 — Forgetting what sorted() returns

`sorted()` does not return a `List`.

It returns:

```java
Stream<Account>
```

We need a terminal operation such as:

```java
.toList()
```

to produce:

```java
List<Account>
```

---

## 43. Day 3 Practical Work Completed

Today we actually coded and tested these concepts in IntelliJ.

### Completed

- `Comparator`
- `Comparator.naturalOrder()`
- `Comparator.reverseOrder()`
- `Comparator.comparing()`
- `sorted()`
- Ascending Account balance sorting
- Descending Account balance sorting
- `min()`
- `max()`
- `findFirst()` vs `min()`
- Finding lowest savings balance
- Finding highest current balance
- Finding Account with highest savings balance
- `Comparator.reversed()`
- `thenComparing()`
- Multi-level sorting
- Tie-breaking using Account Number
- JUnit tests for sorting
- JUnit tests for highest/lowest results
- Correct handling of `Optional`
- Stream type tracking

---

## 44. Day 3 Banking Platform Progress

Our Banking Platform is becoming more useful:

```text
Customer
   ↓
Account
   ↓
Bank
   ↓
BankService
   ↓
StreamPractice
   ↓
Stream-based account analysis
```

We can now perform operations such as:

```text
Find lowest savings balance
Find highest current balance
Find highest savings account
Sort accounts by balance
Sort accounts by balance descending
Sort using tie-breakers
```

This is moving beyond basic Java syntax into real business-oriented collection processing.

---

## 45. Day 3 Status

## 🟢 WEEK 2 — DAY 3 COMPLETE

### Main topic

**Comparator + Sorting + Min/Max**

### Core concepts mastered

```text
Comparator
Comparable vs Comparator
sorted()
naturalOrder()
reverseOrder()
comparing()
reversed()
thenComparing()
min()
max()
Optional
Stream type tracking
```

### Practical banking use

```text
Account analysis
Balance comparison
Account ranking/order
Tie-breaking
Savings/current account filtering
```

---

## 46. What We Are NOT Starting Today

We are not starting Day 4 now.

`reduce()` will remain for the next session.

The next checkpoint will begin with:

```text
Week 2 — Day 4
        ↓
reduce()
        ↓
Total balance
        ↓
Savings total
        ↓
Current total
        ↓
Optional / empty stream
        ↓
JUnit
```

### Final Day 3 checkpoint

```text
Week 2
│
├── Day 1 ✅ Functional Interface + Predicate + Stream basics
├── Day 2 ✅ findFirst + anyMatch + allMatch + count
├── Day 3 ✅ Comparator + Sorting + min/max + thenComparing
└── Day 4 ⏳ reduce() — NEXT SESSION
```

**Day 3 is officially closed.**
