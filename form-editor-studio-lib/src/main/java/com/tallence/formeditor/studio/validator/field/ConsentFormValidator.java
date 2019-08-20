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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static com.tallence.formeditor.cae.parser.AbstractFormElementParser.FORM_DATA_HINT;
import static com.tallence.formeditor.cae.parser.AbstractFormElementParser.FORM_DATA_NAME;
import static com.tallence.formeditor.cae.parser.ConsentFormCheckBoxParser.CONSENT_FORM_CHECK_BOX_TYPE;
import static com.tallence.formeditor.cae.parser.ConsentFormCheckBoxParser.FORM_LINK_TARGET;

/**
 * Validates, that ConsentFormCheckBoxes have a text and a link.
 */
@Component
public class ConsentFormValidator extends AbstractFormValidator implements FieldValidator {
  @Override
  public List<String> resonsibleFor() {
    return Collections.singletonList(CONSENT_FORM_CHECK_BOX_TYPE);
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {

    String name = StructUtil.getString(fieldData, FORM_DATA_NAME);
    if (StructUtil.getLinks(fieldData, FORM_LINK_TARGET).isEmpty()) {
      addErrorIssue(issues, id, FORM_LINK_TARGET, "consentForm_missing_linkTarget", name);
    }

    String hint = StructUtil.getString(fieldData, FORM_DATA_HINT);
    if (StringUtils.isEmpty(hint)) {
      addErrorIssue(issues, id, FORM_DATA_HINT, "consentForm_missing_hint", name);
    } else if (!hint.matches(".*%.+%.*")) {
      addErrorIssue(issues, id, FORM_DATA_HINT, "consentForm_invalid_hint", name);
    }
  }
}
