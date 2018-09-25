/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.contentbeans.FormEditor;
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
public class TextFieldValidator implements FieldValidator {

  private static final String VALIDATOR = "validator";
  private static final String REGEX = "regexpValidator";

  @Override
  public List<String> resonsibleFor() {
    return Collections.singletonList("TextField");
  }

  @Override
  public void validateField(Struct fieldData, String action, Issues issues) {
    Struct validator = (Struct) fieldData.get(VALIDATOR);
    if (validator != null) {
      validateFieldValidators(validator, issues);
    }
  }

  private void validateFieldValidators(Struct validator, Issues issues) {
    // Size constraints
    Integer minSize = (Integer) validator.get("minSize");
    Integer maxSize = (Integer) validator.get("maxSize");
    validateMinSize(minSize, issues);
    validateMaxSize(maxSize, minSize, issues);

    // Regex
    String regex = (String) validator.get(REGEX);
    if (StringUtils.hasLength(regex)) {
      validateRegex(regex, issues);
    }
  }

  private void validateMinSize(Integer minSize, Issues issues) {
    if (minSize != null && minSize < 0) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ELEMENTS, "invalid_minsize");
    }
  }

  private void validateMaxSize(Integer maxSize, Integer minSize, Issues issues) {
    if (maxSize != null && maxSize < 0) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ELEMENTS, "invalid_maxsize");
    } else if (maxSize != null && minSize != null && maxSize < minSize) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ELEMENTS, "maxsize_smaller_minsize");
    }
  }

  private void validateRegex(String regex, Issues issues) {
    try {
      Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ELEMENTS, "form_action_invalid_regexp");
    }
  }

}