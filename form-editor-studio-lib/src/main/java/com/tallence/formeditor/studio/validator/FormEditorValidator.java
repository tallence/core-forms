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
import com.tallence.formeditor.studio.validator.field.FormFieldMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Validates, 3 things:
 *
 * <ul>
 *   <li>a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.</li>
 *   <li>Calls each spring bean of instance {@link FieldValidator} for validating the configs ({@link FieldValidator#validateField})</li>
 *   <li>Calls each spring bean of instance {@link FieldValidator} to make sure, specific fields are configured in the form ({@link FieldValidator#checkFormFieldMatch})</li>
 * </ul>
 *
 * <p>the third point could have been built in a separate Studio-Lib-Validator. But form field structs would be parsed
 * twice in that case, which might affect the performance.</p>
 */
public class FormEditorValidator extends ContentTypeValidatorBase {

  @Autowired
  private List<FieldValidator> fieldValidators;
  private String formDataProperty = FormEditor.FORM_DATA;
  private String formActionProperty = FormEditor.FORM_ACTION;

  @Override
  public void validate(Content content, Issues issues) {

    Struct formData = content.getStruct(formDataProperty);
    String action = content.getString(formActionProperty);

    // Validate form fields
    FormFieldMatch formFieldMatch = new FormFieldMatch();
    if (formData != null && formData.get(FormEditor.FORM_ELEMENTS) != null) {
      Struct formElements = formData.getStruct(FormEditor.FORM_ELEMENTS);

      formElements.getProperties().entrySet()
              .stream()
              .filter(set -> set.getValue() instanceof Struct)
              .forEach(formElementEntry -> validateFormElement(issues, formFieldMatch, content, action, formElementEntry));
    }

    fieldValidators.forEach(v -> v.handleFieldMatches(formFieldMatch, content, issues));

    // Further validations
    if (FormEditor.MAIL_ACTION.equals(action) && !StringUtils.hasText(content.getString(FormEditor.ADMIN_MAILS))) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ACTION, "form_action_mail");
    }
  }

  private void validateFormElement(Issues issues, FormFieldMatch formFieldMatch, Content content, String action, Map.Entry<String, Object> formElementEntry) {
    String formElementKey = formElementEntry.getKey();
    Struct formElementData = (Struct) formElementEntry.getValue();
    String type = (String) formElementData.get("type");

    //Apply the responsible validators to the current form-key and -data
    fieldValidators.stream()
        .filter(v -> v.responsibleFor(type, formElementData))
        .forEach(fieldValidator -> {
          fieldValidator.validateField(formElementKey, formElementData, action, issues);
          fieldValidator.checkFormFieldMatch(formElementKey, formElementData, formFieldMatch, content, action);
        });
  }

  public void setFormDataProperty(String formDataProperty) {
    this.formDataProperty = formDataProperty;
  }

  public void setFormActionProperty(String formActionProperty) {
    this.formActionProperty = formActionProperty;
  }
}
