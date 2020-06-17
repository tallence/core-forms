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

package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.util.StructUtil;
import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.cae.parser.CheckBoxesParser;
import org.springframework.stereotype.Component;

import static com.tallence.formeditor.cae.elements.ComplexValue.CHECKED_BY_DEFAULT;
import static com.tallence.formeditor.cae.parser.AbstractFormElementParser.*;

/**
 * Validates the minSize/maxSize properties for CheckBoxGroups
 */
@Component
public class CheckBoxesRestrictionsValidator extends AbstractFormValidator implements FieldValidator {

  @Override
  public boolean responsibleFor(String fieldType, Struct formElementData) {
    return CheckBoxesParser.parserKey.equals(fieldType);
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {

    Struct validator = StructUtil.getSubstruct(fieldData, FORM_DATA_VALIDATOR);
    if (validator == null) {
      return;
    }

    Integer maxSelection = StructUtil.getInteger(validator, FORM_VALIDATOR_MAXSIZE);
    Integer minSelection = StructUtil.getInteger(validator, FORM_VALIDATOR_MINSIZE);

    Struct options = StructUtil.getSubstruct(fieldData, FORM_GROUP_ELEMENTS_PROPERTY_NAME);

    int allOptions = options == null ? 0 : options.getProperties().size();
    long selectedOptions = options == null ? 0 : options.getProperties().values()
            .stream()
            .filter(Struct.class::isInstance)
            .map(Struct.class::cast)
            .filter(s -> StructUtil.getBoolean(s, CHECKED_BY_DEFAULT))
            .count();

    if (minSelection != null) {
      if (minSelection < 0) {
        addErrorIssue(issues, id, FULLPATH_MIN_SIZE, "checkboxes_options_min_lower_zero", fieldData.get(FORM_DATA_NAME));
      }
      if (allOptions < minSelection) {
        addErrorIssue(issues, id, FULLPATH_MIN_SIZE, "checkboxes_options_min_greater_options", fieldData.get(FORM_DATA_NAME));
      }
    }
    if (maxSelection != null) {
      if (maxSelection < 0) {
        addErrorIssue(issues, id, FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_zero", fieldData.get(FORM_DATA_NAME));
      }
      if (allOptions < maxSelection) {
        addErrorIssue(issues, id, FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_options", fieldData.get(FORM_DATA_NAME));
      }
      if (selectedOptions > maxSelection) {
        addErrorIssue(issues, id, FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_preselection", fieldData.get(FORM_DATA_NAME));
      }
    }
    if (minSelection != null && maxSelection != null && minSelection > maxSelection) {
      addErrorIssue(issues, id, FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_min", fieldData.get(FORM_DATA_NAME));
    }
  }

}
