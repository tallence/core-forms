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

import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.elements.ConsentFormCheckBox;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_DATA_HINT;
import static com.tallence.formeditor.parser.ConsentFormCheckBoxParser.FORM_LINK_TARGET;

/**
 * Validates, that ConsentFormCheckBoxes have a text and a link.
 */
@Component
public class ConsentFormValidator extends AbstractFormValidator<ConsentFormCheckBox> {

  public ConsentFormValidator() {
    super(ConsentFormCheckBox.class);
  }

  @Override
  public void validateField(ConsentFormCheckBox formElement, String action, Issues issues) {

    String name = formElement.getName();
    if (formElement.getLinkTarget() == null) {
      addErrorIssue(issues, formElement.getStructId(), FORM_LINK_TARGET, "consentForm_missing_linkTarget", name);
    }

    String hint = formElement.getHint();
    if (StringUtils.isEmpty(hint)) {
      addErrorIssue(issues, formElement.getStructId(), FORM_DATA_HINT, "consentForm_missing_hint", name);
    } else if (!hint.matches(".*%.+%.*")) {
      addErrorIssue(issues, formElement.getStructId(), FORM_DATA_HINT, "consentForm_invalid_hint", name);
    }
  }
}
