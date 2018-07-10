package com.github.wreulicke.compatible4j.processor;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.auto.common.MoreElements.getAnnotationMirror;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import org.kohsuke.MetaInfServices;

import com.github.wreulicke.compatible4j.annotation.Compatible;

@MetaInfServices(Processor.class)
public class CompatibleAnnotationProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    if (env.processingOver()) {
      return true;
    }
    Types typeUtils = this.processingEnv.getTypeUtils();
    Set<? extends Element> e = env.getElementsAnnotatedWith(Compatible.class);
    for (TypeElement typeElement : ElementFilter.typesIn(e)) {
      TypeMirror typeMirror = getClasseFromAnnotation(typeElement, Compatible.class, "value");
      Element element = typeUtils.asElement(typeMirror);

      Map<Name, VariableElement> fieldMap = ElementFilter.fieldsIn(typeElement.getEnclosedElements())
        .stream()
        .collect(Collectors.toMap(VariableElement::getSimpleName, Function.identity()));
      List<VariableElement> otherFields = ElementFilter.fieldsIn(element.getEnclosedElements());
      boolean isNotCompatible = false;
      for (VariableElement otherField : otherFields) {
        Name name = otherField.getSimpleName();
        VariableElement field = fieldMap.get(name);
        if (field == null) {
          this.processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR, typeElement.getSimpleName() + " has not `" + name + "` field of " + otherField.asType(),
              typeElement);
          isNotCompatible = true;
        }
        else if (!otherField.asType()
          .equals(field.asType())) {
          this.processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR, "Type of `" + field.getSimpleName() + "` must equal to " + otherField.asType(), field);
          isNotCompatible = true;
        }
      }
      if (isNotCompatible) {
        this.processingEnv.getMessager()
          .printMessage(Diagnostic.Kind.ERROR, "Incompatible", typeElement);
      }
    }
    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton("com.github.wreulicke.compatible4j.annotation.Compatible");
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  public static TypeMirror getClasseFromAnnotation(Element element, Class<? extends Annotation> annotationType, String argName) {
    AnnotationMirror am = getAnnotationMirror(element, annotationType).get();
    AnnotationValue av = getAnnotationValue(am, argName);
    return TO_TYPE.visit(av);
  }

  private static final AnnotationValueVisitor<List<TypeMirror>, Void> TO_LIST_OF_TYPE = new SimpleAnnotationValueVisitor6<List<TypeMirror>, Void>() {
    @Override
    public List<TypeMirror> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
      return vals.stream()
        .map(TO_TYPE::visit)
        .collect(Collectors.toList());
    }
  };

  private static final AnnotationValueVisitor<TypeMirror, Void> TO_TYPE = new SimpleAnnotationValueVisitor6<TypeMirror, Void>() {
    @Override
    public TypeMirror visitType(TypeMirror t, Void aVoid) {
      return t;
    }
  };
}
