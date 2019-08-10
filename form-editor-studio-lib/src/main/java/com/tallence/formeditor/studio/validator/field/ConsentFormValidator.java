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
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.validation.Issues;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Validates, that ConsentFormCheckBoxes have a text and a link.
 */
@Component
public class ConsentFormValidator extends AbstractFormValidator implements FieldValidator {
  @Override
  public List<String> resonsibleFor() {
    return Collections.singletonList("ConsentFormCheckBox");
  }

  @Override
  public void validateField(String id, Struct fieldData, String action, Issues issues) {

    String name = StructUtil.getString(fieldData, "name");
    if (fieldData.get("linkTarget") == null || ((List<Content>) fieldData.get("linkTarget")).isEmpty()) {
      addErrorIssue(issues, id, "linkTarget", "consentForm_missing_linkTarget", name);
    }

    String hint = StructUtil.getString(fieldData, "hint");
    if (StringUtils.isEmpty(hint)) {
      addErrorIssue(issues, id, "hint", "consentForm_missing_hint", name);
    } else if (!hint.matches(".*%.+%.*")) {
      addErrorIssue(issues, id, "hint", "consentForm_invalid_hint", name);
    }
  }
}
