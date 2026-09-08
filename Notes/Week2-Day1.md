# Week 2 -- Day 1

## Functional Interfaces, Lambda Expressions & Stream API Foundations

## 1. Topics Covered

-   Functional interfaces
-   Lambda expressions
-   `@FunctionalInterface`
-   `Predicate<T>`
-   Predicate composition: `and()`, `or()`, `negate()`
-   `Map.values()`
-   `stream()`
-   `filter()`
-   `map()`
-   `collect()`
-   `Collectors.toList()`
-   `Collectors.toSet()`
-   `forEach()`
-   `count()`
-   Intermediate vs terminal operations
-   Tracking the type through a Stream pipeline
-   Banking Platform examples and exercises

------------------------------------------------------------------------

## 2. Functional Interface

A functional interface is an interface with exactly **one abstract
method**.

It can still contain multiple `default` and `static` methods.

``` java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

The single abstract method is:

``` java
int add(int a, int b);
```

`@FunctionalInterface` tells the compiler that the interface is intended
to have exactly one abstract method.

------------------------------------------------------------------------

## 3. Lambda Expressions

A lambda provides an implementation for the single abstract method of a
functional interface.

Syntax:

``` java
(parameters) -> expression
```

Example:

``` java
Calculator calculator = (a, b) -> a + b;
```

Java infers the parameter types from the functional interface.

------------------------------------------------------------------------

## 4. Connection to JUnit

We previously used:

``` java
assertThrows(
    InsufficientBalanceException.class,
    () -> account.withdraw(new BigDecimal("1500"))
);
```

The lambda tells JUnit which code to execute while checking for the
expected exception.

This is another practical use of functional-interface behavior.

------------------------------------------------------------------------

## 5. Predicate`<T>`{=html}

`Predicate<T>` is a built-in functional interface representing a
condition.

Conceptually:

``` text
Input -> condition -> true/false
```

Its main abstract method is:

``` java
boolean test(T t);
```

Example:

``` java
Predicate<BigDecimal> checker =
    balance -> balance.compareTo(new BigDecimal("1000")) > 0;
```

Banking Platform example:

``` java
Predicate<Account> savings =
    account -> account.getAccountType() == AccountType.SAVINGS;
```

Another:

``` java
Predicate<Account> highBalance =
    account -> account.getBalance()
        .compareTo(new BigDecimal("5000")) > 0;
```

------------------------------------------------------------------------

## 6. BigDecimal Comparisons

For money comparisons, use:

``` java
balance.compareTo(limit)
```

Results:

``` text
negative -> less than
0        -> equal
positive -> greater than
```

Examples:

``` java
balance.compareTo(new BigDecimal("5000")) > 0
```

means greater than 5000.

``` java
balance.compareTo(new BigDecimal("5000")) < 0
```

means less than 5000.

``` java
balance.compareTo(new BigDecimal("5000")) == 0
```

means equal to 5000.

``` java
balance.compareTo(new BigDecimal("5000")) >= 0
```

means greater than or equal to 5000.

``` java
balance.compareTo(new BigDecimal("5000")) <= 0
```

means less than or equal to 5000.

------------------------------------------------------------------------

## 7. Predicate Composition

### `and()`

``` java
savings.and(highBalance)
```

means:

``` text
SAVINGS AND balance > 5000
```

### `or()`

``` java
savings.or(highBalance)
```

means:

``` text
SAVINGS OR balance > 5000
```

### `negate()`

``` java
savings.negate()
```

means:

``` text
NOT SAVINGS
```

If `savings` is true for SAVINGS accounts, `savings.negate()` is true
for non-SAVINGS accounts.

------------------------------------------------------------------------

## 8. Map vs Stream `map()`

Do not confuse `Map` with Stream `map()`.

A `Map` is a data structure:

``` java
Map<String, Account> accounts;
```

Here:

``` text
String  -> key
Account -> value
```

A Stream's `map()` transforms elements.

Example:

``` java
.map(account -> account.getAccountNumber())
```

This transforms:

``` text
Account -> String
```

Another:

``` java
.map(account -> account.getBalance())
```

transforms:

``` text
Account -> BigDecimal
```

Interview answer:

> `filter()` selects elements; `map()` transforms elements.

------------------------------------------------------------------------

## 9. Map `values()`

Given:

``` java
Map<String, Account> accounts;
```

``` java
accounts.values()
```

returns all Account values.

Conceptually:

``` text
Map<String, Account>
        ↓
Collection<Account>
```

Then:

``` java
accounts.values().stream()
```

gives:

``` text
Stream<Account>
```

------------------------------------------------------------------------

## 10. Stream Pipeline

Typical pattern:

``` java
source
    .stream()
    .filter(...)
    .map(...)
    .collect(...);
```

Banking Platform example:

``` java
accounts.values()
    .stream()
    .filter(savings)
    .map(account -> account.getAccountNumber())
    .collect(Collectors.toList());
```

------------------------------------------------------------------------

## 11. `filter()`

`filter()` selects elements that satisfy a condition.

Example:

``` java
.filter(savings)
```

It keeps only SAVINGS accounts.

The element type does not change:

``` text
Stream<Account>
       ↓ filter()
Stream<Account>
```

The number of elements can change.

------------------------------------------------------------------------

## 12. `map()`

`map()` transforms each element.

Example:

``` java
.map(account -> account.getAccountNumber())
```

Type transformation:

``` text
Account -> String
```

Therefore:

``` text
Stream<Account>
       ↓ map()
Stream<String>
```

Another example:

``` java
.map(account -> account.getBalance())
```

gives:

``` text
Stream<BigDecimal>
```

------------------------------------------------------------------------

## 13. Chained Object Access

We practiced:

``` java
.map(account -> account.getCustomer().getCustomerName())
```

The transformation is:

``` text
Account
  ↓ getCustomer()
Customer
  ↓ getCustomerName()
String
```

Therefore the resulting stream is:

``` text
Stream<String>
```

------------------------------------------------------------------------

## 14. Understanding "Stop at Stream`<String>`{=html}"

When asked to stop at `Stream<String>`, do not call `collect()` yet.

Example:

``` java
accounts.values()
    .stream()
    .filter(savings)
    .map(account -> account.getAccountNumber());
```

Type flow:

``` text
Map<String, Account>
        ↓ values()
Collection<Account>
        ↓ stream()
Stream<Account>
        ↓ filter()
Stream<Account>
        ↓ map()
Stream<String>  <- stop
```

After `collect()`:

``` text
Stream<String>
     ↓
List<String> or Set<String>
```

------------------------------------------------------------------------

## 15. `collect()`

`collect()` is a **terminal operation**.

It gathers the Stream elements into a final result.

Example:

``` java
List<String> accountNumbers =
    accounts.values()
        .stream()
        .filter(savings)
        .map(account -> account.getAccountNumber())
        .collect(Collectors.toList());
```

Type transition:

``` text
Stream<String>
     ↓ collect(toList())
List<String>
```

------------------------------------------------------------------------

## 16. `Collectors.toList()`

Use:

``` java
Collectors.toList()
```

to collect into a List.

``` java
List<String> accountNumbers =
    accounts.values()
        .stream()
        .filter(savings)
        .map(account -> account.getAccountNumber())
        .collect(Collectors.toList());
```

A List can contain duplicates.

------------------------------------------------------------------------

## 17. `Collectors.toSet()`

Use:

``` java
Collectors.toSet()
```

to collect into a Set.

``` java
Set<String> accountNumbers =
    accounts.values()
        .stream()
        .filter(current)
        .map(account -> account.getAccountNumber())
        .collect(Collectors.toSet());
```

A Set keeps unique elements.

Example:

``` text
Input:
1001
1002
1001
1003
1002

Set:
1001
1002
1003
```

Do not generally rely on a plain `HashSet` for insertion order.

------------------------------------------------------------------------

## 18. `forEach()`

`forEach()` is a terminal operation.

It performs an action for every element.

Example:

``` java
accounts.values()
    .stream()
    .filter(savings)
    .map(account -> account.getAccountNumber())
    .forEach(accountNumber ->
        System.out.println(accountNumber));
```

The result is:

``` text
void
```

It prints each account number rather than creating a List or Set.

------------------------------------------------------------------------

## 19. `count()`

`count()` is a terminal operation.

It returns the number of elements as a `long`.

Example:

``` java
long count =
    accounts.values()
        .stream()
        .filter(savings)
        .count();
```

If there are 6 SAVINGS accounts:

``` text
count = 6L
```

------------------------------------------------------------------------

## 20. Intermediate Operations

Intermediate operations return another Stream and allow further
chaining.

Examples:

``` java
filter()
map()
sorted()
```

Example:

``` java
stream
    .filter(...)
    .map(...)
    .sorted(...);
```

------------------------------------------------------------------------

## 21. Terminal Operations

Terminal operations finish the Stream pipeline.

Examples practiced today:

``` java
collect()
count()
forEach()
```

Once a terminal operation is called, the Stream pipeline is finished.

------------------------------------------------------------------------

## 22. Why `collect()` Cannot Be Followed by `map()`

Invalid:

``` java
stream
    .filter(...)
    .collect(Collectors.toList())
    .map(...);
```

Reason:

``` text
Stream
  ↓
collect()
  ↓
List
```

After `collect()`, there is no Stream to call `map()` on.

Correct order:

``` java
stream
    .filter(...)
    .map(...)
    .collect(Collectors.toList());
```

------------------------------------------------------------------------

## 23. Multiple Filters

These are both valid ways to express AND logic.

### Separate filters

``` java
accounts.values()
    .stream()
    .filter(current)
    .filter(highBalance);
```

### Combined predicate

``` java
accounts.values()
    .stream()
    .filter(current.and(highBalance));
```

Both mean:

``` text
CURRENT AND balance > 5000
```

------------------------------------------------------------------------

## 24. `or()` Example

``` java
accounts.values()
    .stream()
    .filter(current.or(lowBalance))
    .map(account -> account.getCustomer().getCustomerName());
```

Meaning:

``` text
CURRENT OR balance < 1000
```

------------------------------------------------------------------------

## 25. `negate()` Example

``` java
accounts.values()
    .stream()
    .filter(savings.negate())
    .map(account -> account.getAccountNumber());
```

Meaning:

``` text
NOT SAVINGS
```

------------------------------------------------------------------------

## 26. Complete Banking Platform Example

Find customer names for SAVINGS accounts with balance greater than 5000:

``` java
List<String> names =
    accounts.values()
        .stream()
        .filter(savings)
        .filter(highBalance)
        .map(account ->
            account.getCustomer().getCustomerName())
        .collect(Collectors.toList());
```

Type flow:

``` text
Map<String, Account>
        ↓
Collection<Account>
        ↓
Stream<Account>
        ↓ filter(savings)
Stream<Account>
        ↓ filter(highBalance)
Stream<Account>
        ↓ map(...)
Stream<String>
        ↓ collect(toList())
List<String>
```

------------------------------------------------------------------------

## 27. Unique Customer Names

``` java
Set<String> customerNames =
    accounts.values()
        .stream()
        .filter(account ->
            account.getAccountType() == AccountType.SAVINGS)
        .map(account ->
            account.getCustomer().getCustomerName())
        .collect(Collectors.toSet());
```

The Set is useful because it removes duplicate names.

------------------------------------------------------------------------

## 28. Common Mistakes From Practice

### Method parentheses

Incorrect:

``` java
account.getAccountType
```

Correct:

``` java
account.getAccountType()
```

Methods require `()`.

### Map variable

Given:

``` java
Map<String, Account> accounts;
```

correct:

``` java
accounts.values()
```

not:

``` java
account.values()
```

### Predicate usage

Given:

``` java
Predicate<Account> savings = ...;
```

correct:

``` java
.filter(savings)
```

not:

``` java
.filter(savings())
```

### `Collectors` spelling

Correct:

``` java
Collectors.toList()
Collectors.toSet()
```

Not:

``` java
Collector.toList()
Collector.toSet()
```

### Result type after `map()`

If:

``` java
.map(account -> account.getAccountNumber())
```

then the result elements are Strings.

Therefore:

``` java
List<String>
```

not:

``` java
List<Account>
```

### Variable naming

Prefer:

``` java
Map<String, Account> accounts;

List<String> accountNumbers;

Set<String> customerNames;
```

Avoid reusing `accounts` for unrelated result variables.

------------------------------------------------------------------------

## 29. Interview Questions

### Q1. What is a functional interface?

An interface with exactly one abstract method.

### Q2. What is a lambda expression?

A concise way to provide an implementation for a functional interface's
single abstract method.

### Q3. What is `Predicate<T>`?

A functional interface representing a condition that takes one input and
returns a boolean.

### Q4. What does `filter()` do?

It selects elements that satisfy a condition.

### Q5. What does `map()` do?

It transforms each element into another value.

### Q6. Difference between `filter()` and `map()`?

`filter()` selects; `map()` transforms.

### Q7. What does `collect()` do?

It is a terminal operation used to gather Stream elements into a final
result.

### Q8. What does `Collectors.toList()` produce?

A List containing the Stream elements.

### Q9. What does `Collectors.toSet()` produce?

A Set containing unique elements.

### Q10. What does `count()` return?

A `long` representing the number of elements.

### Q11. What does `forEach()` return?

`void`.

### Q12. What are intermediate operations?

Operations that return another Stream, such as `filter()`, `map()`, and
`sorted()`.

### Q13. What are terminal operations?

Operations that finish the Stream pipeline, such as `collect()`,
`count()`, and `forEach()`.

### Q14. Can `map()` be called after `collect()`?

No. `collect()` is terminal and ends the Stream pipeline.

### Q15. Why use a Set?

To eliminate duplicate elements.

------------------------------------------------------------------------

## 30. Core Patterns to Memorize

### Filter

``` java
accounts.values()
    .stream()
    .filter(savings);
```

### Transform

``` java
accounts.values()
    .stream()
    .map(account -> account.getBalance());
```

### Filter + Transform

``` java
accounts.values()
    .stream()
    .filter(savings)
    .map(account -> account.getAccountNumber());
```

### Filter + Transform + List

``` java
List<String> accountNumbers =
    accounts.values()
        .stream()
        .filter(savings)
        .map(account -> account.getAccountNumber())
        .collect(Collectors.toList());
```

### Filter + Transform + Set

``` java
Set<String> customerNames =
    accounts.values()
        .stream()
        .filter(savings)
        .map(account ->
            account.getCustomer().getCustomerName())
        .collect(Collectors.toSet());
```

### Count

``` java
long count =
    accounts.values()
        .stream()
        .filter(savings)
        .count();
```

### Print

``` java
accounts.values()
    .stream()
    .filter(savings)
    .map(account -> account.getAccountNumber())
    .forEach(accountNumber ->
        System.out.println(accountNumber));
```

------------------------------------------------------------------------

# 31. Day 1 Final Mental Model

``` text
Functional Interface
        ↓
Lambda
        ↓
Predicate
        ↓
Stream
        ↓
filter()  -> select
        ↓
map()     -> transform
        ↓
collect() -> gather
```

Important Stream classification:

``` text
Intermediate:
    filter()
    map()
    sorted()

Terminal:
    collect()
    count()
    forEach()
```

The most important interview statement from today:

> **`filter()` selects elements; `map()` transforms elements.**

And the Banking Platform pattern to remember:

``` java
accounts.values()
    .stream()
    .filter(...)
    .map(...)
    .collect(...);
```

This foundation will be used for the deeper Stream API work in Week 2
Day 2.
