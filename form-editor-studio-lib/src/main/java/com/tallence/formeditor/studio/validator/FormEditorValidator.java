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
import com.coremedia.rest.cap.validation.ContentTypeValidatorBase;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.parser.form.FormEditorParserService;
import com.tallence.formeditor.studio.validator.field.FieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Validates, that a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.
 */
public class FormEditorValidator extends ContentTypeValidatorBase {

  private final FormEditorParserService formEditorParserService;

  public FormEditorValidator(FormEditorParserService formEditorParserService) {
    this.formEditorParserService = formEditorParserService;
  }

  @Autowired
  private List<FieldValidator> fieldValidators;
  //Can be overwritten, see the setters below
  private String formActionProperty = FormEditorHelper.FORM_ACTION;

  @Override
  public void validate(Content content, Issues issues) {

    String action = content.getString(formActionProperty);

    // Validate form fields
    formEditorParserService.parseFormElements(content)
            .forEach(formElement -> validateFormElement(issues, action, formElement));

    // Further validations
    if (FormEditorHelper.MAIL_ACTION.equals(action) && !StringUtils.hasText(content.getString(FormEditorHelper.ADMIN_MAILS))) {
      issues.addIssue(Severity.ERROR, FormEditorHelper.FORM_ACTION, "form_action_mail");
    }
  }

  private void validateFormElement(Issues issues, String action, FormElement<?> formElement) {
    //Apply the responsible validators to the current form-key and -data
    fieldValidators.forEach(fieldValidator -> fieldValidator.validateFieldIfResponsible(formElement, action, issues));
  }

  public void setFormActionProperty(String formActionProperty) {
    this.formActionProperty = formActionProperty;
  }
}
