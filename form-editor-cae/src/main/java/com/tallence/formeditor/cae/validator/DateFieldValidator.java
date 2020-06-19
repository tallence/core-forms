/*
 * Copyright 2020 Tallence AG
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

import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DateFieldValidator implements Validator<String> {

  private boolean mandatory;
  private ZonedDateTime minDate;
  private ZonedDateTime maxDate;

  @Override
  public List<String> validate(String value) {
    if (StringUtils.isBlank(value)) {
      return mandatory ? Collections.singletonList("com.tallence.forms.datefield.empty") : Collections.emptyList();
    }

    ZonedDateTime currentDate = parseDate(value).orElse(null);
    if (currentDate == null) {
      return Collections.singletonList("com.tallence.forms.datefield.invalid");
    }
    if (minDate != null && currentDate.isBefore(minDate)) {
      return Collections.singletonList("com.tallence.forms.datefield.min");
    }
    if (maxDate != null && currentDate.isAfter(minDate)) {
      return Collections.singletonList("com.tallence.forms.datefield.max");
    }

    return Collections.emptyList();
  }

  public static Optional<ZonedDateTime> parseDate(String dateValue) {
    try {
      return Optional.of(ZonedDateTime.parse(dateValue));
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public void setMinDate(ZonedDateTime minDate) {
    this.minDate = minDate;
  }

  public void setMaxDate(ZonedDateTime maxDate) {
    this.maxDate = maxDate;
  }

  public ZonedDateTime getMinDate() {
    return minDate;
  }

  public ZonedDateTime getMaxDate() {
    return maxDate;
  }


}
