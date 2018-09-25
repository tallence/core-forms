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

package com.tallence.formeditor.cae.validator;

import com.tallence.formeditor.cae.elements.TextField;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator for elements of type {@link com.tallence.formeditor.cae.elements.TextArea} and
 * {@link TextField}
 */
public class TextValidator implements Validator<String> {

  private boolean mandatory;
  private Integer minSize;
  private Integer maxSize;
  private Pattern regexp;

  @Override
  public List<String> validate(String value) {

    List<String> errors = new ArrayList<>();

    if (StringUtils.hasText(value)) {
      if (this.minSize != null && value.length() < this.minSize) {
        errors.add("com.tallence.forms.textfield.tooshort");
      }
      if (this.maxSize != null && value.length() > this.maxSize) {
        errors.add("com.tallence.forms.textfield.toolong");
      }
      if (this.regexp != null && !regexp.matcher(value).matches()) {
        errors.add("com.tallence.forms.textfield.regexp");
      }
    } else if (this.mandatory) {
      errors.add("com.tallence.forms.textfield.empty");
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

  public Integer getMinSize() {
    return this.minSize;
  }

  public void setMinSize(Integer minSize) {
    this.minSize = minSize;
  }

  public Integer getMaxSize() {
    return this.maxSize;
  }

  public void setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
  }

  public Pattern getRegexp() {
    return this.regexp;
  }

  public void setRegexp(String regexp) {
    if (StringUtils.hasText(regexp)) {
      this.regexp = Pattern.compile(regexp);
    } else {
      this.regexp = null;
    }
  }
}
