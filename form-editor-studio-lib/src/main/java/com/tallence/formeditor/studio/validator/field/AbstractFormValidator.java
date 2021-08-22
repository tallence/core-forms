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
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.FormEditorHelper;

import static com.coremedia.cap.util.CapStructUtil.getInteger;
import static com.tallence.formeditor.FormEditorHelper.FORM_DATA;
import static com.tallence.formeditor.parser.AbstractFormElementParser.*;

abstract class AbstractFormValidator {

  public static final String FULLPATH_MIN_SIZE = FORM_DATA_VALIDATOR + "." + FORM_VALIDATOR_MINSIZE;
  public static final String FULLPATH_MAX_SIZE = FORM_DATA_VALIDATOR + "." + FORM_VALIDATOR_MAXSIZE;

  protected void addErrorIssue(Issues issues, String formElementId, String propertyName, String errorCode, Object... objects) {
    addIssue(issues, formElementId, propertyName, errorCode, Severity.ERROR, objects);
  }

  protected void addIssue(Issues issues, String formElementId, String propertyName, String errorCode, Severity severity, Object... objects) {
    String property = FORM_DATA + "." + FormEditorHelper.FORM_ELEMENTS + "." + formElementId + "." + propertyName;
    issues.addIssue(Severity.ERROR, property, errorCode, objects);
  }

  protected void validateMinSize(Integer minSize, Issues issues, String formElementId, String name) {
    if (minSize != null && minSize < 0) {
      addErrorIssue(issues, formElementId, FULLPATH_MIN_SIZE, "formfield_validator_invalid_minsize", name, minSize);
    }
  }

  protected void validateMaxSize(Integer maxSize, Integer minSize, Issues issues, String formElementId, String name) {
    if (maxSize != null && maxSize < 0) {
      addErrorIssue(issues, formElementId, FULLPATH_MAX_SIZE, "formfield_validator_invalid_maxsize", name, maxSize);
    } else if (maxSize != null && minSize != null && maxSize < minSize) {
      addErrorIssue(issues, formElementId, FULLPATH_MAX_SIZE, "formfield_validator_maxsize_smaller_minsize", name, maxSize);
    }
  }

  protected void validateMaxAndMinSize(Struct validator, Issues issues, String formElementId, String name) {
    // Size constraints
    Integer minSize = getInteger(validator, FORM_VALIDATOR_MINSIZE);
    Integer maxSize = getInteger(validator, FORM_VALIDATOR_MAXSIZE);
    validateMinSize(minSize, issues, formElementId, name);
    validateMaxSize(maxSize, minSize, issues, formElementId, name);
  }
}
