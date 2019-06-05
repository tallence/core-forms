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

import com.coremedia.blueprint.base.util.StructUtil;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Validates, that CheckBoxes, DropDowns and RadioButtons have at least one groupElement.
 */
@Component
public class OptionsNotEmptyValidator implements FieldValidator {
  @Override
  public List<String> resonsibleFor() {
    return Arrays.asList("RadioButtons", "CheckBoxes", "SelectBox");
  }

  @Override
  public void validateField(Struct fieldData, String action, Issues issues) {

    boolean noElements = Optional.ofNullable(StructUtil.getSubstruct(fieldData, "groupElements"))
        .map(s -> s.getProperties().isEmpty())
        .orElse(true);
    if (noElements) {

      String messageKey = fieldData.getString("type").toLowerCase() + "_missing_options";

      issues.addIssue(Severity.ERROR, FormEditor.FORM_ELEMENTS, messageKey, fieldData.getString("name"));
    }

  }

}
