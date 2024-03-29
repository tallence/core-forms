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

import com.tallence.formeditor.validator.DateFieldValidator;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Model bean for a configured DateField.
 * extra Template is required for this field.
 */
public class DateField extends AbstractFormElement<String, DateFieldValidator> {

  private final Locale locale;

  public DateField(Locale locale) {
    super(String.class);
    this.locale = locale;
  }

  @Override
  public String serializeValue() {
    String value = getValue();
    if (StringUtils.isBlank(value)) {
      return "";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);
    return DateFieldValidator.parseDate(value).map(z -> z.format(formatter)).orElse("");
  }
}
