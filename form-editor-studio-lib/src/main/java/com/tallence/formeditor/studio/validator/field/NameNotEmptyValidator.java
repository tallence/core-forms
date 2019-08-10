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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Validates, that all form elements have a name.
 */
@Component
public class NameNotEmptyValidator extends AbstractFormValidator implements FieldValidator {
  @Override
  public List<String> resonsibleFor() {
    return Arrays.asList("TextField", "NumberField", "RadioButtons", "CheckBoxes", "SelectBox", "TextArea", "UsersMail",
            "FileUpload", "ConsentFormCheckBox");
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    if (!StringUtils.hasText(StructUtil.getString(fieldData, "name"))) {
      addErrorIssue(issues, id, "name", "formField_missing_name", fieldData.get("type"));
    }
  }
}
