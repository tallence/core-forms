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
import com.tallence.formeditor.cae.elements.SelectBox;
import com.tallence.formeditor.cae.validator.SelectBoxValidator;
import org.springframework.stereotype.Component;

/**
 * Parser for elements of type {@link SelectBox}
 */
@Component
public class SelectBoxParser extends AbstractFormElementParser<SelectBox> {

  public static final String parserKey = "SelectBox";
  public static final String OPTIONS = FORM_GROUP_ELEMENTS_PROPERTY_NAME;


  @Override
  public SelectBox instantiateType(Struct elementData) {
    return new SelectBox();
  }


  @Override
  public void parseSpecialFields(SelectBox formElement, Struct elementData) {
    doForStructElement(elementData, FORM_DATA_VALIDATOR, validator -> {
      SelectBoxValidator selectBoxValidator = new SelectBoxValidator(formElement);

      selectBoxValidator.setMandatory(parseBoolean(validator, FORM_VALIDATOR_MANDATORY));

      formElement.setValidator(selectBoxValidator);
    });

    doForStructElement(elementData, OPTIONS, options -> formElement.setOptions(parseComplexValues(options)));
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
