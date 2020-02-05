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
import com.tallence.formeditor.cae.elements.CheckBoxesGroup;
import com.tallence.formeditor.cae.validator.CheckBoxesGroupValidator;
import org.springframework.stereotype.Component;

import static com.coremedia.blueprint.base.util.StructUtil.getBoolean;
import static com.coremedia.blueprint.base.util.StructUtil.getSubstruct;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link CheckBoxesGroup}
 */
@Component
public class CheckBoxesParser extends AbstractFormElementParser<CheckBoxesGroup> {

  public static final String parserKey = "CheckBoxes";
  private static final String CHECK_BOXES = FORM_GROUP_ELEMENTS_PROPERTY_NAME;


  @Override
  public CheckBoxesGroup instantiateType(Struct elementData) {
    return new CheckBoxesGroup();
  }


  @Override
  public void parseSpecialFields(CheckBoxesGroup formElement, Struct elementData) {
    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      CheckBoxesGroupValidator checkBoxesValidator = new CheckBoxesGroupValidator(formElement);

      checkBoxesValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));

      formElement.setValidator(checkBoxesValidator);
    });

    ofNullable(getSubstruct(elementData, CHECK_BOXES)).ifPresent(options -> formElement.setCheckBoxes(parseComplexValues(options)));
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
