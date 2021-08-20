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

import com.tallence.formeditor.validator.SelectBoxValidator;

import java.util.List;

/**
 * Model bean for a configured SelectBox.
 */
public class SelectBox extends AbstractFormElement<String, SelectBoxValidator> implements FieldWithOptions {

  public SelectBox() {
    super(String.class);
  }

  private List<ComplexValue> options;

  @Override
  public List<ComplexValue> getOptions() {
    return this.options;
  }

  public void setOptions(List<ComplexValue> options) {
    this.options = options;
  }

  @Override
  public String serializeValue() {
    return options.stream().filter(cv -> cv.getValue().equals(getValue())).findFirst().map(ComplexValue::getDisplayName).orElse("");
  }
}
