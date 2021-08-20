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

import com.tallence.formeditor.elements.UsersMail;
import com.tallence.formeditor.validator.annotation.ValidationMessage;
import com.tallence.formeditor.validator.annotation.ValidationProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.tallence.formeditor.validator.UsersMailValidator.MESSAGE_KEY_USERMAIL_EMAIL;
import static org.springframework.util.StringUtils.hasText;

/**
 * Validator for elements of type {@link UsersMail}
 */
@ValidationMessage(name="email", messageKey = MESSAGE_KEY_USERMAIL_EMAIL)
public class UsersMailValidator implements Validator<UsersMail.UsersMailData> {

  protected static final String MESSAGE_KEY_USERMAIL_EMAIL = "com.tallence.forms.usersmail.invalid";
  protected static final String MESSAGE_KEY_USERMAIL_REQUIRED = "com.tallence.forms.usersmail.empty";

  private static final Pattern mailRegexp = Pattern.compile(".+@.+\\..+");

  @ValidationProperty(messageKey = MESSAGE_KEY_USERMAIL_REQUIRED)
  private boolean mandatory;

  @Override
  public List<ValidationFieldError> validate(UsersMail.UsersMailData value) {

    List<ValidationFieldError> errors = new ArrayList<>();

    if (this.mandatory && (value == null || !hasText(value.getUsersMail()))) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_USERMAIL_REQUIRED));
    }

    if (value != null && hasText(value.getUsersMail()) && !mailRegexp.matcher(value.getUsersMail()).matches()) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_USERMAIL_EMAIL));
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
