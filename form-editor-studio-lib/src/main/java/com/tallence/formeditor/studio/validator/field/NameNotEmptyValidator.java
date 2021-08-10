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
import com.coremedia.cap.util.CapStructUtil;
import com.coremedia.rest.validation.Issues;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.coremedia.cap.util.CapStructUtil.getString;
import static com.tallence.formeditor.cae.FormElementFactory.FORM_DATA_KEY_TYPE;
import static com.tallence.formeditor.cae.parser.TextFieldParser.FORM_DATA_NAME;

/**
 * Validates, that all form elements have a name.
 */
@Component
public class NameNotEmptyValidator extends AbstractFormValidator implements FieldValidator {

  /**
   * Every formField needs a name.
   */
  @Override
  public boolean responsibleFor(String fieldType, Struct formElementData) {
    return true;
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    if (!StringUtils.hasText(getString(fieldData, FORM_DATA_NAME))) {
      addErrorIssue(issues, id, FORM_DATA_NAME, "formField_missing_name", fieldData.get(FORM_DATA_KEY_TYPE));
    }
  }
}
