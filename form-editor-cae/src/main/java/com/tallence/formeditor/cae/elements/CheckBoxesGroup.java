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

package com.tallence.formeditor.cae.elements;

import com.tallence.formeditor.cae.validator.CheckBoxesGroupValidator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model bean for a configured CheckBoxesGroup.
 */
public class CheckBoxesGroup extends AbstractFormElement<List, CheckBoxesGroupValidator> implements FieldWithOptions {

  public CheckBoxesGroup() {
    super(List.class);
  }

  private List<ComplexValue> checkBoxes;

  @Override
  public String serializeValue() {
    return "[" +
            getSelectedOptions().stream().map(ComplexValue::getDisplayName).collect(Collectors.joining(", ")) +
            "]";
  }

  /**
   * @deprecated use {@link #getOptions()}
   */
  @Deprecated
  public List<ComplexValue> getCheckBoxes() {
    return this.checkBoxes;
  }

  @Override
  public List<ComplexValue> getOptions() {
    return this.checkBoxes;
  }

  public List<ComplexValue> getSelectedOptions() {
    List values = getValue() != null ? getValue() : Collections.emptyList();
    return checkBoxes.stream().filter(cv -> values.contains(cv.getValue())).collect(Collectors.toList());
  }

  public void setCheckBoxes(List<ComplexValue> checkBoxes) {
    this.checkBoxes = checkBoxes;
  }

  private void getValues() {

  }

}
