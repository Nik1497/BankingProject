# Week 2 — Day 4: Stream API `reduce()`

## Day 4 objectives
- Understand `reduce()` as a terminal stream operation.
- Use `reduce()` with and without an identity value.
- Understand `Optional` results.
- Apply `reduce()` to banking balances.
- Combine `filter()`, `map()`, and `reduce()`.
- Use `reduce()` for multiplication.
- Test normal and empty inputs with JUnit.

## 1. What is `reduce()`?
`reduce()` combines multiple stream elements into one result.

```text
Many elements → combine repeatedly → one result
```

## 2. Two-argument `reduce()`

General form:

```java
reduce(identity, accumulator)
```

Example:

```java
int total = List.of(10, 20, 30, 40)
        .stream()
        .reduce(0, (a, b) -> a + b);
```

- **Identity:** starting value (`0` for addition, `1` for multiplication).
- **Accumulator:** rule for combining the current result with the next element.
- `a`: accumulated result so far.
- `b`: next stream element.

Starting at `0`, adding `10, 20, 30, 40` gives `100`.

### Empty stream behavior
With an identity value, an empty stream returns the identity.

## 3. One-argument `reduce()`

```java
Optional<Integer> result = numbers.stream()
        .reduce((a, b) -> a + b);
```

There is no identity value, so the result is an `Optional`:
- Non-empty stream: Optional contains the reduction result.
- Empty stream: `Optional.empty()`.

| Form | Empty stream result |
|---|---|
| `reduce(identity, accumulator)` | Identity value |
| `reduce(accumulator)` | `Optional.empty()` |

## 4. BigDecimal addition

Use `BigDecimal` for banking money. Its addition method is `.add()`:

```java
(a, b) -> a.add(b)
```

Identity for addition:

```java
BigDecimal.ZERO
```

### Total balance

```java
public BigDecimal calculateBalance() {
    BigDecimal balance = bank.getAccounts()
            .stream()
            .map(acc -> acc.getBalance())
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

    return balance;
}
```

A concise method reference can replace the lambda in `map()`:

```java
.map(Account::getBalance)
```

### Type flow

```text
Collection<Account>
    ↓ stream()
Stream<Account>
    ↓ map(Account::getBalance)
Stream<BigDecimal>
    ↓ reduce(BigDecimal.ZERO, ...)
BigDecimal
```

## 5. Optional reduction

```java
public Optional<BigDecimal> calculateBalanceOptional() {
    Optional<BigDecimal> balance = bank.getAccounts()
            .stream()
            .map(acc -> acc.getBalance())
            .reduce((a, b) -> a.add(b));

    return balance;
}
```

Test an Optional total of 14000 with:

```java
assertEquals(Optional.of(new BigDecimal("14000")), balance);
```

Don't compare a `BigDecimal` with an `Optional<BigDecimal>`.

When the bank has no accounts, one-argument reduction returns:

```java
Optional.empty()
```

JUnit check:

```java
assertTrue(result.isEmpty());
```

## 6. Filter + map + reduce: savings balance

```java
public BigDecimal calculateSavingBalance() {
    Predicate<Account> saving =
            account -> account.getAccountType() == AccountType.SAVINGS;

    BigDecimal balance = bank.getAccounts()
            .stream()
            .filter(saving)
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

    return balance;
}
```

Pipeline:

```text
All accounts
  ↓ filter(SAVINGS)
Savings accounts only
  ↓ map(balance)
Stream<BigDecimal>
  ↓ reduce(sum)
Total savings balance
```

- `filter()` selects matching accounts.
- `map()` extracts each balance.
- `reduce()` adds the balances.

## 7. `reduce()` for multiplication

```java
public int calculateProduct(List<Integer> numbers) {
    int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);

    return product;
}
```

Shorter version:

```java
public int calculateProduct(List<Integer> numbers) {
    return numbers.stream()
            .reduce(1, (a, b) -> a * b);
}
```

Example:

```java
calculateProduct(List.of(1, 2, 4, 6, 7)); // 336
```

Identity for multiplication is `1`. Therefore:

```java
calculateProduct(List.of()); // returns 1
```

Example JUnit test:

```java
@Test
public void calculateProductEmptyListTest() {
    int product = stream.calculateProduct(List.of());

    assertEquals(1, product);
}
```

## 8. Identity values

| Operation | Identity |
|---|---:|
| Addition | `0` |
| Multiplication | `1` |

An identity leaves the value unchanged:
- `0 + x = x`
- `1 * x = x`

## 9. Stream operations reviewed

| Operation | Category | Purpose |
|---|---|---|
| `stream()` | Stream creation | Starts stream processing |
| `filter()` | Intermediate | Selects matching elements |
| `map()` | Intermediate | Transforms each element |
| `sorted()` | Intermediate | Sorts elements |
| `min()` / `max()` | Terminal | Finds minimum/maximum |
| `reduce()` | Terminal | Combines elements into one result |
| `collect()` | Terminal | Collects elements |
| `count()` | Terminal | Counts elements |
| `findFirst()` | Terminal | Returns first element as Optional |

## 10. Common mistakes
1. Using `+` with `BigDecimal` instead of `.add()`.
2. Forgetting the identity in the two-argument form.
3. Comparing `BigDecimal` directly with `Optional<BigDecimal>` in tests.
4. Forgetting one-argument `reduce()` returns an `Optional`.
5. Forgetting empty-stream behavior.
6. Using `0` as the multiplication identity instead of `1`.

## 11. Day 4 practical work
- Total balance using `map()` and `reduce()`.
- JUnit test for total balance.
- One-argument reduction returning `Optional<BigDecimal>`.
- Optional result test.
- Empty-bank test with `Optional.empty()`.
- Savings balance using `filter()`, `map()`, and `reduce()`.
- Integer multiplication using `reduce()`.
- Product test and empty-list test.

## Day 4 checkpoint
**Status:** Complete once tests pass and Git commit/push are done.

### Git workflow

```bash
git status
git add .
git commit -m "Week 2 Day 4: Practice Stream reduce operations"
git push
```

Do not start Day 5 until notes and Git commit/push are complete.

## Next session — Week 2 Day 5
Start with a quick recall of:
- `reduce()` with and without identity
- `Optional.empty()`
- `filter() + map() + reduce()`
- Identity values for addition and multiplication
