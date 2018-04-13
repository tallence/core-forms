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
import com.tallence.formeditor.cae.elements.ComplexValue;
import com.tallence.formeditor.cae.elements.FormElement;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Abstract Parser which can parse struct values into {@link FormElement} model beans.
 * The struct values are created in studio form.
 * <p>
 * Implementations have to implement:
 * {@link #instantiateType(Struct)}
 * {@link #getParserKey()}
 * {@link #parseSpecialFields(FormElement, Struct)}
 */
public abstract class AbstractFormElementParser<T extends FormElement> {

  static final String FORM_DATA_VALIDATOR = "validator";
  private static final String FORM_DATA_NAME = "name";
  private static final String FORM_DATA_HINT = "hint";
  static final String FORM_VALIDATOR_MANDATORY = "mandatory";
  static final String FORM_VALIDATOR_MINSIZE = "minSize";
  static final String FORM_VALIDATOR_MAXSIZE = "maxSize";
  static final String FORM_VALIDATOR_REGEXP = "regexpValidator";
  static final String FORM_GROUP_ELEMENTS_PROPERTY_NAME = "groupElements";

  /**
   * Creates an instance of the concrete parser class.
   *
   * @param elementData the element data from which to create the instance
   */
  public abstract T instantiateType(Struct elementData);

  /**
   * @return the string key of the concrete parser, e.g. "CheckBoxes".
   */
  public abstract String getParserKey();

  /**
   * The parsing process uses two steps:
   * <ul>
   * <li>{@link #parseBaseFields(FormElement, Struct, String)} which parses the fields, used by all form elements.</li>
   * <li>this method which handles fields, which are relevant for only some fields.</li>
   * </ul>
   *
   * @param formElement the current form element which is not filled completely yet.
   * @param elementData the struct representation of the current form field.
   */
  public abstract void parseSpecialFields(final T formElement, Struct elementData);

  /**
   * Parses the form element's name, id and hint, these two fields are used by every form element.
   *
   * @param formElement the current form element which is not filled completely yet.
   * @param elementData the struct representation of the current form field.
   * @param id          the form elements Id, a unique identifier of the element in scope of the current form.
   */
  public void parseBaseFields(final T formElement, Struct elementData, String id) {

    formElement.setName(parseString(elementData, FORM_DATA_NAME));
    formElement.setHint(parseString(elementData, FORM_DATA_HINT));
    formElement.setId(id);
  }

  void doForStructElement(Struct parent, String key, Consumer<Struct> callback) {
    if (parent.get(key) != null) {
      Struct struct = parent.getStruct(key);
      callback.accept(struct);
    }
  }

  static List<ComplexValue> parseComplexValues(Struct values) {
    return values.getProperties().entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> new ComplexValue(e.getKey(), (Struct) e.getValue()))
            .collect(Collectors.toList());
  }

  static String parseString(Struct elementData, String key) {
    return elementData.get(key) != null ? elementData.getString(key) : null;
  }

  static boolean parseBoolean(Struct elementData, String key) {
    return elementData.get(key) != null && elementData.getBoolean(key);
  }

  static Integer parseInteger(Struct elementData, String key) {
    return elementData.get(key) != null ? elementData.getInt(key) : null;
  }
}
