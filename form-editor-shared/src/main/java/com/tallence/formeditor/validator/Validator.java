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

package com.tallence.formeditor.validator;

import com.tallence.formeditor.elements.FormElement;

import java.util.List;

/**
 * A Validator which validates the request form data for the {@link FormElement}.
 */
public interface Validator<T> {

  /**
   * Validates the given FormElement-Value
   * @return a List of Error-Codes or an empty list, if the validation succeeded.
   */
  List<ValidationFieldError> validate(T value);

  boolean isMandatory();
}
