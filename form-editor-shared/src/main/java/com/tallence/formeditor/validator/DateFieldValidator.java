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

package com.tallence.formeditor.validator;

import com.tallence.formeditor.validator.annotation.ValidationMessage;
import com.tallence.formeditor.validator.annotation.ValidationProperty;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ValidationMessage(name = "date", messageKey = DateFieldValidator.MESSAGE_KEY_DATEFIELD_PATTERN)
public class DateFieldValidator implements Validator<String> {

  private static final String MESSAGE_KEY_DATEFIELD_MANDATORY = "com.tallence.forms.datefield.mandatory";
  protected static final String MESSAGE_KEY_DATEFIELD_PATTERN = "com.tallence.forms.datefield.invalid";
  private static final String MESSAGE_KEY_DATEFIELD_MINDATE = "com.tallence.forms.datefield.min";
  private static final String MESSAGE_KEY_DATEFIELD_MAXDATE = "com.tallence.forms.datefield.max";

  @ValidationProperty(messageKey = MESSAGE_KEY_DATEFIELD_MANDATORY)
  private boolean mandatory;

  @ValidationProperty(messageKey = MESSAGE_KEY_DATEFIELD_MINDATE)
  private LocalDate minDate;

  @ValidationProperty(messageKey = MESSAGE_KEY_DATEFIELD_MAXDATE)
  private LocalDate maxDate;

  @Override
  public List<ValidationFieldError> validate(String value) {

    if (StringUtils.isBlank(value) && mandatory) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_DATEFIELD_MANDATORY));
    }

    LocalDate inputDate = parseDate(value).orElse(null);
    if (inputDate == null) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_DATEFIELD_PATTERN));
    }
    if (minDate != null && inputDate.isBefore(minDate)) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_DATEFIELD_MINDATE, minDate));
    }
    if (maxDate != null && inputDate.isAfter(maxDate)) {
      return Collections.singletonList(new ValidationFieldError(MESSAGE_KEY_DATEFIELD_MAXDATE, maxDate));
    }

    return Collections.emptyList();
  }

  /**
   * parse the input value,
   * multiple possible formats are allowed:
   * - LocalDateTime: 2020-12-12T12:12:12.000
   * - ZonedDateTime: 2020-12-12T12:12:12.000+2:00
   * - LocalDate: 2020-12-12
   *
   * the time information is irrelevant, internally it just uses the date part.
   *
   * @param dateValue string value
   * @return parsed LocalDate
   */
  public static Optional<LocalDate> parseDate(String dateValue) {
    try {
      //zoned date time used to parse ISO 8601 dates 2020-12-12T12:12:12.000
      return Optional.of(LocalDateTime.parse(dateValue).toLocalDate());
    } catch (DateTimeParseException ignored) {
    }
    try {
      //if it failed due to format, try to parse ISO date 2020-12-12T12:12:12.000+2:00
      return Optional.of(ZonedDateTime.parse(dateValue).toLocalDate());
    } catch (DateTimeParseException ignored) {
    }
    try {
      //if it failed due to format, try to parse ISO date 2020-12-12
      return Optional.of(LocalDate.parse(dateValue));
    } catch (DateTimeParseException ignored) {
    }
    return Optional.empty();
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public LocalDate getMinDate() {
    return minDate;
  }

  public void setMinDate(LocalDate minDate) {
    this.minDate = minDate;
  }

  public LocalDate getMaxDate() {
    return maxDate;
  }

  public void setMaxDate(LocalDate maxDate) {
    this.maxDate = maxDate;
  }
}
