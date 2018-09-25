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

package com.tallence.formeditor.studio.validator;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.cap.validation.ContentTypeValidatorBase;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.contentbeans.FormEditor;
import com.tallence.formeditor.studio.validator.field.FieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Validates, that a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.
 */
public class FormEditorValidator extends ContentTypeValidatorBase {

  @Autowired
  private List<FieldValidator> fieldValidators;

  private Map<String, List<FieldValidator>> fieldValidatorsByType = new HashMap<>();

  @PostConstruct
  public void init() {
    fieldValidators.forEach(validator -> {
      validator.resonsibleFor().forEach(type -> {
        if (!fieldValidatorsByType.containsKey(type)) {
          fieldValidatorsByType.put(type, new ArrayList<>());
        }
        fieldValidatorsByType.get(type).add(validator);
      });
    });
  }

  @Override
  public void validate(Content content, Issues issues) {

    Struct formData = content.getStruct(FormEditor.FORM_DATA);
    String action = content.getString(FormEditor.FORM_ACTION);

    // Validate form fields
    if (formData.get(FormEditor.FORM_ELEMENTS) != null) {
      Struct formElements = formData.getStruct(FormEditor.FORM_ELEMENTS);

      formElements.getProperties().values()
              .stream()
              .filter(Struct.class::isInstance)
              .map(Struct.class::cast)
              .forEach(fieldStruct -> {
                String type = (String) fieldStruct.get("type");
                Optional.ofNullable(fieldValidatorsByType.get(type)).ifPresent(fieldValidatorsForType -> {
                  for (FieldValidator fieldValidator : fieldValidatorsForType) {
                    fieldValidator.validateField(fieldStruct, action, issues);
                  }
                });
              });
    }

    // Further validations
    if (FormEditor.MAIL_ACTION.equals(action) && !StringUtils.hasText(content.getString(FormEditor.ADMIN_MAILS))) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ACTION, "form_action_mail");
    }
  }
}
