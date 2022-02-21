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

import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.elements.CheckBoxesGroup;
import com.tallence.formeditor.elements.ComplexValue;
import org.springframework.stereotype.Component;

/**
 * Validates the minSize/maxSize properties for CheckBoxGroups
 */
@Component
public class CheckBoxesRestrictionsValidator extends AbstractFormValidator<CheckBoxesGroup> {

  public CheckBoxesRestrictionsValidator() {
    super(CheckBoxesGroup.class);
  }

  @Override
  public void validateField(CheckBoxesGroup formElement, String action, Issues issues) {

    final var validator = formElement.getValidator();
    if (validator == null) {
      return;
    }

    Integer maxSelection = validator.getMaxSize();
    Integer minSelection = validator.getMinSize();

    final var options = formElement.getOptions();

    int allOptions = options.size();
    long selectedOptions = options.stream()
            .filter(ComplexValue::isSelectedByDefault)
            .count();

    if (minSelection != null) {
      if (minSelection < 0) {
        addErrorIssue(issues, formElement.getStructId(), FULLPATH_MIN_SIZE, "checkboxes_options_min_lower_zero", formElement.getName());
      }
      if (allOptions < minSelection) {
        addErrorIssue(issues, formElement.getStructId(), FULLPATH_MIN_SIZE, "checkboxes_options_min_greater_options", formElement.getName());
      }
    }
    if (maxSelection != null) {
      if (maxSelection < 0) {
        addErrorIssue(issues, formElement.getStructId(), FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_zero", formElement.getName());
      }
      if (allOptions < maxSelection) {
        addErrorIssue(issues, formElement.getStructId(), FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_options", formElement.getName());
      }
      if (selectedOptions > maxSelection) {
        addErrorIssue(issues, formElement.getStructId(), FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_preselection", formElement.getName());
      }
    }
    if (minSelection != null && maxSelection != null && minSelection > maxSelection) {
      addErrorIssue(issues, formElement.getStructId(), FULLPATH_MAX_SIZE, "checkboxes_options_max_lower_min", formElement.getName());
    }
  }

}
