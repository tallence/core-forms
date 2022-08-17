/*
 * Copyright 2021 Tallence AG
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
import com.tallence.formeditor.elements.HiddenField;
import org.springframework.stereotype.Component;

import static com.coremedia.cap.util.CapStructUtil.getString;

/**
 * Parser for elements of type {@link HiddenField}
 */
@Component
public class HiddenFieldParser extends AbstractFormElementParser<HiddenField> {

  public static final String HIDDEN_FIELD_VALUE = "value";

  public static final String parserKey = "HiddenField";

  @Override
  public HiddenField instantiateType(Struct elementData) {
    return new HiddenField();
  }

  @Override
  public void parseSpecialFields(HiddenField formElement, Struct elementData) {
    formElement.setValue(getString(elementData, HIDDEN_FIELD_VALUE));
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
