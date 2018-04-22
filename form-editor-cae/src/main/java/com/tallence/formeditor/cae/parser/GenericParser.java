/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tallence.formeditor.cae.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.annotations.Configurer;
import com.tallence.formeditor.cae.annotations.Initializer;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic parser implementation for form elements and configured types.
 *
 * @param <T> the class for which this parser was created.
 */
@Component
@Scope("prototype")
public class GenericParser<T> {

  private final Class<T> forClass;
  private List<ConfiguredPropertyDescriptor> descriptors;
  private List<ElementInitializer> initializers;


  public GenericParser(final Class<T> forClass, final Collection<ElementPropertyConfigurer> configurers, final Collection<ElementInitializer> initializers) {
    this.forClass = forClass;
    configureParser(configurers, initializers);
  }


  private void configureParser(final Collection<ElementPropertyConfigurer> configurers, final Collection<ElementInitializer> initializers) {
    this.initializers = initializers.stream()
        .filter(initializer -> initializer.getClass().isAnnotationPresent(Initializer.class))
        .filter(initializer -> forClass.equals(initializer.getClass().getAnnotation(Initializer.class).value()))
        .collect(Collectors.toList());
    this.descriptors = getAllFieldsForClass().stream()
        .filter(field -> field.isAnnotationPresent(Configured.class))
        .map(field -> createConfiguredPropertyDescriptor(field, configurers, initializers))
        .collect(Collectors.toList());
  }


  private ConfiguredPropertyDescriptor createConfiguredPropertyDescriptor(final Field field, final Collection<ElementPropertyConfigurer> configurers, final Collection<ElementInitializer> initializers) {
    final String fieldName = field.getName();
    final Configured fieldAnnotation = field.getAnnotation(Configured.class);
    final String key = fieldAnnotation.key().isEmpty() ? fieldName : fieldAnnotation.key();

    final Class<?> type = new BeanWrapperImpl(forClass).getPropertyType(field.getName());

    final ElementPropertyConfigurer configurer = type.isAnnotationPresent(Configured.class) ? createParserConfigurer(type, configurers, initializers)
        : findMatchingConfigurer(type, configurers, fieldAnnotation.configurer());

    return new ConfiguredPropertyDescriptor(key, fieldName, configurer);
  }


  private ElementPropertyConfigurer createParserConfigurer(final Class<?> type, final Collection<ElementPropertyConfigurer> configurers, final Collection<ElementInitializer> initializers) {
    final GenericParser<?> parser = new GenericParser<>(type, configurers, initializers);

    return (configurationSource, key, wrapper, propertyName) -> {
      if (configurationSource.get(key) != null) {
        wrapper.setPropertyValue(propertyName, parser.createElement(configurationSource.getStruct(key), wrapper.getWrappedInstance()));
      }
    };
  }


  private ElementPropertyConfigurer findMatchingConfigurer(final Class<?> type, final Collection<ElementPropertyConfigurer> configurers, Class configurerHint) {
    return configurers.stream()
        .filter(configurer -> canConfigurePropertyOfType(type, configurer))
        .filter(configurer -> configurerHint.equals(Configured.class) || configurerHint.equals(configurer.getClass()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No configurer for " + type + " found."));
  }


  private boolean canConfigurePropertyOfType(Class<?> type, ElementPropertyConfigurer configurer) {
    type = ClassUtils.resolvePrimitiveIfNecessary(type);
    final Configurer annotation = configurer.getClass().getAnnotation(Configurer.class);

    return annotation.value().equals(type);
  }


  private List<Field> getAllFieldsForClass() {
    final List<Field> result = new LinkedList<>();
    recursiveGetFields(forClass, result);
    return result;
  }


  private void recursiveGetFields(Class type, List<Field> result) {
    if (type.equals(Object.class)) {
      return;
    }
    Collections.addAll(result, type.getDeclaredFields());
    recursiveGetFields(type.getSuperclass(), result);
  }


  private T createInstance(final Object parent) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    final Constructor<T> noArgsConstructor = ClassUtils.getConstructorIfAvailable(forClass);
    if (noArgsConstructor != null) {
      return noArgsConstructor.newInstance();
    }

    if (parent != null) {
      final Constructor<T> parentArgConstructor = ClassUtils.getConstructorIfAvailable(forClass, parent.getClass());
      if (parentArgConstructor != null) {
        return parentArgConstructor.newInstance(parent);
      }
    }

    throw new IllegalStateException("The " + forClass + " has no matching constructor (no arguments, or one argument accepting the parent");
  }


  private T createElement(final Struct elementData, final Object parent) {
    final T instance;
    try {
      instance = createInstance(parent);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new IllegalStateException("Could not instantiate instance of " + forClass, e);
    }
    final BeanWrapperImpl wrapper = new BeanWrapperImpl(instance);

    if (descriptors != null) {
      //noinspection unchecked
      descriptors.forEach(descriptor -> descriptor.configurer.configure(elementData, descriptor.key, wrapper, descriptor.propertyName));
    }

    if (initializers != null) {
      //noinspection unchecked
      initializers.forEach(initializer -> initializer.initialize(instance, elementData));
    }

    return instance;
  }


  public T createElement(final Struct elementData) {
    return this.createElement(elementData, null);
  }


  private static class ConfiguredPropertyDescriptor {
    private final String key;
    private final String propertyName;
    private final ElementPropertyConfigurer configurer;

    ConfiguredPropertyDescriptor(final String key, final String propertyName, final ElementPropertyConfigurer configurer) {
      this.key = key;
      this.propertyName = propertyName;
      this.configurer = configurer;
    }
  }
}
