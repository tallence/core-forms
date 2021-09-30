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

package com.tallence.formeditor.cae.actions;

import com.tallence.formeditor.elements.FileUpload;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.elements.TextOnly;
import com.tallence.formeditor.elements.UsersMail;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract form action which provides common form methods
 *
 */
public abstract class AbstractFormAction implements FormAction {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractFormAction.class);

  protected final FormEditorMailAdapter mailAdapter;

  @Autowired
  public AbstractFormAction(FormEditorMailAdapter mailAdapter) {
    this.mailAdapter = mailAdapter;
  }

  protected boolean sendUserConfirmationMail(FormEditor target, List<FormElement<?>> formElements,
                                             String formData, HttpServletRequest request, HttpServletResponse response,
                                             List<MultipartFile> files) {
    boolean errorSendingUserMail = false;

    // filter for UserMail-fields with sendCopy activated.
    Optional<String> userMailOptional = formElements.stream()
            .filter(element -> (element instanceof UsersMail))
            .map(element -> ((UsersMail) element).getValue())
            .filter(Objects::nonNull)
            .filter(UsersMail.UsersMailData::isSendCopy)
            .map(UsersMail.UsersMailData::getUsersMail)
            .findFirst();

    if (userMailOptional.isPresent()) {
      String userMail = userMailOptional.get();

      try {
        mailAdapter.sendUserMail(target, userMail, formData, formElements, files);
      } catch (Exception e) {
        LOG.error("Confirmation Mail to user " + userMail + " could not be sent! For Form  " + target.getContentId(), e);
        errorSendingUserMail = true;
      }

    }

    return errorSendingUserMail;
  }


  protected String serializeFormElements(FormEditor target, List<FormElement<?>> formElements, List<MultipartFile> files) {
    StringBuilder formDataBuilder = new StringBuilder();
    //Serialize all non-FileUploads. FileUploads are serialized a few lines below.
    formElements.stream().filter((element) -> !(element instanceof FileUpload))
        .forEach((element) -> serializeFormElementForMail(target, formDataBuilder, element));

    files.stream().filter(Objects::nonNull).forEach((file) -> {
      formDataBuilder.append(file.getName());
      formDataBuilder.append(": ");
      formDataBuilder.append(file.getOriginalFilename());
      formDataBuilder.append("<br/>");
    });
    return formDataBuilder.toString();
  }


  protected void serializeFormElementForMail(FormEditor target, StringBuilder formDataBuilder, FormElement<?> element) {
    formDataBuilder.append(element.getName());
    formDataBuilder.append(": ");
    if (element instanceof TextOnly) {
      formDataBuilder.append(element.getHint());
    } else {
      formDataBuilder.append(element.serializeValue());
    }
    formDataBuilder.append("<br/>");
  }

}
