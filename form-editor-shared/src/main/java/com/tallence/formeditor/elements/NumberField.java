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

package com.tallence.formeditor.elements;

import com.coremedia.cap.content.Content;
import com.tallence.formeditor.validator.NumberValidator;

/**
 * Model bean for a configured NumberField.
 */
public class NumberField extends AbstractFormElement<String, NumberValidator> {

  public NumberField(Content formEditor) {
    super(String.class, formEditor);
  }

}
