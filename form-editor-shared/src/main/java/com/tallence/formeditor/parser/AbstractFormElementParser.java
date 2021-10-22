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

package com.tallence.formeditor.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.elements.AdvancedSettings;
import com.tallence.formeditor.elements.ComplexValue;
import com.tallence.formeditor.elements.FormElement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.coremedia.cap.util.CapStructUtil.*;
import static java.util.Optional.ofNullable;

/**
 * Abstract Parser which can parse struct values into {@link FormElement} model beans.
 * The struct values are created in studio form.
 * <p>
 * Implementations have to implement:
 * {@link #instantiateType(Struct)}
 * {@link #getParserKey()} or {@link #getParserKeys()}
 * {@link #parseSpecialFields(FormElement, Struct)}
 */
public abstract class AbstractFormElementParser<T extends FormElement<?>> {

  public static final String FORM_DATA_VALIDATOR = "validator";
  public static final String FORM_DATA_VALIDATOR_MESSAGES = "errorMessages";
  public static final String FORM_DATA_ADVANCED_SETTINGS = "advancedSettings";
  public static final String FORM_DATA_CUSTOM_ID = "customId";
  public static final String FORM_DATA_CUSTOM_WIDTH = "columnWidth";
  public static final String FORM_DATA_BREAK_AFTER_ELEMENT = "breakAfterElement";
  public static final String FORM_DATA_VISIBILIY = "visibility";
  public static final String FORM_DATA_VISIBILITY_ACTIVATED = "activated";
  public static final String FORM_DATA_VISIBILITY_ELEMENT_ID = "elementId";
  public static final String FORM_DATA_VISIBILITY_ELEMENT_VALUE = "value";
  public static final String FORM_DATA_VISIBILITY_ELEMENT_VALUES = "values";
  public static final String FORM_DATA_NAME = "name";
  public static final String FORM_DATA_HINT = "hint";
  public static final String FORM_DATA_PLACEHOLDER = "placeholder";
  public static final String FORM_VALIDATOR_MINSIZE = "minSize";
  public static final String FORM_VALIDATOR_MAXSIZE = "maxSize";
  public static final String FORM_VALIDATOR_REGEXP = "regexpValidator";
  public static final String FORM_VALIDATOR_MINDATE_VALUE = "minDate";
  public static final String FORM_VALIDATOR_MINDATE_TODAY = "minDateToday";
  public static final String FORM_VALIDATOR_MAXDATE_VALUE = "maxDate";
  public static final String FORM_VALIDATOR_MAXDATE_TODAY = "maxDateToday";
  public static final String FORM_GROUP_ELEMENTS_PROPERTY_NAME = "groupElements";
  public static final String FORM_VALIDATOR_MANDATORY = "mandatory";

  /**
   * Creates an instance of the concrete parser class.
   *
   * @param elementData the element data from which to create the instance
   */
  public abstract T instantiateType(Struct elementData);

  /**
   * @return the string keys of the concrete parser, e.g. "CheckBoxes". One parser can be responsible for multiple
   * FormFieldTypes, e.g. NumberField and RatingField.
   */
  public List<String> getParserKeys() {
    String key = getParserKey();
    if (key == null) {
      throw new IllegalStateException("Either getParserKeys() or getParserKey() must be overwritten in subClass and must not return null!");
    }
    return Collections.singletonList(key);
  }

  /**
   * @return the string key of the concrete parser, e.g. "CheckBoxes". If {@link AbstractFormElementParser#getParserKeys()}
   * is implemented in subClass, this method is not required.
   */
  protected String getParserKey() {
    return null;
  }

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
   * And parse the advancedSettings which might be configured in most field types.
   *
   * @param formElement the current form element which is not filled completely yet.
   * @param elementData the struct representation of the current form field.
   * @param id          the form elements Id, a unique identifier of the element in scope of the current form.
   */
  public void parseBaseFields(final T formElement, Struct elementData, String id) {

    formElement.setName(getString(elementData, FORM_DATA_NAME));
    formElement.setHint(getString(elementData, FORM_DATA_HINT));
    formElement.setPlaceholder(getString(elementData, FORM_DATA_PLACEHOLDER));
    formElement.setId(id);

    ofNullable(getSubstruct(elementData, FORM_DATA_ADVANCED_SETTINGS)).ifPresent(advancedSettings -> {

      AdvancedSettings settings = new AdvancedSettings();

      ofNullable(getString(advancedSettings, FORM_DATA_CUSTOM_ID)).ifPresent(settings::setCustomId);
      ofNullable(getInteger(advancedSettings, FORM_DATA_CUSTOM_WIDTH)).ifPresent(settings::setColumnWidth);
      settings.setBreakAfterElement(getBoolean(advancedSettings, FORM_DATA_BREAK_AFTER_ELEMENT));

      ofNullable(getSubstruct(advancedSettings, FORM_DATA_VISIBILIY)).ifPresent(visibility -> {
        boolean activated = getBoolean(visibility, FORM_DATA_VISIBILITY_ACTIVATED);
        if (activated) {
          settings.setVisibilityDependent(activated);
          ofNullable(getString(visibility, FORM_DATA_VISIBILITY_ELEMENT_ID)).ifPresent(settings::setDependentElementId);
          ofNullable(getString(visibility, FORM_DATA_VISIBILITY_ELEMENT_VALUE)).ifPresent(settings::setDependentElementValues);
        }
      });
      formElement.setAdvancedSettings(settings);
    });
  }

  protected static List<ComplexValue> parseComplexValues(Struct values) {
    return values.getProperties().entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> new ComplexValue(e.getKey(), (Struct) e.getValue()))
            .collect(Collectors.toList());
  }
}
