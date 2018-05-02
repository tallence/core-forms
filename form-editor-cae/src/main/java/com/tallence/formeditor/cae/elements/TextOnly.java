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

package com.tallence.formeditor.cae.elements;

import com.tallence.formeditor.cae.annotations.FormElementDefinition;
import com.tallence.formeditor.cae.validator.TextOnlyValidator;

import java.util.Map;

/**
 * Model bean for a configured TextOnly field.
 */
@FormElementDefinition
public class TextOnly extends AbstractFormElement<String, TextOnlyValidator> {

  public TextOnly() {
    super(String.class);
  }


  @Override
  public String serializeValue() {
    return "";
  }


  @Override
  public void fillFormData(Map<String, String> formData) {
    //Do nothing, no formData needed for TextOnly
  }
}
