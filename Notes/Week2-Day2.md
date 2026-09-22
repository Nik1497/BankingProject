# Week 2 — Day 2: Stream API Intermediate & Terminal Operations

## Goal
Continue Java Stream API with practical Banking Platform examples.

## 1. Stream Pipeline

Typical structure:

```text
Source → stream() → intermediate operations → terminal operation → result
```

Example:

```java
accounts.values()
    .stream()
    .filter(savings)
    .map(Account::getBalance)
    .sorted()
    .findFirst();
```

Intermediate operations build the pipeline; a terminal operation finishes it.

---

## 2. `sorted()`

`sorted()` is an **intermediate operation** used to sort stream elements.

Ascending:

```java
numbers.stream()
    .sorted()
    .forEach(System.out::println);
```

Descending:

```java
numbers.stream()
    .sorted(Comparator.reverseOrder())
    .forEach(System.out::println);
```

`sorted()` changes the **order**, not the element type.

```text
Stream<BigDecimal> → sorted() → Stream<BigDecimal>
Stream<String>     → sorted() → Stream<String>
```

### Banking example

```java
accounts.values()
    .stream()
    .filter(account -> account.getAccountType() == AccountType.SAVINGS)
    .map(account -> account.getBalance())
    .sorted()
    .forEach(System.out::println);
```

Descending:

```java
accounts.values()
    .stream()
    .filter(account -> account.getAccountType() == AccountType.SAVINGS)
    .map(account -> account.getBalance())
    .sorted(Comparator.reverseOrder())
    .forEach(System.out::println);
```

Collecting:

```java
List<BigDecimal> balances = accounts.values()
    .stream()
    .filter(account -> account.getAccountType() == AccountType.SAVINGS)
    .map(account -> account.getBalance())
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
```

---

## 3. `findFirst()`

`findFirst()` is a **terminal operation**.

It returns the first element currently present in the stream.

```java
Optional<Integer> first =
    numbers.stream()
        .sorted()
        .findFirst();
```

For values `50, 10, 30, 20`, sorting produces `10, 20, 30, 50`, so the result is `Optional[10]`.

### Why `Optional<T>`?

The stream may be empty:

```java
Optional<Integer> first =
    numbers.stream()
        .findFirst();
```

An empty stream produces `Optional.empty()`.

Type rule:

```text
Stream<Account>    → Optional<Account>
Stream<String>     → Optional<String>
Stream<BigDecimal> → Optional<BigDecimal>
```

### Banking example

```java
Optional<Account> result =
    accounts.values()
        .stream()
        .filter(savings)
        .findFirst();
```

Important: `findFirst()` means the first element **at that point in the stream**. It does not inherently mean highest or lowest.

Using today's learned operations, sorting first can give lowest/highest:

Lowest balance:

```java
Optional<BigDecimal> result =
    accounts.values()
        .stream()
        .filter(account -> account.getAccountType() == AccountType.SAVINGS)
        .map(account -> account.getBalance())
        .sorted()
        .findFirst();
```

Highest balance:

```java
Optional<BigDecimal> result =
    accounts.values()
        .stream()
        .filter(account -> account.getAccountType() == AccountType.SAVINGS)
        .map(account -> account.getBalance())
        .sorted(Comparator.reverseOrder())
        .findFirst();
```

**Note:** `min()` and `max()` were mentioned but intentionally not learned today.

---

## 4. `anyMatch()`

`anyMatch()` is a **terminal operation**.

It asks:

> Does at least one element satisfy this condition?

Return type:

```java
boolean
```

Example:

```java
boolean result =
    accounts.values()
        .stream()
        .anyMatch(savings);
```

Banking example:

```java
Predicate<Account> current =
    account -> account.getAccountType() == AccountType.CURRENT;

Predicate<Account> highBalance10k =
    account -> account.getBalance()
        .compareTo(new BigDecimal("10000")) > 0;
```

Question: Does the bank have at least one CURRENT account with balance greater than ₹10,000?

```java
boolean result = accounts.values()
    .stream()
    .filter(current)
    .anyMatch(highBalance10k);
```

---

## 5. `allMatch()`

`allMatch()` is a **terminal operation**.

It asks:

> Do all elements satisfy this condition?

Return type:

```java
boolean
```

Example:

```java
boolean result =
    accounts.values()
        .stream()
        .filter(savings)
        .allMatch(highBalance);
```

Banking example:

```java
Predicate<Account> current =
    account -> account.getAccountType() == AccountType.CURRENT;

Predicate<Account> minimumBalance =
    account -> account.getBalance()
        .compareTo(new BigDecimal("5000")) >= 0;
```

Question: Do ALL CURRENT accounts have at least ₹5,000?

```java
boolean result = accounts.values()
    .stream()
    .filter(current)
    .allMatch(minimumBalance);
```

---

## 6. `anyMatch()` vs `allMatch()`

| Operation | Question | Return |
|---|---|---|
| `anyMatch()` | Does at least one match? | `boolean` |
| `allMatch()` | Do all match? | `boolean` |
| `findFirst()` | What is the first element? | `Optional<T>` |
| `count()` | How many elements? | `long` |

Mental shortcut:

```text
anyMatch  → at least one?
allMatch  → everyone?
findFirst → first?
count     → how many?
```

---

## 7. `count()`

`count()` is a **terminal operation** that counts the elements remaining in the stream.

Return type:

```java
long
```

Example:

```java
long result = accounts.values()
    .stream()
    .filter(current)
    .count();
```

This means: How many CURRENT accounts exist?

### Important

`count()` does **not** accept a condition.

Incorrect:

```java
.count(highBalance10k)
```

Correct:

```java
.filter(highBalance10k)
.count();
```

`filter()` decides what stays; `count()` counts what remains.

---

## 8. Multiple `filter()` Operations

Filters can be chained:

```java
long result = accounts.values()
    .stream()
    .filter(current)
    .filter(lowBalance5k)
    .count();
```

Meaning:

> How many CURRENT accounts have balance below ₹5,000?

Mental model:

```text
All accounts
    ↓
CURRENT only
    ↓
Balance < ₹5,000
    ↓
COUNT
```

Multiple filters can also be combined into one predicate using `&&`, but separate filters are often easier to read while learning.

---

## 9. `map()` Reminder

`map()` transforms every element and **can change its type**.

```java
.map(account -> account.getBalance())
```

means:

```text
Account → BigDecimal
```

So:

```text
Stream<Account>
      ↓ map()
Stream<BigDecimal>
```

Another example:

```java
.map(account -> account.getAccountNumber())
```

means:

```text
Account → String
```

Key distinction:

> `filter()` selects elements; `map()` transforms elements.

---

## 10. Method Reference

We used:

```java
Account::getBalance
```

which is equivalent to:

```java
account -> account.getBalance()
```

Example:

```java
List<BigDecimal> balances =
    accounts.values()
        .stream()
        .map(Account::getBalance)
        .toList();
```

If balances are:

```text
5000
12000
3000
8000
```

the result is:

```text
[5000, 12000, 3000, 8000]
```

Type:

```java
List<BigDecimal>
```

---

## 11. Type Tracking

Always track the current stream element type.

```java
accounts.values()
    .stream()
```

Current type:

```text
Stream<Account>
```

After:

```java
.map(Account::getBalance)
```

Current type:

```text
Stream<BigDecimal>
```

After:

```java
.sorted()
```

Still:

```text
Stream<BigDecimal>
```

After:

```java
.findFirst()
```

Result:

```text
Optional<BigDecimal>
```

This is one of the most useful habits when debugging Stream code.

---

## 12. Common Mistakes From Today

### Wrong object name

Incorrect:

```java
Account.values()
```

Correct:

```java
accounts.values()
```

`accounts` is the Map containing the Account objects.

### Wrong capitalization

Incorrect:

```java
.allmatch(...)
```

Correct:

```java
.allMatch(...)
```

Java is case-sensitive.

### Passing a condition to `count()`

Incorrect:

```java
.count(highBalance10k)
```

Correct:

```java
.filter(highBalance10k)
.count();
```

### Confusing high-balance filtering with highest balance

```java
.filter(highBalance)
.findFirst()
```

means:

> Find the first account satisfying the high-balance condition.

It does **not** mean:

> Find the account with the highest balance.

With today's learned operations, highest can be demonstrated using:

```java
.sorted(Comparator.reverseOrder())
.findFirst();
```

`min()` and `max()` are not part of today's learned material.

---

## 13. Day 2 Operation Summary

| Operation | Category | Purpose | Result |
|---|---|---|---|
| `sorted()` | Intermediate | Sort elements | `Stream<T>` |
| `findFirst()` | Terminal | Get first element | `Optional<T>` |
| `anyMatch()` | Terminal | At least one matches | `boolean` |
| `allMatch()` | Terminal | All elements match | `boolean` |
| `count()` | Terminal | Count elements | `long` |
| `filter()` | Intermediate | Keep matching elements | `Stream<T>` |
| `map()` | Intermediate | Transform elements | `Stream<R>` |
| `collect()` | Terminal | Collect stream results | Collection |
| `forEach()` | Terminal | Perform action for each | `void` |

---

## 14. Interview Questions

**Q: Is `sorted()` intermediate or terminal?**  
A: Intermediate.

**Q: What does `findFirst()` return?**  
A: `Optional<T>`.

**Q: Difference between `anyMatch()` and `allMatch()`?**  
A: `anyMatch()` checks whether at least one element matches; `allMatch()` checks whether every element matches.

**Q: What does `count()` return?**  
A: `long`.

**Q: Can `count()` accept a Predicate?**  
A: No. Use `filter()` first, then `count()`.

**Q: Does `sorted()` change the element type?**  
A: No. It changes ordering.

**Q: Does `filter()` change the element type?**  
A: No. It selects elements.

**Q: Can `map()` change the element type?**  
A: Yes.

**Q: Why does `findFirst()` return Optional?**  
A: Because the stream may be empty.

---

# 15. Final Mental Model

```text
FILTER
"What should stay?"

MAP
"What should each element become?"

SORTED
"In what order?"

FIND FIRST
"What is the first one?"

ANY MATCH
"Does at least one match?"

ALL MATCH
"Do all match?"

COUNT
"How many remain?"
```

Example:

```java
accounts.values()
    .stream()
    .filter(savings)
    .map(Account::getBalance)
    .sorted(Comparator.reverseOrder())
    .findFirst();
```

Read it as:

> Get all accounts → keep SAVINGS → convert each account to its balance → sort highest first → take the first balance.

---

# Day 2 Status

Completed:

- `sorted()` ✅
- ascending/descending sorting ✅
- `findFirst()` ✅
- `Optional<T>` in Stream results ✅
- `anyMatch()` ✅
- `allMatch()` ✅
- `count()` ✅
- multiple `filter()` operations ✅
- combining Stream operations ✅
- method reference `Account::getBalance` ✅
- Stream type tracking ✅

Not yet learned:

- `min()` ❌
- `max()` ❌

Tomorrow:

1. Start with the planned 3-question Day 2 mini-test.
2. No hints.
3. Then move to Week 2 Day 3.
