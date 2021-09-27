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
package com.tallence.formeditor.caas;

import com.coremedia.caas.wiring.ProvidesTypeNameResolver;
import com.coremedia.caas.wiring.TypeNameResolver;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.caas.adapter.FormEditorAdapterFactory;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.validator.Validator;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

/**
 * Configuration for the FormEditor Headless component.
 * It is mapping the {@link FormElement}'s and {@link Validator}'s classes simpleNames to a GrapgQL-Schema-Element.
 *
 * Custom additional FormElements and Validators might be added to the classPath as well and will be handled if they are
 * placed in the {@link com.tallence.formeditor.elements} or {@link com.tallence.formeditor.validator} packages.
 * For elements and validators outside this package, you will have to define your own {@link ProvidesTypeNameResolver}
 */
@Configuration(proxyBeanMethods = false)
@Import(value = {MultiSiteConfiguration.class,})
public class FormEditorConfig {

  @Bean
  public FormEditorAdapterFactory formEditorAdapter(FormElementFactory formElementFactory) {
    return new FormEditorAdapterFactory(formElementFactory);
  }

  @Bean
  public ProvidesTypeNameResolver providesFormBeanTypeNameResolver() {

    var reflections = new Reflections(new ConfigurationBuilder().forPackages(FormElement.class.getPackageName()));
    final var formElementTypes = reflections.getSubTypesOf(FormElement.class).stream()
            .map(Class::getSimpleName)
            .collect(toCollection(ArrayList::new));
    formElementTypes.add(FormElement.class.getSimpleName());

    return name -> formElementTypes.contains(name) ? Optional.of(true): Optional.empty();
  }

  @Bean
  public ProvidesTypeNameResolver providesFormValidatorTypeNameResolver() {

    var reflections = new Reflections(new ConfigurationBuilder().forPackages(Validator.class.getPackageName()));
    final var validatorTypes = reflections.getSubTypesOf(Validator.class).stream()
            .map(Class::getSimpleName)
            .collect(toCollection(ArrayList::new));
    validatorTypes.add(Validator.class.getSimpleName());
    validatorTypes.add("SizeValidator");

    return name -> validatorTypes.contains(name) ? Optional.of(true): Optional.empty();
  }

  @Bean
  public TypeNameResolver<FormElement<?>> formElementTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName + "Impl");
    };
  }

  @Bean
  public TypeNameResolver<Validator<?>> validatorTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName);
    };
  }
}
