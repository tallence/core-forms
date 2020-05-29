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

import com.coremedia.blueprint.base.util.StructUtil;
import com.coremedia.cap.struct.Struct;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Complex value can be used for CheckBoxes, RadioButtons and SelectBoxes
 */
public class ComplexValue {

  public static final String CHECKED_BY_DEFAULT = "checkedByDefault";
  private static final String PROPERTY_VALUE = "value";

  private final String displayName;
  private final String value;
  private final boolean selectedByDefault;

  public ComplexValue(String displayName, Struct data) {
    this.displayName = displayName;
    this.value = Optional.ofNullable(data).map(d -> StructUtil.getString(d, PROPERTY_VALUE)).filter(StringUtils::isNotBlank).orElse(displayName);
    this.selectedByDefault = data != null && data.get(CHECKED_BY_DEFAULT) != null && data.getBoolean(CHECKED_BY_DEFAULT);
  }

  /**
   * represents the submit value
   *
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * represents the display value
   *
   * @return
   */
  public String getDisplayName() {
    return displayName;
  }

  public boolean isSelectedByDefault() {
    return selectedByDefault;
  }
}
