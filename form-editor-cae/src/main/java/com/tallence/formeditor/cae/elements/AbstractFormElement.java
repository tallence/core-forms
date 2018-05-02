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

import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract Element used by all {@link FormElement}s.
 * It provides base fields, used by all elements, like id, name and hint.
 * And base functions like validation and serialization.
 */
public abstract class AbstractFormElement<T, V extends Validator<T>> implements FormElement<T> {

  static final String GROUP_ELEMENTS_KEY = "groupElements";

  private String id;

  @Configured
  private String name;

  @Configured
  private String hint;

  @Configured
  private V validator;

  private T value;
  private final Class<T> type;

  public AbstractFormElement(Class<T> type) {
    this.type = type;
  }

  @Override
  public List<String> getValidationResult() {
    return this.validator != null ? this.validator.validate(getValue()) : Collections.emptyList();
  }


  @Override
  public boolean isMandatory() {
    return this.validator != null && this.validator.isMandatory();
  }


  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    List<String> values = postData.get(getId());

    if (values == null || values.isEmpty()) {
      setValue(null);
    } else if (getType().equals(List.class)) {
      //noinspection unchecked
      setValue((T) values);
    } else if (values.size() == 1) {
      //noinspection unchecked
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
    return null;
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
    return this.id;
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
}
