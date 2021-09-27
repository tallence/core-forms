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

package com.tallence.formeditor.elements;

import com.tallence.formeditor.validator.RadioButtonGroupValidator;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * Model bean for a configured RadioButtonGroup.
 */
public class RadioButtonGroup extends AbstractFormElement<String, RadioButtonGroupValidator>  implements FieldWithOptions<String> {

  public RadioButtonGroup() {
    super(String.class);
  }

  private List<ComplexValue> radioButtons;

  /**
   * @deprecated use {@link #getOptions()}
   */
  public List<ComplexValue> getRadioButtons() {
    return getOptions();
  }

  @Override
  @NonNull
  public List<ComplexValue> getOptions() {
    return ofNullable(this.radioButtons).orElse(Collections.emptyList());
  }

  public void setRadioButtons(List<ComplexValue> radioButtons) {
    this.radioButtons = radioButtons;
  }

  @Override
  public String serializeValue() {
    return radioButtons.stream().filter(cv -> cv.getValue().equals(getValue())).findFirst().map(ComplexValue::getDisplayName).orElse("");
  }
}
