package com.github.wreulicke.compatible4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
  ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Compatible {

  Class<?> value();

}
