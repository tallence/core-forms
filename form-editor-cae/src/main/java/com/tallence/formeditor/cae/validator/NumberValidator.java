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

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for elements of type {@link com.tallence.formeditor.cae.elements.NumberField}
 */
public class NumberValidator implements Validator<String>, SizeValidator {

  private boolean mandatory;
  private Integer minSize;
  private Integer maxSize;

  @Override
  public List<String> validate(String value) {

    List<String> errors = new ArrayList<>();
    if (StringUtils.hasText(value)) {
      if (!value.matches("\\d+")) {
        errors.add("com.tallence.forms.numberfield.nan");
        return errors;
      }

      Integer number = Integer.valueOf(value);
      if (this.minSize != null && number < this.minSize) {
        errors.add("com.tallence.forms.numberfield.tooshort");
      }
      if (this.maxSize != null && number > this.maxSize) {
        errors.add("com.tallence.forms.numberfield.toolong");
      }
    } else if (this.mandatory) {
      errors.add("com.tallence.forms.numberfield.empty");
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
