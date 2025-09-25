# Weird Lambda

This repository is used to demonstrate some weird behaviors of lambda functions in Java.

## Setup

To run the code, you need to have Java 8 or later installed on your machine. Then run command `gradle run`.

## Code Explanation

The main class [`Main`](src/main/java/cool/muyucloud/lambda/Main.java) has 2 sets of overloaded methods, which all take
a lambda, named `foo` and`bar`.

All the methods print out which method is called, based on the lambda's parameter and return types.

A test class `Test` is in use to demonstrate the return type of the lambda.

### 1. `foo`: `void -> void` vs. `void -> Object`

The `foo` methods accept a lambda that takes nothing, while the return value is different: one returns nothing (
`void`), the other returns `Object`.

In the `main` method, we call these methods with different lambda expressions to see how Java resolves the overloads.

```java
foo(() -> new Test());
foo(() -> {});
```

What we get is:

```log
void -> o
void -> void
```

That means Java successfully resolves both overloads. The first call to `foo` matches the `void -> Object` overload,
while the second call matches the `void -> void` overload.

This is quite fairly expected, as the lambda `() -> new Test()` clearly returns an `Object`, while
`() -> {}` returns nothing.

### 2. `bar`: `Object -> void` vs. `Object -> Object`

The `bar` methods accept a lambda that takes an `object` parameter and also have different return types: one returns
nothing (`void`), the other returns `Object`.

Just like `foo`, we call these methods with different lambda expressions in the `main` method.

```java
bar((o) -> new Test(o)); // This one fails to compile
bar((o) -> {});
```

However, this time we encounter a compilation error:

```log
error: reference to bar is ambiguous
    bar((o) -> new Test());
    ^
  both method bar(Consumer<Object>) in Main and method bar(Function<Object, Object>) in Main match
```

This means Java cannot decide which overload to use for the first call to `bar`. The lambda `o -> new Test()` could 
match both `Object -> void` and `Object -> Object`, leading to ambiguity.

This is quite unexpected, as we would assume that the return type of the lambda should help Java resolve the overloads.

## Some fixes

To resolve the ambiguity, we can use 2 different approaches:

a. **Explicit Casting**: We can cast the lambda to the desired functional interface.

```java
bar((Function<Object, Object>) o -> new Test(o));
```
*I personally don't like casting, as it looks so "violent"*

b. **Using full-body lambda**: We can use a full-body lambda with an explicit return statement.

```java
bar(o -> {
    return new Test();
});
```