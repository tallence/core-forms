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

package com.tallence.formeditor.studio.validator.field;

import com.coremedia.blueprint.base.util.StructUtil;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.cae.parser.NumberFieldParser;
import org.springframework.stereotype.Component;

import static com.tallence.formeditor.cae.parser.AbstractFormElementParser.*;

/**
 * Validates, that sizeLimits in a numberField make sense.
 */
@Component
public class NumberFieldValidator extends AbstractFormValidator implements FieldValidator {

  @Override
  public boolean responsibleFor(String fieldType, Struct formElementData) {
    return NumberFieldParser.parserKey.equals(fieldType);
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {
    Struct validator = StructUtil.getSubstruct(fieldData, FORM_DATA_VALIDATOR);
    if (validator != null) {
      validateMaxAndMinSize(validator, issues, id, (String) fieldData.get(FORM_DATA_NAME));
    }
  }

}
