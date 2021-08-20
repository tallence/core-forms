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
import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.parser.TextFieldParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.coremedia.cap.util.CapStructUtil.getString;
import static com.coremedia.cap.util.CapStructUtil.getSubstruct;
import static com.tallence.formeditor.parser.AbstractFormElementParser.*;

/**
 * Validates, that regular expressions in a text field are parseable and size limits make sense.
 */
@Component
public class TextFieldValidator extends AbstractFormValidator implements FieldValidator {

  private static final String FULLPATH_REGEX = FORM_DATA_VALIDATOR + "." + FORM_VALIDATOR_REGEXP;

  @Override
  public boolean responsibleFor(String fieldType, Struct formElementData) {
    return TextFieldParser.KEY_TEXT_FIELD.equals(fieldType);
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    Struct validator = getSubstruct(fieldData, FORM_DATA_VALIDATOR);
    if (validator != null) {
      validateFieldValidators(validator, issues, id, (String) fieldData.get(FORM_DATA_NAME));
    }
  }

  private void validateFieldValidators(Struct validator, Issues issues, String formElementId, String name) {
    validateMaxAndMinSize(validator, issues, formElementId, name);

    // Regex
    String regex = getString(validator, FORM_VALIDATOR_REGEXP);
    if (StringUtils.hasLength(regex)) {
      validateRegex(regex, issues, formElementId);
    }
  }

  private void validateRegex(String regex, Issues issues, String formElementId) {
    try {
      Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      addErrorIssue(issues, formElementId, FULLPATH_REGEX, "formfield_validator_invalid_regexp");
    }
  }

}
