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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Validates, that regular expressions in a text field are parseable and size limits make sense.
 */
@Component
public class TextFieldValidator extends AbstractFormValidator implements FieldValidator {

  private static final String VALIDATOR = "validator";
  private static final String REGEX = "regexpValidator";

  @Override
  public List<String> resonsibleFor() {
    return Collections.singletonList("TextField");
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    Struct validator = StructUtil.getSubstruct(fieldData, VALIDATOR);
    if (validator != null) {
      validateFieldValidators(validator, issues, id, (String) fieldData.get("name"));
    }
  }

  private void validateFieldValidators(Struct validator, Issues issues, String formElementId, String name) {
    // Size constraints
    Integer minSize = StructUtil.getInteger(validator, "minSize");
    Integer maxSize = StructUtil.getInteger(validator, "maxSize");
    validateMinSize(minSize, issues, formElementId, name);
    validateMaxSize(maxSize, minSize, issues, formElementId, name);

    // Regex
    String regex = StructUtil.getString(validator, REGEX);
    if (StringUtils.hasLength(regex)) {
      validateRegex(regex, issues, formElementId);
    }
  }

  private void validateMinSize(Integer minSize, Issues issues, String formElementId, String name) {
    if (minSize != null && minSize < 0) {
      addErrorIssue(issues, formElementId, "validator.minSize", "formfield_validator_invalid_minsize", name, minSize);
    }
  }

  private void validateMaxSize(Integer maxSize, Integer minSize, Issues issues, String formElementId, String name) {
    if (maxSize != null && maxSize < 0) {
      addErrorIssue(issues, formElementId, "validator.maxSize", "formfield_validator_invalid_maxsize", name, maxSize);
    } else if (maxSize != null && minSize != null && maxSize < minSize) {
      addErrorIssue(issues, formElementId, "validator.maxSize", "formfield_validator_maxsize_smaller_minsize", name, maxSize);
    }
  }

  private void validateRegex(String regex, Issues issues, String formElementId) {
    try {
      Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      addErrorIssue(issues, formElementId, "validator.maxSize", "formfield_validator_invalid_regexp");
    }
  }

}
