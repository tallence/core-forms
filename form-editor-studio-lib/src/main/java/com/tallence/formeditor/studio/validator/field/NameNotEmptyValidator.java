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
import com.tallence.formeditor.elements.AbstractFormElement;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.tallence.formeditor.parser.TextFieldParser.FORM_DATA_NAME;

/**
 * Validates, that all form elements have a name.
 */
@Component
public class NameNotEmptyValidator extends AbstractFormValidator<AbstractFormElement<?, ?>> {

  public NameNotEmptyValidator() {
    super(AbstractFormElement.class);
  }

  /**
   * Every formField needs a name.
   */
  @Override
  public void validateField(AbstractFormElement<?, ?> formElement, String action, Issues issues) {
    if (StringUtils.isEmpty(formElement.getName())) {
      addErrorIssue(issues, formElement.getStructId(), FORM_DATA_NAME, "formField_missing_name", formElement.getClass().getSimpleName());
    }
  }

}
