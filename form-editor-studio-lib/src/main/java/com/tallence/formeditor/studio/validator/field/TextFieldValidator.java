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
import com.tallence.formeditor.elements.TextField;
import com.tallence.formeditor.validator.TextValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_DATA_VALIDATOR;
import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_VALIDATOR_REGEXP;

/**
 * Validates, that regular expressions in a text field are parseable and size limits make sense.
 */
@Component
public class TextFieldValidator extends AbstractFormValidator<TextField> {

  private static final String FULLPATH_REGEX = FORM_DATA_VALIDATOR + "." + FORM_VALIDATOR_REGEXP;

  public TextFieldValidator() {
    super(TextField.class);
  }

  @Override
  public void validateField(TextField formElement, String action, Issues issues) {
    var validator = formElement.getValidator();
    if (validator != null) {
      validateFieldValidators(validator, issues, formElement.getStructId(), formElement.getName());
    }
  }

  private void validateFieldValidators(TextValidator validator, Issues issues, String formElementId, String name) {
    validateMaxAndMinSize(validator, issues, formElementId, name);

    // Regex
    try {
      Pattern regex = validator.getRegexp();
    } catch (PatternSyntaxException e) {
      addErrorIssue(issues, formElementId, FULLPATH_REGEX, "formfield_validator_invalid_regexp");
    }
  }

}
