# Week 1 — Day 6

## Goal

Understand interfaces, abstraction, programming to an interface, loose coupling, dependency injection, method overriding, and runtime polymorphism using the banking application.

## 1. Interface

An interface is **not a class**. It is a Java construct used to define a contract.

```text
Interface → WHAT
Implementation class → HOW
```

Example:

```java
public interface BankingService {
    void transfer(
        String sourceAccountNum,
        String destinationAccountNum,
        BigDecimal amount
    );
}
```

The interface says that a banking service must provide `transfer()`. The implementation provides the actual logic.

## 2. `implements`

A class uses `implements` when it agrees to follow an interface contract.

```java
public class BankService implements BankingService {

    @Override
    public void transfer(
        String sourceAccountNum,
        String destinationAccountNum,
        BigDecimal amount
    ) {
        // actual transfer logic
    }
}
```

## 3. Why use interfaces?

Without an interface:

```java
BankService service = new BankService(bank);
```

`Main` directly depends on the concrete implementation.

With an interface:

```java
BankingService service = new BankService(bank);
```

`Main` depends on the contract instead of the concrete implementation.

Later:

```java
BankingService service =
    new CorporateBankService(bank);
```

The caller can still use:

```java
service.transfer(...);
```

This reduces coupling and makes implementations easier to replace.

## 4. Programming to an Interface

Programming to an interface means using an interface/abstraction as the reference type rather than directly depending on a concrete implementation.

```java
BankingService service = new BankService(bank);
```

Here:

```text
Reference type → BankingService
Actual object  → BankService
```

## 5. What methods can be called?

The reference type determines which methods are available at compile time.

If `BankingService` contains only:

```java
void transfer(...);
```

then:

```java
service.transfer(...);      // valid
```

If `getAccount()` is not declared in `BankingService`:

```java
service.getAccount(...);    // compile-time error
```

Even if the actual `BankService` class has `getAccount()`.

## 6. `@Override`

```java
@Override
public void transfer(...) {
    ...
}
```

`@Override` tells Java that the method is intended to implement/override a method from the interface or parent type. It also helps catch incorrect method signatures.

## 7. Reference Type vs Actual Object

For:

```java
BankingService service =
    new CorporateBankService(bank);
```

```text
Reference type → BankingService
Actual object  → CorporateBankService
```

The reference type determines what can be called at compile time.

The actual object determines which overridden implementation runs at runtime.

## 8. Runtime Polymorphism

Polymorphism allows one common interface/reference type to represent different implementation objects.

```text
             BankingService
              /                       /                BankService      CorporateBankService
         ↓                   ↓
    transfer()          transfer()
```

If:

```java
BankingService service =
    new BankService(bank);

service.transfer(...);
```

then `BankService.transfer()` runs.

If:

```java
BankingService service =
    new CorporateBankService(bank);

service.transfer(...);
```

then `CorporateBankService.transfer()` runs.

### Key rule

> **Reference type decides what method can be called; actual object decides which overridden implementation runs.**

## 9. Dependency Injection

`BankService` needs a `Bank`.

Instead of creating a new Bank inside the service, we pass the existing Bank through the constructor:

```java
public BankService(Bank bank) {
    this.bank = bank;
}
```

Then:

```java
Bank bank = new Bank();
BankService service = new BankService(bank);
```

This is a simple example of dependency injection.

Meaning:

> Give an object the dependency it needs from outside instead of making the object create it itself.

## 10. Why should BankService not create its own Bank?

If Main has Bank A containing accounts and BankService creates Bank B, the service would be working with a different collection.

Instead:

```text
Main
 ↓
Bank A
 ↑
 │
BankService
```

Both use the same Bank instance.

# Interview Questions & Answers

## Q1. What is an interface?

An interface is a Java construct that defines a contract for classes that implement it. It specifies operations that the implementation must provide.

## Q2. Is an interface a class?

No. An interface is a separate Java construct used to define a contract/abstraction.

## Q3. Difference between an interface and a class?

An interface primarily defines a contract — what operations are available. A class provides implementation and can contain state and behavior.

Easy rule:

```text
Interface = WHAT
Class = HOW
```

## Q4. What does `implements` mean?

`implements` means a class agrees to follow the contract defined by an interface and provides implementations for its required methods.

## Q5. Why use interfaces?

Interfaces allow code to depend on abstractions rather than concrete implementations. This reduces coupling and makes implementations easier to replace.

## Q6. What is programming to an interface?

It means using an interface or abstraction as the reference type instead of directly depending on a concrete implementation.

Example:

```java
BankingService service = new BankService(bank);
```

## Q7. What is loose coupling?

Loose coupling means a component has minimal dependency on the concrete details of another component. Interfaces help because code can depend on a contract rather than a specific implementation.

## Q8. What is polymorphism?

Polymorphism allows a reference of a common interface/parent type to refer to different implementation objects, with the appropriate overridden implementation selected at runtime.

## Q9. What is runtime polymorphism?

Runtime polymorphism occurs when the implementation that executes is determined by the actual object at runtime.

Example:

```java
BankingService service =
    new CorporateBankService(bank);

service.transfer(...);
```

`CorporateBankService.transfer()` executes.

## Q10. Difference between reference type and actual object type?

For:

```java
BankingService service =
    new CorporateBankService(bank);
```

```text
Reference type → BankingService
Actual object  → CorporateBankService
```

The reference type determines what methods can be called, while the actual object determines which overridden implementation runs.

## Q11. Can we call a method through an interface reference if it is not declared in the interface?

No. Even if the implementation class has that method, it cannot be called through the interface reference unless the method is declared in the interface.

## Q12. What is `@Override`?

`@Override` tells the compiler that a method is intended to override or implement a method from a parent type or interface. It also helps catch incorrect method signatures.

## Q13. What is dependency injection?

Dependency injection means providing an object's required dependency from outside instead of making the object create that dependency itself.

Example:

```java
BankService service = new BankService(bank);
```

## Q14. Why shouldn't BankService create its own Bank?

Because it could create a different Bank instance from the one containing the application's accounts. Passing the existing Bank ensures both objects work with the same state.

## Q15. What is the reference type here?

```java
BankingService service =
    new CorporateBankService(bank);
```

Answer:

```text
Reference type → BankingService
Actual object → CorporateBankService
```

## Q16. Which transfer implementation executes?

```java
BankingService service =
    new BankService(bank);

service.transfer(...);
```

Answer:

> `BankService.transfer()` executes because the actual object is `BankService`.

## Q17. What happens if the object is changed?

```java
BankingService service =
    new CorporateBankService(bank);

service.transfer(...);
```

Answer:

> `CorporateBankService.transfer()` executes because the actual object is `CorporateBankService`.

# Key Rules

```text
Interface = WHAT
Implementation = HOW

Reference type
→ What methods can I call?

Actual object
→ Which overridden implementation runs?

implements
→ Class follows interface contract

Programming to interface
→ Depend on abstraction, not concrete implementation

Polymorphism
→ Same interface/reference, different implementations

Dependency Injection
→ Give dependencies from outside
```

# Day 6 Summary

We changed:

```java
BankService service = new BankService(bank);
```

to:

```java
BankingService service = new BankService(bank);
```

without changing transfer behavior.

This demonstrated why interfaces are useful.

The design can support different implementations:

```text
             BankingService
              /                       /                BankService      CorporateBankService
         ↓                   ↓
    transfer()          transfer()
```

The caller can use the same contract:

```java
service.transfer(...);
```

while the actual implementation can vary.

**Week 1 Day 6 — COMPLETE**
