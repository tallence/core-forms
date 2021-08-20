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

import com.tallence.formeditor.elements.CheckBoxesGroup;
import com.tallence.formeditor.elements.ComplexValue;
import com.tallence.formeditor.validator.annotation.ValidationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for elements of type {@link CheckBoxesGroup}
 */
public class CheckBoxesGroupValidator implements Validator<List>, SizeValidator {

  private static final String MESSAGE_KEY_CHECKBOX_REQUIRED = "com.tallence.forms.checkboxes.empty";
  private static final String MESSAGE_KEY_CHECKBOX_MIN = "com.tallence.forms.checkboxes.min";
  private static final String MESSAGE_KEY_CHECKBOX_MAX = "com.tallence.forms.checkboxes.max";

  private final CheckBoxesGroup checkBoxesGroup;

  @ValidationProperty(messageKey = MESSAGE_KEY_CHECKBOX_REQUIRED)
  private boolean mandatory;

  @ValidationProperty(messageKey = MESSAGE_KEY_CHECKBOX_MIN)
  private Integer minSize;

  @ValidationProperty(messageKey = MESSAGE_KEY_CHECKBOX_MAX)
  private Integer maxSize;

  public CheckBoxesGroupValidator(CheckBoxesGroup checkBoxesGroup) {
    this.checkBoxesGroup = checkBoxesGroup;
  }

  @Override
  public List<ValidationFieldError> validate(List value) {

    List<ValidationFieldError> errors = new ArrayList<>();

    if (value != null && !value.isEmpty()) {

      List<String> values = new ArrayList<>();
      for (ComplexValue complexValue : this.checkBoxesGroup.getOptions()) {
        values.add(complexValue.getValue());
      }

      if (!values.containsAll(value)) {
        throw new InvalidGroupElementException("A CheckBox was chosen, which does not exist in Form Element!");
      }

      if (this.minSize != null && this.minSize != 0  && values.size() < this.minSize) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_CHECKBOX_MIN, this.minSize));
      }

      if (this.maxSize != null && this.maxSize != 0  && values.size() < this.maxSize) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_CHECKBOX_MAX, this.maxSize));
      }

    } else if (this.mandatory) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_CHECKBOX_REQUIRED));
    }

    return errors;
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public Integer getMinSize() {
    return this.minSize;
  }

  public void setMinSize(Integer minSize) {
    this.minSize = minSize;
  }

  @Override
  public Integer getMaxSize() {
    return this.maxSize;
  }

  public void setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
  }
}
