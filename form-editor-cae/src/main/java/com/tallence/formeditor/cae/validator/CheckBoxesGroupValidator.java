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

import com.tallence.formeditor.cae.elements.CheckBoxesGroup;
import com.tallence.formeditor.cae.elements.ComplexValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for elements of type {@link CheckBoxesGroup}
 */
public class CheckBoxesGroupValidator implements Validator<List> {

  private final CheckBoxesGroup checkBoxesGroup;

  private boolean mandatory;

  public CheckBoxesGroupValidator(CheckBoxesGroup checkBoxesGroup) {
    this.checkBoxesGroup = checkBoxesGroup;
  }

  @Override
  public List<String> validate(List value) {

    List<String> errors = new ArrayList<>();

    if (value != null && !value.isEmpty()) {

      List<String> values = new ArrayList<>();
      for (ComplexValue complexValue : this.checkBoxesGroup.getCheckBoxes()) {
        values.add(complexValue.getValue());
      }

      if (!values.containsAll(value)) {
        throw new InvalidGroupElementException("A CheckBox was chosen, which does not exist in Form Element!");
      }
    } else if (this.mandatory) {
      errors.add("com.tallence.forms.checkboxes.empty");
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
}
