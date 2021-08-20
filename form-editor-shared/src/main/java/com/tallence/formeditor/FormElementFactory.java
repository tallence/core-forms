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

package com.tallence.formeditor;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.parser.AbstractFormElementParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AutoWires all parsers from type {@link AbstractFormElementParser} and creates form elements.
 *
 */
@Component
public class FormElementFactory {


  public static final String FORM_DATA_KEY_TYPE = "type";

  private final Map<String, AbstractFormElementParser<? extends FormElement<?>>> typeToParser = new HashMap<>();

  public FormElementFactory(List<AbstractFormElementParser<? extends FormElement<?>>> parsers) {
    parsers.forEach(p -> p.getParserKeys().forEach(k -> typeToParser.put(k, p)));
  }

  public <T extends FormElement<?>> T createFormElement(Struct elementData, String id) {
    return parseType(elementData, id);
  }


  private <T extends FormElement<?>> T parseType(Struct elementData, String id) {
    String type = elementData.getString(FORM_DATA_KEY_TYPE);

    @SuppressWarnings("unchecked")
    AbstractFormElementParser<T> parser = (AbstractFormElementParser<T>) this.typeToParser.get(type);
    if (parser == null) {
      throw new IllegalStateException("Did not find a Parser for type: " + type);
    }

    T formElement = parser.instantiateType(elementData);
    parser.parseBaseFields(formElement, elementData, id);
    parser.parseSpecialFields(formElement, elementData);

    return formElement;
  }

}
