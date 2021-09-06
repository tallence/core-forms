/*
 * Copyright 2020 Tallence AG
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

package com.tallence.formeditor.cae.handler;

import com.coremedia.blueprint.cae.web.i18n.RequestMessageSource;
import com.coremedia.blueprint.cae.web.i18n.ResourceBundleInterceptor;
import com.coremedia.blueprint.cae.web.links.NavigationLinkSupport;
import com.coremedia.blueprint.common.contentbeans.CMChannel;
import com.coremedia.blueprint.common.navigation.Navigation;
import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.coremedia.cache.Cache;
import com.coremedia.objectserver.web.links.Link;
import com.coremedia.objectserver.web.links.LinkFormatter;
import com.tallence.formeditor.cae.FormFreemarkerFacade;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.cae.model.FormEditorConfig;
import com.tallence.formeditor.cae.serializer.FormConfigCacheKey;
import com.tallence.formeditor.cae.serializer.FormElementSerializerFactory;
import com.tallence.formeditor.contentbeans.FormEditor;
import com.tallence.formeditor.parser.CurrentFormSupplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.coremedia.objectserver.web.HandlerHelper.MODEL_ROOT;


/**
 * Controller to provide a {@link FormEditor} object as JSON based representation which can be used by any frontend application.
 */
@Link
@RequestMapping
@Component
public class FormConfigController {

  private static final String FORMS_ROOT_URL_SEGMENT = "/dynamic/forms";
  private static final String FORM_EDITOR_CONFIG_VIEW = "formEditorConfig";
  public static final String FORM_EDITOR_CONFIG_URL = FORMS_ROOT_URL_SEGMENT + "/" + FORM_EDITOR_CONFIG_VIEW + "/{currentContext}/{editor}";

  private final CurrentContextService currentContextService;
  private final FormFreemarkerFacade formFreemarkerFacade;
  private final LinkFormatter linkFormatter;
  private final RequestMessageSource messageSource;
  private final ResourceBundleInterceptor pageResourceBundlesInterceptor;
  private final Cache cache;
  private final List<FormElementSerializerFactory<?>> formElementSerializerFactories;

  public FormConfigController(CurrentContextService currentContextService,
                              FormFreemarkerFacade formFreemarkerFacade,
                              LinkFormatter linkFormatter,
                              RequestMessageSource messageSource,
                              ResourceBundleInterceptor pageResourceBundlesInterceptor,
                              Cache cache, List<FormElementSerializerFactory<?>> formElementSerializerFactories) {
    this.currentContextService = currentContextService;
    this.formFreemarkerFacade = formFreemarkerFacade;
    this.linkFormatter = linkFormatter;
    this.messageSource = messageSource;
    this.pageResourceBundlesInterceptor = pageResourceBundlesInterceptor;
    this.cache = cache;
    this.formElementSerializerFactories = formElementSerializerFactories;
  }

  /**
   * Link builder for {@link FormEditor} config endpoint
   */
  @SuppressWarnings("unused")
  @Link(type = FormEditor.class, view = FORM_EDITOR_CONFIG_VIEW, uri = FORM_EDITOR_CONFIG_URL)
  public UriComponents buildLinkForFormConfig(FormEditor form, UriComponentsBuilder uriComponentsBuilder) {
    return uriComponentsBuilder.buildAndExpand(currentContextService.getContext().getContentId(), form.getContentId());
  }

  /**
   *  The REST endpoint will provide an enriched JSON representation of {@link FormEditor}.
   *  The resulting JSON contains information about all all fields, including validation settings and messages, url to the submit endpoint, etc.
   *
   * @return JSON String
   */
  @ResponseBody
  @GetMapping(value = FORM_EDITOR_CONFIG_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String getFormConfig(@PathVariable CMChannel currentContext,
                              @PathVariable FormEditor editor,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {

    Navigation navigation = currentContext.getRootNavigation();
    request.setAttribute(NavigationLinkSupport.ATTR_NAME_CMNAVIGATION, navigation);
    CurrentFormSupplier.setCurrentFormLocale(editor.getContent());

    List<FormElement<?>> formElements = formFreemarkerFacade.parseFormElements(editor);
    if (formElements.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "no form elements configured");
    }

    //set up message interceptor
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject(MODEL_ROOT, navigation);
    pageResourceBundlesInterceptor.postHandle(request, response, null, modelAndView);

    //prepare form config
    FormEditorConfig formEditorConfig = new FormEditorConfig();
    formEditorConfig.setFormActionUrl(linkFormatter.formatLink(editor, FormController.FORM_EDITOR_SUBMIT_VIEW, request, response, false));
    formEditorConfig.setFormElements(formElements);
    return this.cache.get(new FormConfigCacheKey(
            editor,
            request, response,
            (key, args) -> messageSource.getMessage(key, args, navigation.getLocale()),
            formEditorConfig,
            formElementSerializerFactories));

  }

}
