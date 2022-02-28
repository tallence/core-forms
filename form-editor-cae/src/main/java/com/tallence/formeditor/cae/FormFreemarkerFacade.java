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
import com.tallence.formeditor.cae.handler.ReCaptchaService;
import com.tallence.formeditor.contentbeans.FormEditor;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.parser.form.FormEditorParserService;

import java.util.List;

/**
 * FreemarkerFacade for Form elements.
 *
 */
public class FormFreemarkerFacade {

  private final ReCaptchaService reCaptchaService;

  private final CurrentContextService currentContextService;

  private final FormEditorParserService formEditorParserService;

  public FormFreemarkerFacade(ReCaptchaService pReCaptchaService,
                              CurrentContextService currentContextService,
                              FormEditorParserService formEditorParserService) {
    this.reCaptchaService = pReCaptchaService;
    this.currentContextService = currentContextService;
    this.formEditorParserService = formEditorParserService;
  }

  public List<FormElement<?>> parseFormElements(FormEditor formEditor) {
    return formEditorParserService.parseFormElements(formEditor.getContent());
  }

  public String getReCaptchaWebsiteSecretForSite() {
    return reCaptchaService.getWebsiteSecretForSite(currentContextService.getContext());
  }
}
