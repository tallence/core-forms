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
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.rest.cap.validation.ContentTypeValidatorBase;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.elements.PageElement;
import com.tallence.formeditor.parser.CurrentFormSupplier;
import com.tallence.formeditor.studio.validator.field.FieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * Validates, that a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.
 */
public class FormEditorValidator extends ContentTypeValidatorBase {

  private final ThreadLocal<Locale> localeThreadLocal;
  private final FormElementFactory formElementFactory;
  private final SitesService sitesService;

  public FormEditorValidator(ThreadLocal<Locale> localeThreadLocal, FormElementFactory formElementFactory, SitesService sitesService) {
    this.localeThreadLocal = localeThreadLocal;
    this.formElementFactory = formElementFactory;
    this.sitesService = sitesService;
  }

  @Autowired
  private List<FieldValidator> fieldValidators;
  //Can be overwritten, see the setters below
  private String formActionProperty = FormEditorHelper.FORM_ACTION;

  @Override
  public void validate(Content content, Issues issues) {

    CurrentFormSupplier.setCurrentForm(content);

    String action = content.getString(formActionProperty);

    // Validate form fields
    localeThreadLocal.set(sitesService.getContentSiteAspect(content).getLocale());
    var formElements = FormEditorHelper.parseFormElements(content, formElementFactory);
    formElements.forEach(formElement -> validateFormElement(issues, action, formElement));

    if (formElements.stream().anyMatch(f -> f instanceof PageElement) &&
            formElements.stream().anyMatch(f -> !(f instanceof PageElement))) {
      //Expect only PageElements or no PageElement
      issues.addIssue(Severity.ERROR, FormEditorHelper.FORM_DATA, "formField_ordering_error");
    }

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
