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

package com.tallence.formeditor.validator;

import com.tallence.formeditor.elements.TextField;
import com.tallence.formeditor.validator.annotation.ValidationProperty;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Validator for elements of type {@link com.tallence.formeditor.elements.TextArea} and
 * {@link TextField}
 */
public class TextValidator implements Validator<String>, SizeValidator {

  private static final String MESSAGE_KEY_TEXTFIELD_REQUIRED = "com.tallence.forms.textfield.empty";
  private static final String MESSAGE_KEY_TEXTFIELD_MIN = "com.tallence.forms.textfield.min";
  private static final String MESSAGE_KEY_TEXTFIELD_MAX = "com.tallence.forms.textfield.max";
  private static final String MESSAGE_KEY_TEXTFIELD_REGEX = "com.tallence.forms.textfield.regex";

  @ValidationProperty(messageKey = MESSAGE_KEY_TEXTFIELD_REQUIRED)
  private boolean mandatory;

  @ValidationProperty(messageKey = MESSAGE_KEY_TEXTFIELD_MIN)
  private Integer minSize;

  @ValidationProperty(messageKey = MESSAGE_KEY_TEXTFIELD_MAX)
  private Integer maxSize;

  @ValidationProperty(messageKey = MESSAGE_KEY_TEXTFIELD_REGEX)
  private String regexp;

  @Override
  public List<ValidationFieldError> validate(String value) {

    List<ValidationFieldError> errors = new ArrayList<>();

    if (StringUtils.hasText(value)) {
      if (this.minSize != null && value.length() < this.minSize) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_TEXTFIELD_MIN, this.minSize));
      }
      if (this.maxSize != null && value.length() > this.maxSize) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_TEXTFIELD_MAX, this.maxSize));
      }
      if (this.regexp != null && !getRegexp().matcher(value).matches()) {
        errors.add(new ValidationFieldError(MESSAGE_KEY_TEXTFIELD_REGEX, this.regexp));
      }
    } else if (this.mandatory) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_TEXTFIELD_REQUIRED));
    }

    return errors;
  }


  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public Integer getMinSize() {
    return this.minSize;
  }

  public void setMinSize(Integer minSize) {
    this.minSize = minSize;
  }

  @Override
  public Integer getMaxSize() {
    return this.maxSize;
  }

  public void setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
  }

  public Pattern getRegexp() throws PatternSyntaxException {
    return Optional.ofNullable(this.regexp)
            .filter(StringUtils::hasText)
            .map(Pattern::compile)
            .orElse(null);
  }

  public void setRegexp(Pattern regexp) {
    this.regexp = regexp.pattern();
  }

  /**
   * Do not parse the {@link Pattern} here, because it might already fail: {@link PatternSyntaxException}
   * This case will be handled by the "com.tallence.formeditor.studio.validator.field.TextFieldValidator"
   * @param regexp the regular expression of the Field
   */
  public void setRegexp(String regexp) {
    this.regexp = regexp;
  }
}
