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

package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;

/**
 * Defines a common interface for form editor field validators.
 * They are used to check if a formField fulfills all requirements (validation, see {@link #validateField}.
 *
 * And they CAN be used to check, if any field matches specific requirements, e.g. to make sure, specific fields are configured,
 * see {@link #checkFormFieldMatch} and {@link #handleFieldMatches}.
 */
public interface FieldValidator {

  /**
   * Returns the field type(s) this validator acts on.
   */
  boolean responsibleFor(String fieldType, Struct formElementData);

  /**
   * Validate a single field configuration.
   *
   * @param id        the id of the form element
   * @param fieldData the sub struct of formElements for a field.
   * @param action    the current action of the form (some validators need this).
   * @param issues    the Issues object for the current validation request.
   */
  void validateField(String id, Struct fieldData, String action, Issues issues);

  /**
   * This method will only be called if {@link #responsibleFor} returned true for the current form field.
   *
   * If the current field matches the implemented check, it can be registered at {@link FormFieldMatch} which will later
   * be processed by {@link #handleFieldMatches}
   *
   * To make complex checks possible, many params are passed:
   *
   * @param id the id of the form field
   * @param fieldData the struct of the form field
   * @param fieldMatch matches can be registered here
   * @param formDoc the content object of the current form document
   * @param action the action of the form document
   */
  default void checkFormFieldMatch(String id, Struct fieldData, FormFieldMatch fieldMatch, Content formDoc, String action) {
    //do nothing if not implemented
  }

  /**
   * Checks, if a {@link FormFieldMatch} from {@link #checkFormFieldMatch} has been registered. An {@link Issues}
   * can be added if no match was found.
   *
   * @param fieldMatch the source.
   * @param formDoc the current form document for complex requirement checks
   */
  default void handleFieldMatches(FormFieldMatch fieldMatch, Content formDoc, Issues issues) {
    //do nothing if not implemented
  }

}
