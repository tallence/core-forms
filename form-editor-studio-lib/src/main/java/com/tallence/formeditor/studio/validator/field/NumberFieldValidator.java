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
import com.tallence.formeditor.elements.NumberField;
import org.springframework.stereotype.Component;

/**
 * Validates, that sizeLimits in a numberField make sense.
 */
@Component
public class NumberFieldValidator extends AbstractFormValidator<NumberField> {

  public NumberFieldValidator() {
    super(NumberField.class);
  }

  @Override
  void validateField(NumberField formElement, String action, Issues issues) {
    var validator = formElement.getValidator();
    if (validator != null) {
      validateMaxAndMinSize(validator, issues, formElement.getId(), formElement.getName());
    }
  }

}
