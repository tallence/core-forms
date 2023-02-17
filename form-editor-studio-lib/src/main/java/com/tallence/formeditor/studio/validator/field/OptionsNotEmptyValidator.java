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

import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.elements.FieldWithOptions;
import org.springframework.stereotype.Component;

import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_GROUP_ELEMENTS_PROPERTY_NAME;

/**
 * Validates, that CheckBoxes, DropDowns and RadioButtons have at least one groupElement.
 */
@Component
public class OptionsNotEmptyValidator extends AbstractFormValidator<FieldWithOptions<?>> {

  public OptionsNotEmptyValidator() {
    super(FieldWithOptions.class);
  }

  @Override
  void validateField(FieldWithOptions<?> formElement, String action, Issues issues) {

    if (formElement.getOptions().isEmpty()) {

      String messageKey = formElement.getClass().getSimpleName().toLowerCase() + "_missing_options";
      addErrorIssue(issues, formElement.getStructId(), FORM_GROUP_ELEMENTS_PROPERTY_NAME, messageKey, formElement.getName());
    }

  }

}
