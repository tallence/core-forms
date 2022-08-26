/*
 * Copyright 2022 Tallence AG
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
import org.apache.commons.lang3.StringUtils;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import java.util.Collections;
import java.util.List;

@ValidationMessage(name = "iban", messageKey = IbanFieldValidator.MESSAGE_KEY_IBANFIELD_PATTERN)
public class IbanFieldValidator implements Validator<String> {

  private static final String MESSAGE_KEY_IBANFIELD_MANDATORY = "com.tallence.forms.ibanField.mandatory";
  protected static final String MESSAGE_KEY_IBANFIELD_PATTERN = "com.tallence.forms.ibanField.invalid";

  @ValidationProperty(messageKey = MESSAGE_KEY_IBANFIELD_MANDATORY)
  private boolean mandatory;

  @Override
  public List<ValidationFieldError> validate(String value) {

    if (StringUtils.isBlank(value) && mandatory) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_IBANFIELD_MANDATORY));
    }

    try {
      IbanUtil.validate(value);
    } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_IBANFIELD_PATTERN));
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
