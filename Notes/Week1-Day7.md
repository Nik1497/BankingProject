# Week 1 — Day 7: Maven + JUnit 5 Unit Testing

## 1. Maven Basics
Maven is a Java build and dependency-management tool. It manages dependencies, compiles code, runs tests, and provides a standard project structure.

`pom.xml` means **Project Object Model**. It contains project configuration, dependencies, compiler/build configuration, etc.

Standard structure:
```text
project-root/
├── pom.xml
└── src/
    ├── main/java/com/banking/
    └── test/java/com/banking/
```

Production code belongs under `src/main/java`; test code belongs under `src/test/java`.

## 2. JUnit 5
JUnit is a Java testing framework used to write and execute automated tests.

Dependency used:
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.12.2</version>
    <scope>test</scope>
</dependency>
```

`scope=test` means the dependency is needed for tests rather than production code.

## 3. @Test
`@Test` tells JUnit that a method is a test method.

```java
@Test
void shouldDepositAmountSuccessfully() {
    account.deposit(new BigDecimal("500"));
    assertEquals(new BigDecimal("1500"), account.getBalance());
}
```

## 4. @BeforeEach and Test Isolation
`@BeforeEach` runs before every test method.

```java
Account account;
Customer test1;

@BeforeEach
void setup() {
    test1 = new Customer(1, "test1", "test1@gmail.com", "1234567890");
    account = new Account(
        "8888800001",
        new BigDecimal("1000"),
        AccountType.SAVINGS,
        test1
    );
}
```

Every test therefore starts with a fresh account whose balance is 1000.

This is important because tests should be independent. A withdrawal in one test must not affect another test.

## 5. AAA Pattern
A common unit-test structure is **AAA**:
- **Arrange** — prepare data/state
- **Act** — perform the operation
- **Assert** — verify the result

Example:
```java
@Test
void shouldDepositAmountSuccessfully() {
    // Arrange — fresh account from @BeforeEach

    // Act
    account.deposit(new BigDecimal("500"));

    // Assert
    assertEquals(new BigDecimal("1500"), account.getBalance());
}
```

## 6. assertEquals()
`assertEquals(expected, actual)` verifies expected and actual values.

For `BigDecimal`, equality follows `BigDecimal.equals()`, so scale matters.

```java
new BigDecimal("10.0").equals(new BigDecimal("10.00")) // false
```

But:
```java
new BigDecimal("10.0").compareTo(new BigDecimal("10.00")) // 0
```

Therefore, `10.0` and `10.00` are numerically equal but are not equal according to `BigDecimal.equals()`.

## 7. assertThrows()
`assertThrows()` verifies that an operation throws the expected exception.

```java
assertThrows(
    InsufficientBalanceException.class,
    () -> account.withdraw(new BigDecimal("1500"))
);
```

The first argument is the expected exception type.

The lambda is the operation JUnit should execute.

If no exception is thrown, the test fails. If the wrong exception type is thrown, the test also fails.

## 8. Why the Lambda?
Compare:

```java
account.withdraw(new BigDecimal("1500"))
```

This executes immediately.

Whereas:

```java
() -> account.withdraw(new BigDecimal("1500"))
```

represents an action that can be executed later.

JUnit receives the lambda and executes it itself, allowing JUnit to catch and verify the thrown exception.

Conceptually:
```text
assertThrows()
    ↓
receives expected exception + lambda
    ↓
JUnit executes lambda
    ↓
withdraw(1500)
    ↓
exception thrown
    ↓
JUnit checks exception type
    ↓
test passes
```

## 9. Testing Exception Messages
`assertThrows()` returns the actual exception object.

```java
InsufficientBalanceException exception = assertThrows(
    InsufficientBalanceException.class,
    () -> account.withdraw(new BigDecimal("1500"))
);

assertEquals(
    "Insufficient balance",
    exception.getMessage()
);
```

This verifies both the exception type and its message.

## 10. Current Account Tests
The Account tests cover:
1. Successful withdrawal
2. Withdrawal with insufficient balance
3. Withdrawal amount of zero
4. Withdrawal amount being negative
5. Successful deposit
6. Negative deposit amount
7. Zero deposit amount

All current tests passed successfully.

## 11. Important Interview Q&A

### Q1. What is JUnit?
JUnit is a Java testing framework used to write and execute automated tests.

### Q2. Why use unit tests?
They verify individual pieces of application logic and help detect regressions when code changes.

### Q3. What does @Test do?
It tells JUnit that a method should be executed as a test.

### Q4. What does @BeforeEach do?
It runs before every test method and is commonly used to create fresh test data/state.

### Q5. Why is test isolation important?
Each test should be independent so changes made by one test do not affect another.

### Q6. What is AAA?
Arrange → Act → Assert.

### Q7. What does assertEquals() do?
It verifies that expected and actual values are equal.

### Q8. What does assertThrows() do?
It verifies that executing an operation throws the expected exception type.

### Q9. Why use a lambda with assertThrows()?
The lambda passes the operation to JUnit without executing it immediately. JUnit then executes it and verifies the exception.

### Q10. What happens if no exception is thrown?
The test fails because the expected exception was not thrown.

### Q11. What happens if a different exception is thrown?
The test fails because the thrown type does not match the expected type.

### Q12. BigDecimal.equals() vs compareTo()?
`equals()` considers value and scale; `compareTo()` compares numerical value and ignores scale differences.

### Q13. Why BigDecimal for banking amounts?
It provides precise decimal arithmetic appropriate for monetary calculations; `double` can introduce floating-point precision issues.

### Q14. Why shouldn't tests share the same Account object?
Tests can mutate account state, causing later tests to depend on earlier tests.

### Q15. What does assertThrows() return?
The actual exception object that was thrown, allowing inspection of its message and other properties.

## 12. Key Takeaways
```text
Maven
  ↓
Dependency management + build

JUnit
  ↓
Automated Java testing

@BeforeEach
  ↓
Fresh setup before every test

@Test
  ↓
Individual test

AAA
  ↓
Arrange → Act → Assert

assertEquals()
  ↓
Expected vs actual

assertThrows()
  ↓
Expected exception

Lambda
  ↓
Pass executable behavior without executing it immediately

BigDecimal
  ↓
Precise monetary values
```

## Day 7 Status
**Completed successfully — all current tests passed.**
