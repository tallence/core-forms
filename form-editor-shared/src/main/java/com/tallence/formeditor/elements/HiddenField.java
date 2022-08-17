/*
 * Copyright 2021 Tallence AG
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

package com.tallence.formeditor.elements;

import com.tallence.formeditor.validator.TextOnlyValidator;

/**
 * Model bean for a configured HiddenField.
 * No special logic required here: the hidden value will be part of the form submit request and
 * ist set as a value into this field model via {@link AbstractFormElement#setValue}.
 */
public class HiddenField extends AbstractFormElement<String, TextOnlyValidator> {

  public HiddenField() {
    super(String.class);
  }

}
