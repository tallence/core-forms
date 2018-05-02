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

import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.elements.UsersMail;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

/**
 * Validator for elements of type {@link UsersMail}
 */
@Configured
public class UsersMailValidator implements Validator<UsersMail.UsersMailData> {

  private static final Pattern mailRegexp = Pattern.compile(".+@.+\\..+");

  @Configured
  private boolean mandatory;

  @Override
  public List<String> validate(UsersMail.UsersMailData value) {

    List<String> errors = new ArrayList<>();

    if (this.mandatory && (value == null || !hasText(value.getUsersMail()))) {
      errors.add("com.tallence.forms.usersmail.empty");
    }
    if (value != null && hasText(value.getUsersMail()) && !mailRegexp.matcher(value.getUsersMail()).matches()) {
      errors.add("com.tallence.forms.usersmail.invalid");
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
}
