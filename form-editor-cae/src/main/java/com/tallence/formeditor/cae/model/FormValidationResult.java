/*
 * Copyright 2020 Tallence AG
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

package com.tallence.formeditor.cae.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FormValidationResult {

  private final String globalError;
  private final Map<String, List<String>> fieldErrors;

  public static FormValidationResult globalError(String errorMessage) {
    return new FormValidationResult(errorMessage, Collections.emptyMap());
  }

  public static FormValidationResult fieldError(String field, String errorMessage) {
    return new FormValidationResult(null, Collections.singletonMap(field, Collections.singletonList(errorMessage)));
  }

  public FormValidationResult(String globalError, Map<String, List<String>> fieldErrors) {
    this.globalError = globalError;
    this.fieldErrors = fieldErrors;
  }

  public String getGlobalError() {
    return globalError;
  }

  public Map<String, List<String>> getFieldErrors() {
    return fieldErrors;
  }
}
