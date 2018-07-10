
Compatible4j
=========================================

Provides automaticalliy compatibility detection between beans.


## Usage

`@Compatible` annotated your class will be automatically verified by `compatible4j`.

See below examples.

```java
class A {
  Integer field;
}

@Comaptible(A.class)
class B { // B is similar to A.
  Integer field;
}

@Comaptible(A.class)
class C { // C is not similar to A.
  String field;
}

@Comaptible(A.class)
class D { // D is similar to A.
  Integer field;

  String otherField;
}

```
