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

import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.handler.FormController.FormProcessingResult;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.tallence.formeditor.cae.handler.FormErrors.DB_SAVE;
import static com.tallence.formeditor.cae.handler.FormErrors.USER_MAIL;

/**
 * Default Action for the form framework. Saves the formData in a custom DB, e.g. Elastic Social mongoDB.
 *
 */
@Component
public class DefaultFormAction extends AbstractFormAction {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultFormAction.class);

  protected static final String ACTION_KEY = "default";

  private final FormEditorStorageAdapter storageAdapter;

  @Autowired
  public DefaultFormAction(FormEditorStorageAdapter storageAdapter, FormEditorMailAdapter mailAdapter) {
    super(mailAdapter);
    this.storageAdapter = storageAdapter;
  }

  @Override
  public FormProcessingResult handleFormSubmit(FormEditor target, List<MultipartFile> files, List<FormElement> formElements, HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {

    String formData = serializeFormElements(target, formElements, files);
    LOG.debug("Saving form request for form [{}] with formData [{}] in storage layer", target.getContentId(), formData);
    if (!storageAdapter.persistFormData(target, formData, formElements, files)) {
      return new FormProcessingResult(false, DB_SAVE);
    }

    boolean errorSendingUserMail = sendUserConfirmationMail(target, formElements, formData, request, response, files);

    return new FormProcessingResult(true, errorSendingUserMail ? USER_MAIL : null);
  }


  @Override
  public boolean isResponsible(String key) {
    return ACTION_KEY.equals(key);
  }
}
