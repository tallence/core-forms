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

import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Interface for all FormElements.
 */
public interface FormElement<T> {

  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  /**
   * @return a unique technical name for this form element which is human readable.
   */
  String getTechnicalName();

  boolean isMandatory();

  String getHint();

  void setHint(String hint);

  T getValue();

  void setValue(T value);

  Class<T> getType();

  /**
   * Validates the current FormElement's value.
   * @return the validation result
   */
  List<String> getValidationResult();

  /**
   * Returns the form Element value serialized as String
   */
  String serializeValue();

  /**
   * Fills the elements values into given formData-Map
   */
  void fillFormData(Map<String, String> formData);

  void setValue(MultiValueMap<String, String> postData, HttpServletRequest request);
}
