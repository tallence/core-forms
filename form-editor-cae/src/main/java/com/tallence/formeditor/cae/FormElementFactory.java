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

package com.tallence.formeditor.cae;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.annotations.FormElementDefinition;
import com.tallence.formeditor.cae.elements.AbstractFormElement;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.parser.ElementInitializer;
import com.tallence.formeditor.cae.parser.ElementPropertyConfigurer;
import com.tallence.formeditor.cae.parser.GenericParser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for creating form elements from configuration.
 *
 * The form element factory provides a method {@link #createFormElement(Struct, String)} for creating instances of
 * {@link FormElement} from a {@link Struct} containing the element configuration. The type of form element is
 * determined by a type key which has to be present within the the element configuration as a string value (see
 * {@link #FORM_DATA_KEY_TYPE}).
 *
 * <h3>Type Resolution</h3>
 *
 * Available type keys are determined when <code>FormElementFactory</code> is instantiated. For every subclass of
 * {@link AbstractFormElement} which is available in the Spring application context at that time, a type key is
 * associated with a {@link GenericParser} for the respective type. Type keys for form elements default to the simple
 * class name but can be overridden via {@link FormElementDefinition}.
 */
@Component
public class FormElementFactory {


  private static final String FORM_DATA_KEY_TYPE = "type";

  private final Map<String, GenericParser> parsersByTypeKey;

  public FormElementFactory(final Collection<AbstractFormElement> formElements, final Collection<ElementPropertyConfigurer> configurers, final Collection<ElementInitializer> initializers) {
    this.parsersByTypeKey = formElements.stream()
        .map(AbstractFormElement::getClass)
        .collect(Collectors.toMap(
            this::determineKeyForElementClass,
            type -> new GenericParser<>(type, configurers, initializers)
        ));
  }


  private String determineKeyForElementClass(Class<? extends FormElement> forClass) {
    final FormElementDefinition annotation = forClass.getAnnotation(FormElementDefinition.class);
    if (annotation != null && !annotation.value().isEmpty()) {
      return annotation.value();
    }
    return forClass.getSimpleName();
  }


  <T extends FormElement> T createFormElement(Struct elementData, String id) {
    return parseType(elementData, id);
  }


  private <T extends FormElement> T parseType(Struct elementData, String id) {
    String type = elementData.getString(FORM_DATA_KEY_TYPE);

    @SuppressWarnings("unchecked")
    GenericParser<T> elementFactory = (GenericParser<T>) this.parsersByTypeKey.get(type);

    if (elementFactory == null) {
      throw new IllegalStateException("Unknown type: " + type);
    }

    final T element = elementFactory.createElement(elementData);
    element.setId(id);
    return element;
  }
}
