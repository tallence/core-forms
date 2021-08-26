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
package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.SitesService;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.FormElement;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class FormEditorAdapter {
  private final Content content;
  private final SitesService sitesService;
  private final FormElementFactory formElementFactory;
  private final ThreadLocal<Locale> localeThreadLocal;

  public FormEditorAdapter(Content content, SitesService sitesService, FormElementFactory formElementFactory, ThreadLocal<Locale> localeThreadLocal) {
    this.content = content;
    this.sitesService = sitesService;
    this.formElementFactory = formElementFactory;
    this.localeThreadLocal = localeThreadLocal;
  }

  @SuppressWarnings("unused")
  public List<FormElement<?>> formElements() {

    //The formElements parsers might need the locale of the current site.
    localeThreadLocal.set(sitesService.getContentSiteAspect(content).getLocale());

    return FormEditorHelper.parseFormElements(content, formElementFactory);
  }

  @SuppressWarnings("unused")
  public List<String> adminEmails() {

    String[] adminMails = Optional.ofNullable(content.getString(FormEditorHelper.ADMIN_MAILS)).orElse("").split(",");
    return Arrays.asList(adminMails);
  }
}
