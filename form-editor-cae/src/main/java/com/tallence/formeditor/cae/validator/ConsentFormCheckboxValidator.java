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

import java.util.Collections;
import java.util.List;

/**
 * Validator for the {@link com.tallence.formeditor.cae.elements.ConsentFormCheckBox}
 *
 */
public class ConsentFormCheckboxValidator implements Validator<Boolean> {

  private boolean mandatory;

  @Override
  public List<String> validate(Boolean value) {
    if (mandatory && value != null && !value) {
      return Collections.singletonList("com.tallence.forms.consentForm.notChecked");
    }
    return Collections.emptyList();
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }
}
