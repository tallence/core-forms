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

package com.tallence.formeditor.cae.elements;

import com.tallence.formeditor.cae.validator.ValidationFieldError;
import com.tallence.formeditor.cae.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract Element used by all {@link FormElement}s.
 * It provides base fields, used by all elements, like id, name and hint.
 * And base functions like validation and serialization.
 */
public abstract class AbstractFormElement<T, V extends Validator<T>> implements FormElement<T> {

  private String id;
  private String name;
  private String hint;
  private String placeholder;
  private T value;
  private V validator;
  private AdvancedSettings settings;
  private final Class<T> type;

  public AbstractFormElement(Class<T> type) {
    this.type = type;
  }

  @Override
  public List<ValidationFieldError> getValidationResult() {
    return this.validator != null ? this.validator.validate(getValue()) : Collections.emptyList();
  }

  @Override
  public boolean dependencyFulfilled(List<FormElement> allElements) {
    if (settings != null && settings.isVisibilityDependent()) {
      return allElements.stream().anyMatch(this::dependentFieldMatch);
    }
    return true;
  }

  protected boolean dependentFieldMatch(FormElement candidate) {
    return candidate.getId().equals(settings.getDependentElementId())
            && candidate.getValue() != null
            && candidate.getValue().toString().equals(settings.getDependentElementValue());
  }

  @Override
  public boolean isMandatory() {
    return this.validator != null && this.validator.isMandatory();
  }


  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    List<String> values = postData.get(getTechnicalName());

    if (values == null || values.isEmpty()) {
      setValue(null);
    } else if (getType().equals(List.class)) {
      setValue((T) values);
    } else if (values.size() == 1) {
      setValue((T) values.get(0));
    } else {
      throw new IllegalStateException("Passed multiple values for " + getName());
    }
  }

  @Override
  public String serializeValue() {
    T value = getValue();
    if (value instanceof String) {
      return (String) value;
    } else if (value != null) {
      throw new IllegalStateException("This method should be overwritten for values which are != String!");
    }
    return "";
  }

  @Override
  public void fillFormData(Map<String, String> formData) {
    String serializedValue = serializeValue();
    if (StringUtils.isNotBlank(serializedValue)) {
      formData.put(getName(), serializedValue);
    }
  }

  @Override
  public String getId() {
    return Optional.ofNullable(getAdvancedSettings())
            .map(AdvancedSettings::getCustomId)
            .filter(StringUtils::isNotBlank)
            .orElse(this.id);
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getHint() {
    return this.hint;
  }

  @Override
  public void setHint(String hint) {
    this.hint = hint;
  }

  @Override
  public String getPlaceholder() {
    return this.placeholder;
  }

  @Override
  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  @Override
  public T getValue() {
    return this.value;
  }

  @Override
  public void setValue(T value) {
    this.value = value;
  }

  public V getValidator() {
    return this.validator;
  }

  public void setValidator(V validator) {
    this.validator = validator;
  }

  @Override
  public Class<T> getType() {
    return this.type;
  }

  @Override
  public String getTechnicalName() {
    return getClass().getSimpleName() + "_" + getId();
  }

  @Override
  public AdvancedSettings getAdvancedSettings() {
    return settings;
  }

  @Override
  public void setAdvancedSettings(AdvancedSettings settings) {
    this.settings = settings;
  }
}
