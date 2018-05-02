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

import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.annotations.FormElementDefinition;
import com.tallence.formeditor.cae.parser.ComplexValuePropertyConfigurer;
import com.tallence.formeditor.cae.validator.CheckBoxesGroupValidator;

import java.util.Collections;
import java.util.List;

/**
 * Model bean for a configured CheckBoxesGroup.
 */
@FormElementDefinition("CheckBoxes")
public class CheckBoxesGroup extends AbstractFormElement<List, CheckBoxesGroupValidator> {

  @Configured(key = GROUP_ELEMENTS_KEY, configurer = ComplexValuePropertyConfigurer.class)
  private List<ComplexValue> checkBoxes;


  public CheckBoxesGroup() {
    super(List.class);
  }


  @Override
  public String serializeValue() {

    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object value : getValue() != null ? getValue() : Collections.emptyList()) {
      sb.append(value.toString());
      sb.append(", ");
    }
    sb.append("]");

    return sb.toString();
  }

  public List<ComplexValue> getCheckBoxes() {
    return this.checkBoxes;
  }

  public void setCheckBoxes(List<ComplexValue> checkBoxes) {
    this.checkBoxes = checkBoxes;
  }
}
