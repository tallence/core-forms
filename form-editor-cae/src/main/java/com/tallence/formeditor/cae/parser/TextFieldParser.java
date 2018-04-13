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
import com.tallence.formeditor.cae.elements.TextField;
import com.tallence.formeditor.cae.validator.TextValidator;
import org.springframework.stereotype.Component;

/**
 * Parser for elements of type {@link TextField}
 */
@Component
public class TextFieldParser extends AbstractFormElementParser<TextField> {

  public static final String parserKey = "TextField";


  @Override
  public TextField instantiateType(Struct elementData) {
    return new TextField();
  }


  @Override
  public void parseSpecialFields(TextField formElement, Struct elementData) {
    doForStructElement(elementData, FORM_DATA_VALIDATOR, validator -> {
      TextValidator textValidator = new TextValidator();

      textValidator.setMandatory(parseBoolean(validator, FORM_VALIDATOR_MANDATORY));
      textValidator.setMinSize(parseInteger(validator, FORM_VALIDATOR_MINSIZE));
      textValidator.setMaxSize(parseInteger(validator, FORM_VALIDATOR_MAXSIZE));
      textValidator.setRegexp(parseString(elementData, FORM_VALIDATOR_REGEXP));

      formElement.setValidator(textValidator);
    });
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
