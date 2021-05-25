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
import com.tallence.formeditor.cae.FormEditorHelper;
import com.tallence.formeditor.studio.validator.field.FieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Validates, that a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.
 */
public class FormEditorValidator extends ContentTypeValidatorBase {

  @Autowired
  private List<FieldValidator> fieldValidators;
  //Can be overwritten, see the setters below
  private String formDataProperty = FormEditorHelper.FORM_DATA;
  private String formActionProperty = FormEditorHelper.FORM_ACTION;

  @Override
  public void validate(Content content, Issues issues) {

    Struct formData = content.getStruct(formDataProperty);
    String action = content.getString(formActionProperty);

    // Validate form fields
    if (formData != null && formData.get(FormEditorHelper.FORM_ELEMENTS) != null) {
      Struct formElements = formData.getStruct(FormEditorHelper.FORM_ELEMENTS);

      formElements.getProperties().entrySet()
              .stream()
              .filter(set -> set.getValue() instanceof Struct)
              .forEach(formElementEntry -> validateFormElement(issues, action, formElementEntry));
    }

    // Further validations
    if (FormEditorHelper.MAIL_ACTION.equals(action) && !StringUtils.hasText(content.getString(FormEditorHelper.ADMIN_MAILS))) {
      issues.addIssue(Severity.ERROR, FormEditorHelper.FORM_ACTION, "form_action_mail");
    }
  }

  private void validateFormElement(Issues issues, String action, Map.Entry<String, Object> formElementEntry) {
    String formElementKey = formElementEntry.getKey();
    Struct formElementData = (Struct) formElementEntry.getValue();
    String type = (String) formElementData.get("type");

    //Apply the responsible validators to the current form-key and -data
    fieldValidators.stream()
            .filter(v -> v.responsibleFor(type, formElementData))
            .forEach(fieldValidator -> fieldValidator.validateField(formElementKey, formElementData, action, issues));
  }

  public void setFormDataProperty(String formDataProperty) {
    this.formDataProperty = formDataProperty;
  }

  public void setFormActionProperty(String formActionProperty) {
    this.formActionProperty = formActionProperty;
  }
}
