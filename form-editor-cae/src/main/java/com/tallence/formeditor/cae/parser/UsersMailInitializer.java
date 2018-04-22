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

package com.tallence.formeditor.cae.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.annotations.Initializer;
import com.tallence.formeditor.cae.elements.UsersMail;

@Initializer(UsersMail.class)
public class UsersMailInitializer implements ElementInitializer<UsersMail> {

  private static final String COPY_TYPE = "copyType";
  private static final String LEGACY_COPY = "copy";

  @Override
  public void initialize(UsersMail element, Struct configurationData) {
    if (configurationData.get(COPY_TYPE) != null) {
      element.setCopyBoxOption(UsersMail.CopyBoxOption.valueOf(configurationData.getString(COPY_TYPE)));
    } else if (configurationData.get(LEGACY_COPY) != null && configurationData.getBoolean(LEGACY_COPY)) {
      element.setCopyBoxOption(UsersMail.CopyBoxOption.CHECKBOX);
    }
  }
}
