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

package com.tallence.formeditor.cae.parser;

import com.coremedia.blueprint.common.navigation.Linkable;
import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.elements.DateField;
import com.tallence.formeditor.cae.validator.DateFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static com.coremedia.cap.util.CapStructUtil.getBoolean;
import static com.coremedia.cap.util.CapStructUtil.getSubstruct;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link DateField}
 */
@Component
public class DateFieldParser extends AbstractFormElementParser<DateField> {

  public static final String parserKey = "DateField";

  private CurrentContextService currentContextService;

  @Override
  public DateField instantiateType(Struct elementData) {
    return new DateField(getCurrentLocale());
  }

  @Override
  public void parseSpecialFields(DateField formElement, Struct elementData) {
    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      DateFieldValidator dateFieldValidator = new DateFieldValidator();

      dateFieldValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));
      if (getBoolean(validator, FORM_VALIDATOR_MINDATE_TODAY)) {
        dateFieldValidator.setMinDate(LocalDate.now());
      }
      if (getBoolean(validator, FORM_VALIDATOR_MAXDATE_TODAY)) {
        dateFieldValidator.setMaxDate(LocalDate.now());
      }

      formElement.setValidator(dateFieldValidator);
    });
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }

  @Autowired
  public void setCurrentContextService(CurrentContextService currentContextService) {
    this.currentContextService = currentContextService;
  }

  private Locale getCurrentLocale() {
    return Optional.ofNullable(currentContextService.getContext()).map(Linkable::getLocale).orElse(Locale.GERMANY);
  }
}
