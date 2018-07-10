package com.github.wreulicke.compatible4j.processor;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;

class CompatibleAnnotationProcessorTest {


  @Test
  public void test() {
    CompatibleAnnotationProcessor sut = new CompatibleAnnotationProcessor();

    Truth.assert_()
      .about(JavaSourcesSubjectFactory.javaSources())
      .that(Arrays.asList(JavaFileObjects.forResource("CompatibleA.java"), JavaFileObjects.forResource("CompatibleB.java")))
      .processedWith(sut)
      .failsToCompile()
      .withErrorCount(2);
  }
}
