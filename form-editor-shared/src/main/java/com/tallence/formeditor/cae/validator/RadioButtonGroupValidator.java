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

package com.tallence.formeditor.cae.validator;

import com.tallence.formeditor.cae.elements.ComplexValue;
import com.tallence.formeditor.cae.elements.RadioButtonGroup;
import com.tallence.formeditor.cae.validator.annotation.ValidationProperty;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Validator for elements of type {@link RadioButtonGroup}
 */
public class RadioButtonGroupValidator implements Validator<String> {

  private static final String MESSAGE_KEY_RADIOBUTTONS_REQUIRED = "com.tallence.forms.radiobuttons.empty";

  private final RadioButtonGroup radioButtonGroup;

  @ValidationProperty(messageKey = MESSAGE_KEY_RADIOBUTTONS_REQUIRED)
  private boolean mandatory;

  public RadioButtonGroupValidator(RadioButtonGroup radioButtonGroup) {
    this.radioButtonGroup = radioButtonGroup;
  }

  @Override
  public List<ValidationFieldError> validate(String value) {

    if (StringUtils.hasText(value)) {

      List<String> values = new ArrayList<>();
      for (ComplexValue complexValue : this.radioButtonGroup.getOptions()) {
        values.add(complexValue.getValue());
      }

      if (!values.contains(value)) {
        throw new InvalidGroupElementException("A RadioButton was chosen, which does not exist in Form Element!");
      }
    } else if (this.mandatory) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_RADIOBUTTONS_REQUIRED));
    }

    return Collections.emptyList();
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }
}
