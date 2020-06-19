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

import edu.umd.cs.findbugs.annotations.Nullable;
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

  String getPlaceholder();

  void setPlaceholder(String placeholder);

  @Nullable
  AdvancedSettings getAdvancedSettings();

  void setAdvancedSettings(AdvancedSettings settings);

  @Nullable
  T getValue();

  void setValue(T value);

  Class<T> getType();

  /**
   * Make sure to check {@link #dependencyFulfilled} before calling this method. The field might dependent on another field which
   * is empty or has a wrong value.
   *
   * Validates the current FormElement's value.
   * @return the validation result
   */
  List<String> getValidationResult();

  /**
   * Checks, if the dependent field's value matches this field's visibility config.
   * @see AdvancedSettings#getDependentElementId()
   */
  boolean dependencyFulfilled(List<FormElement> allElements);

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
