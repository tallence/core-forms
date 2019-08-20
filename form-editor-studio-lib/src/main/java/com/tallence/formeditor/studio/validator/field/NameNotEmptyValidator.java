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
import com.tallence.formeditor.cae.parser.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.tallence.formeditor.cae.FormElementFactory.FORM_DATA_KEY_TYPE;
import static com.tallence.formeditor.cae.parser.ConsentFormCheckBoxParser.CONSENT_FORM_CHECK_BOX_TYPE;
import static com.tallence.formeditor.cae.parser.TextFieldParser.*;

/**
 * Validates, that all form elements have a name.
 */
@Component
public class NameNotEmptyValidator extends AbstractFormValidator implements FieldValidator {
  @Override
  public List<String> resonsibleFor() {
    return Arrays.asList(KEY_TEXT_FIELD, KEY_ZIP_FIELD, KEY_PHONE_FIELD, KEY_FAX_FIELD, KEY_STREET_NUMBER_FIELD,
        NumberFieldParser.parserKey, RadioButtonParser.parserKey, CheckBoxesParser.parserKey, SelectBoxParser.parserKey,
        TextAreaParser.parserKey, UsersMailParser.parserKey, FileUploadParser.parserKey, CONSENT_FORM_CHECK_BOX_TYPE);
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    if (!StringUtils.hasText(StructUtil.getString(fieldData, FORM_DATA_NAME))) {
      addErrorIssue(issues, id, FORM_DATA_NAME, "formField_missing_name", fieldData.get(FORM_DATA_KEY_TYPE));
    }
  }
}
