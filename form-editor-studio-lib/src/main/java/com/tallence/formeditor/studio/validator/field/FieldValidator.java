/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;

import java.util.List;

/**
 * Defines a common interface for form editor fild validators.
 */
public interface FieldValidator {

  /**
   * Returns the field type(s) this validator acts on.
   */
  List<String> resonsibleFor();

  /**
   * Validate a single field configuration.
   *
   * @param fieldData the sub struct of formElements for a field.
   * @param action    the current action of the form (some validators need this).
   * @param issues    the Issues object for the current validation request.
   */
  void validateField(Struct fieldData, String action, Issues issues);

}
