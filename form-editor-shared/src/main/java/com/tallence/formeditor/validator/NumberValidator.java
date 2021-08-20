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

import com.tallence.formeditor.validator.annotation.ValidationMessage;
import com.tallence.formeditor.validator.annotation.ValidationProperty;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tallence.formeditor.validator.NumberValidator.MESSAGE_KEY_NUMBERFIELD_NAN;

/**
 * Validator for elements of type {@link com.tallence.formeditor.elements.NumberField}
 */
@ValidationMessage(name = "number", messageKey = MESSAGE_KEY_NUMBERFIELD_NAN)
public class NumberValidator implements Validator<String>, SizeValidator {

  static final String MESSAGE_KEY_NUMBERFIELD_MIN = "com.tallence.forms.numberfield.min";
  static final String MESSAGE_KEY_NUMBERFIELD_MAX = "com.tallence.forms.numberfield.max";
  protected static final String MESSAGE_KEY_NUMBERFIELD_NAN = "com.tallence.forms.numberfield.nan";
  private static final String MESSAGE_KEY_NUMBERFIELD_REQUIRED = "com.tallence.forms.numberfield.empty";

  @ValidationProperty(messageKey = MESSAGE_KEY_NUMBERFIELD_REQUIRED)
  private boolean mandatory;

  @ValidationProperty(messageKey = MESSAGE_KEY_NUMBERFIELD_MIN)
  private Integer minSize;

  @ValidationProperty(messageKey = MESSAGE_KEY_NUMBERFIELD_MAX)
  private Integer maxSize;

  @Override
  public List<ValidationFieldError> validate(String value) {

    List<ValidationFieldError> errors = new ArrayList<>();
    if (StringUtils.hasText(value)) {
      if (!value.matches("\\d+")) {
        return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_NUMBERFIELD_NAN));
      }

      BigInteger number = new BigInteger(value);
      if (this.minSize != null && number.compareTo(BigInteger.valueOf(minSize)) < 0) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_NUMBERFIELD_MIN, minSize));
      }
      if (this.maxSize != null && number.compareTo(BigInteger.valueOf(maxSize)) > 0) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_NUMBERFIELD_MAX, this.maxSize));
      }
    } else if (this.mandatory) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_NUMBERFIELD_REQUIRED));
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
