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

package com.tallence.formeditor.cae;

import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.cae.handler.CaptchaService;
import com.tallence.formeditor.contentbeans.FormEditor;
import com.tallence.formeditor.elements.FormElement;

import java.util.List;

/**
 * FreemarkerFacade for Form elements.
 *
 */
public class FormFreemarkerFacade {

  private final FormElementFactory formElementFactory;

  private final CaptchaService captchaService;

  private final CurrentContextService currentContextService;

  public FormFreemarkerFacade(FormElementFactory formElementFactory, CaptchaService pCaptchaService, CurrentContextService currentContextService) {
    this.captchaService = pCaptchaService;
    this.formElementFactory = formElementFactory;
    this.currentContextService = currentContextService;
  }

  public List<FormElement<?>> parseFormElements(FormEditor formEditor) {
    return FormEditorHelper.parseFormElements(formEditor.getContent(), formElementFactory);
  }

  public String getReCaptchaWebsiteSecretForSite() {
    return captchaService.getWebsiteSecretForSite(currentContextService.getContext());
  }
}
