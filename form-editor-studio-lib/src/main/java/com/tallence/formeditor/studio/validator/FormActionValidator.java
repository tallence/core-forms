/*
 * $Id$
 * (c) 2013 Tallence GmbH
 */
package com.tallence.formeditor.studio.validator;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.rest.cap.validation.ContentTypeValidatorBase;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.contentbeans.FormEditor;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Validates, that a form with form action "mailAction" does not have a fileUpload-field and has a mail-address entered.
 *
 */
public class FormActionValidator extends ContentTypeValidatorBase {

  private static final List<String> forbiddenFormFields = Collections.singletonList("FileUpload");

  @Override
  public void validate(Content content, Issues issues) {

    Struct formData = content.getStruct(FormEditor.FORM_DATA);
    String action = content.getString(FormEditor.FORM_ACTION);

    if (!FormEditor.MAIL_ACTION.equals(action)) {
      return;
    }

    if (formData != null && formData.get(FormEditor.FORM_ELEMENTS) != null) {
      Struct formElements = formData.getStruct(FormEditor.FORM_ELEMENTS);

      if (formElements.getProperties().values().stream().map(v -> (Struct) v).anyMatch(s -> forbiddenFormFields.contains(s.get("type").toString()))) {
        issues.addIssue(Severity.ERROR, FormEditor.FORM_ACTION, "form_action_mail_file");
      }
    }

    if (isEmpty(content.getString(FormEditor.ADMIN_MAILS))) {
      issues.addIssue(Severity.ERROR, FormEditor.FORM_ACTION, "form_action_mail");
    }
  }
}
