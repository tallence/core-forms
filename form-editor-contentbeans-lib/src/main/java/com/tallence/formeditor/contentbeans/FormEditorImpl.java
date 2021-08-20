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

package com.tallence.formeditor.contentbeans;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.FormConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tallence.formeditor.FormConstants.FORM_DATA;
import static com.tallence.formeditor.FormConstants.FORM_ELEMENTS;

/**
 * Generated extension class for immutable beans of document type "JHContactForm".
 */
public class FormEditorImpl extends FormEditorBase {

  /**
   * {@inheritDoc}
   */
  @Override
  public Struct getFormElements() {
    return getSettingsService().setting(FORM_ELEMENTS, Struct.class, getContent().getStruct(FORM_DATA));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAdminEmails() {
    String[] adminMails = Optional.ofNullable(getContent().getString(ADMIN_MAILS)).orElse("").split(",");
    return Arrays.asList(adminMails);
  }
}
