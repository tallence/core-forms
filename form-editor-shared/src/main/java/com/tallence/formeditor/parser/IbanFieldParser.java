/*
 * Copyright 2020 Tallence AG
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

package com.tallence.formeditor.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.elements.IbanField;
import com.tallence.formeditor.validator.IbanFieldValidator;
import org.springframework.stereotype.Component;

import static com.coremedia.cap.util.CapStructUtil.getBoolean;
import static com.coremedia.cap.util.CapStructUtil.getSubstruct;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link IbanField}
 */
@Component
public class IbanFieldParser extends AbstractFormElementParser<IbanField> {

  public static final String parserKey = "IbanField";

  @Override
  public IbanField instantiateType(Struct elementData) {
    return new IbanField();
  }

  @Override
  public void parseSpecialFields(IbanField formElement, Struct elementData) {
    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      IbanFieldValidator ibanFieldValidator = new IbanFieldValidator();

      ibanFieldValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));
      formElement.setValidator(ibanFieldValidator);
    });
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
