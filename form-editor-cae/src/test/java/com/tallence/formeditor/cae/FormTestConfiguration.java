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

import com.coremedia.blueprint.cae.config.BlueprintI18nCaeBaseLibConfiguration;
import com.coremedia.blueprint.cae.web.i18n.RequestMessageSource;
import com.coremedia.blueprint.cae.web.i18n.ResourceBundleInterceptor;
import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.coremedia.blueprint.testing.ContentTestConfiguration;
import com.coremedia.blueprint.testing.ContentTestHelper;
import com.coremedia.cache.Cache;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.cms.delivery.configuration.DeliveryConfigurationProperties;
import com.coremedia.objectserver.configuration.CaeConfigurationProperties;
import com.coremedia.objectserver.web.config.CaeHandlerServicesConfiguration;
import com.coremedia.objectserver.web.links.LinkFormatter;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.FormEditorConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.cae.actions.DefaultFormAction;
import com.tallence.formeditor.cae.actions.FormAction;
import com.tallence.formeditor.cae.handler.CaptchaService;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.cae.handler.FormConfigController;
import com.tallence.formeditor.cae.handler.FormController;
import com.tallence.formeditor.cae.handler.ReCaptchaServiceImpl;
import com.tallence.formeditor.parser.AbstractFormElementParser;
import com.tallence.formeditor.cae.serializer.FormElementSerializerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Configuration class to set up form test infrastructure.
 */
@ImportResource(
        value = {
                "classpath:/META-INF/coremedia/component-forms.xml",
                "classpath:/com/tallence/formeditor/cae/testdata/bundle-replace-context.xml",}
        , reader = ResourceAwareXmlBeanDefinitionReader.class)
@PropertySource("classpath:com/tallence/formeditor/cae/test.properties")
@EnableConfigurationProperties({
        DeliveryConfigurationProperties.class,
        CaeConfigurationProperties.class
})
@Import({
        BlueprintI18nCaeBaseLibConfiguration.class,
        CaeHandlerServicesConfiguration.class,
        ContentTestConfiguration.class,
        WebMvcAutoConfiguration.class,
        FormEditorConfiguration.class
})
public class FormTestConfiguration {

  private static final String CONTENT_REPOSITORY = "classpath:/com/tallence/formeditor/testdata/contenttest.xml";

  @Bean
  @Scope(SCOPE_SINGLETON)
  public XmlUapiConfig xmlUapiConfig() {
    return new XmlUapiConfig(CONTENT_REPOSITORY);
  }

  @Bean
  @Scope(SCOPE_SINGLETON)
  public ContentTestHelper contentTestHelper() {
    return new ContentTestHelper();
  }

  @Bean
  @Scope(SCOPE_SINGLETON)
  public FormElementFactory formElementFactory(List<AbstractFormElementParser<? extends FormElement<?>>> parsers) {
    return new FormElementFactory(parsers);
  }

  /**
   * Mocking the {@link CaptchaService} it is not yet tested.
   */
  @Bean
  @Scope(SCOPE_SINGLETON)
  public CaptchaService captchaService() {
    return new ReCaptchaServiceImpl(new ReCaptchaServiceImpl.ReCaptchaAuthentication(null, null));
  }

  @Bean
  @Scope(SCOPE_SINGLETON)
  public FormFreemarkerFacade freemarkerFacade(FormElementFactory formElementFactory, CaptchaService captchaService, CurrentContextService currentContextService) {
    return new FormFreemarkerFacade(formElementFactory, captchaService, currentContextService);
  }

  @Bean
  MockMvc mockMvc(WebApplicationContext wac) {
    return MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Bean
  FormController formController(List<FormAction> formActions,
                                DefaultFormAction defaultFormAction,
                                CaptchaService captchaService,
                                FormFreemarkerFacade formFreemarkerFacade,
                                CurrentContextService currentContextService,
                                RequestMessageSource messageSource,
                                ResourceBundleInterceptor pageResourceBundlesInterceptor,
                                @Value("${formEditor.cae.encodeData:true}") boolean encodeFormData) {
    return new FormController(formActions, defaultFormAction, captchaService, formFreemarkerFacade, currentContextService, messageSource, pageResourceBundlesInterceptor, encodeFormData);
  }

  @Bean
  FormConfigController formConfigController(CurrentContextService currentContextService,
                                            FormFreemarkerFacade formFreemarkerFacade,
                                            LinkFormatter linkFormatter,
                                            RequestMessageSource messageSource,
                                            ResourceBundleInterceptor pageResourceBundlesInterceptor,
                                            Cache cache, List<FormElementSerializerFactory<?>> formElementSerializerFactories) {
    return new FormConfigController(currentContextService,
            formFreemarkerFacade,
            linkFormatter,
            messageSource,
            pageResourceBundlesInterceptor,
            cache,
            formElementSerializerFactories);
  }


}
