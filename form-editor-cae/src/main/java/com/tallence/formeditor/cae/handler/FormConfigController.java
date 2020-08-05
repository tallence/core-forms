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
import com.coremedia.blueprint.common.contentbeans.CMContext;
import com.coremedia.blueprint.common.navigation.Navigation;
import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.coremedia.cache.Cache;
import com.coremedia.objectserver.web.links.Link;
import com.coremedia.objectserver.web.links.LinkFormatter;
import com.tallence.formeditor.cae.FormFreemarkerFacade;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.model.FormEditorConfig;
import com.tallence.formeditor.cae.serializer.FormConfigCacheKey;
import com.tallence.formeditor.cae.serializer.ValidationSerializationHelper;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.coremedia.objectserver.web.HandlerHelper.MODEL_ROOT;

@Link
@RequestMapping
@Component
public class FormConfigController {

  private static final Logger LOG = LoggerFactory.getLogger(FormConfigController.class);

  private static final String FORMS_ROOT_URL_SEGMENT = "/dynamic/forms";
  private static final String FORM_EDITOR_CONFIG_VIEW = "formEditorConfig";
  public static final String FORM_EDITOR_CONFIG_URL = FORMS_ROOT_URL_SEGMENT + "/" + FORM_EDITOR_CONFIG_VIEW + "/{currentContext}/{editor}";

  private final CurrentContextService currentContextService;
  private final FormFreemarkerFacade formFreemarkerFacade;
  private final LinkFormatter linkFormatter;
  private final RequestMessageSource messageSource;
  private final ResourceBundleInterceptor pageResourceBundlesInterceptor;
  private final ValidationSerializationHelper validationSerializationHelper;
  private final Cache cache;

  public FormConfigController(CurrentContextService currentContextService,
                              FormFreemarkerFacade formFreemarkerFacade,
                              LinkFormatter linkFormatter,
                              RequestMessageSource messageSource,
                              ResourceBundleInterceptor pageResourceBundlesInterceptor,
                              ValidationSerializationHelper validationSerializationHelper,
                              Cache cache) {
    this.currentContextService = currentContextService;
    this.formFreemarkerFacade = formFreemarkerFacade;
    this.linkFormatter = linkFormatter;
    this.messageSource = messageSource;
    this.pageResourceBundlesInterceptor = pageResourceBundlesInterceptor;
    this.validationSerializationHelper = validationSerializationHelper;
    this.cache = cache;
  }

  @Link(type = FormEditor.class, view = FORM_EDITOR_CONFIG_VIEW, uri = FORM_EDITOR_CONFIG_URL)
  public UriComponents buildLinkForFormConfig(FormEditor form, UriComponentsBuilder uriComponentsBuilder) {
    return uriComponentsBuilder.buildAndExpand(currentContextService.getContext().getContentId(), form.getContentId());
  }

  @ResponseBody
  @RequestMapping(value = FORM_EDITOR_CONFIG_URL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String getFormConfig(@PathVariable CMChannel currentContext,
                              @PathVariable FormEditor editor,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {

    Navigation navigation = getNavigationForContext(currentContext);

    List<FormElement> formElements = formFreemarkerFacade.parseFormElements(editor);
    if (formElements.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      return "";
    }

    //set up message interceptor
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject(MODEL_ROOT, navigation);
    pageResourceBundlesInterceptor.postHandle(request, response, null, modelAndView);
    request.setAttribute(NavigationLinkSupport.ATTR_NAME_CMNAVIGATION, navigation);

    //prepare form config
    FormEditorConfig formEditorConfig = new FormEditorConfig();
    formEditorConfig.setFormActionUrl(linkFormatter.formatLink(editor, FormController.FORM_EDITOR_SUBMIT_VIEW, request, response, false));
    formEditorConfig.setFormElements(formElements);
    return this.cache.get(new FormConfigCacheKey(
            editor,
            target -> linkFormatter.formatLink(target, null, request, response, false),
            (key, args) -> messageSource.getMessage(key, args, navigation.getLocale()),
            validationSerializationHelper,
            formEditorConfig));

  }

  private Navigation getNavigationForContext(CMContext currentContext) {
    if (currentContext == null) {
      currentContext = currentContextService.getContext();
    }
    return currentContext.getRootNavigation();
  }

}
