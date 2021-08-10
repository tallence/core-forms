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

package com.tallence.formeditor.cae.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.elements.RadioButtonGroup;
import com.tallence.formeditor.cae.validator.RadioButtonGroupValidator;
import org.springframework.stereotype.Component;

import static com.coremedia.cap.util.CapStructUtil.*;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link RadioButtonGroup}
 */
@Component
public class RadioButtonParser extends AbstractFormElementParser<RadioButtonGroup> {

  public static final String parserKey = "RadioButtons";
  private static final String RADIO_BUTTONS = FORM_GROUP_ELEMENTS_PROPERTY_NAME;


  @Override
  public RadioButtonGroup instantiateType(Struct elementData) {
    return new RadioButtonGroup();
  }


  @Override
  public void parseSpecialFields(RadioButtonGroup formElement, Struct elementData) {
    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      RadioButtonGroupValidator radioValidator = new RadioButtonGroupValidator(formElement);

      radioValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));

      formElement.setValidator(radioValidator);
    });

    ofNullable(getSubstruct(elementData, RADIO_BUTTONS)).ifPresent(options -> formElement.setRadioButtons(parseComplexValues(options)));
  }

  @Override
  public String getParserKey() {
    return this.parserKey;
  }
}
