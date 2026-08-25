# Banking Platform --- Interview Notes

## Week 1 --- Day 1

### 1. What is Encapsulation?

**Answer:**

Encapsulation is the practice of keeping an object's state private and
providing controlled access to that state through methods. It prevents
direct modification of internal data and allows us to apply validation
or business rules when the data changes.

### 2. Why should fields be private?

Making fields `private` prevents other classes from directly accessing
or modifying an object's internal state.

For example, if a field were public:

``` java
public String customerEmail;
```

another class could directly change it:

``` java
customer.customerEmail = "wrong@email.com";
```

With a private field:

``` java
private String customerEmail;
```

we can expose controlled access through methods such as getters and
update methods.

This allows us to add validation or business rules before changing the
value.

### 3. Why don't we simply make fields public?

Public fields allow unrestricted direct modification by code that has
access to the object. This weakens encapsulation and makes it harder to
enforce validation and business rules.

Private fields provide better control over how the object's state is
read or changed.

### 4. What is the purpose of `this` in a constructor?

`this` refers to the current object.

For example:

``` java
this.customerId = customerId;
```

The left side, `this.customerId`, refers to the object's field.

The right side, `customerId`, refers to the constructor parameter.

So:

``` java
Customer c1 = new Customer(
    101,
    "Nikhil",
    "nikhil@gmail.com",
    "9876543210"
);
```

stores those values inside the `Customer` object's fields.

### 5. Why is `mobileNumber` a `String` instead of an `int`?

A mobile number is an identifier, not a value used for arithmetic.

Using `String` also allows values such as:

``` text
+919876543210
```

and avoids issues with leading zeros or numeric formatting.

### 6. Why use an enum for `AccountType`?

An enum provides a fixed set of valid values, such as:

``` java
SAVINGS
CURRENT
```

It is safer and clearer than using arbitrary strings such as `"saving"`,
`"Savings"`, or `"SAVING"`.

------------------------------------------------------------------------

## Code Concepts Covered Today Day 1


-   Classes and objects
-   Encapsulation
-   Private fields
-   Constructors
-   `this` keyword
-   Getter methods
-   Enums
-   Appropriate data types
- Class vs Object 
- new keyword 
- Constructor 
- Encapsulation
- Getters 
- Enum 
- String for identifiers
- BigDecimal for money 
- Object association 
- Why Account references Customer 
- Why we don't expose setBalance()
