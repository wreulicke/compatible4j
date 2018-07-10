
Compatible4j
=========================================

Provides automaticalliy compatibility detection between beans.

## Install 

```gradle
dependencies {
  compile 'com.github.wreulicke.compatible4j:compatible4j-annotation:0.0.1'
  annotationProcessor 'com.github.wreulicke.compatible4j:compatible4j-processor:0.0.1'
}
```


## Usage

`@Compatible` annotated your class will be automatically verified by `compatible4j`.

See below examples.

```java
class A {
  Integer field;
}

@Comaptible(A.class)
class B { // B is compatible to A.
  Integer field;
}

@Comaptible(A.class)
class C { // C is not compatible to A.
  String field;
}

@Comaptible(A.class)
class D { // D is compatible to A.
  Integer field;

  String otherField;
}

```
