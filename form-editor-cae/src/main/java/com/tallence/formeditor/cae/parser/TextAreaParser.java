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
import com.tallence.formeditor.cae.elements.TextArea;
import com.tallence.formeditor.cae.validator.TextValidator;
import org.springframework.stereotype.Component;

import static com.coremedia.cap.util.CapStructUtil.*;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link TextArea}
 */
@Component
public class TextAreaParser extends AbstractFormElementParser<TextArea> {

  public static final String parserKey = "TextArea";
  private static final String FORM_TEXTAREA_COLUMNS = "columns";
  private static final String FORM_TEXTAREA_ROWS = "rows";


  @Override
  public TextArea instantiateType(Struct elementData) {
    return new TextArea();
  }


  @Override
  public void parseSpecialFields(TextArea formElement, Struct elementData) {

    formElement.setColumns(getInteger(elementData, FORM_TEXTAREA_COLUMNS));
    formElement.setRows(getInteger(elementData, FORM_TEXTAREA_ROWS));

    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      TextValidator textValidator = new TextValidator();

      textValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));
      textValidator.setMinSize(getInteger(validator, FORM_VALIDATOR_MINSIZE));
      textValidator.setMaxSize(getInteger(validator, FORM_VALIDATOR_MAXSIZE));
      formElement.setValidator(textValidator);
    });
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
