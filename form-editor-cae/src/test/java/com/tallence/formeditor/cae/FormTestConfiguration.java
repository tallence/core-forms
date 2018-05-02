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

import com.coremedia.blueprint.testing.ContentTestHelper;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.cae.elements.AbstractFormElement;
import com.tallence.formeditor.cae.handler.ReCaptchaService;
import com.tallence.formeditor.cae.handler.ReCaptchaServiceImpl;
import com.tallence.formeditor.cae.parser.ElementInitializer;
import com.tallence.formeditor.cae.parser.ElementPropertyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;

import java.util.Collection;

import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.CACHE;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.CONTENT_BEAN_FACTORY;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.DATA_VIEW_FACTORY;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.HANDLERS;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.ID_PROVIDER;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.LINK_FORMATTER;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Configuration class to set up form test infrastructure.
 */
@Configuration
@ImportResource(
    value = {
        CACHE,
        CONTENT_BEAN_FACTORY,
        DATA_VIEW_FACTORY,
        ID_PROVIDER,
        HANDLERS,
        LINK_FORMATTER,
        "classpath:/com/tallence/formeditor/contentbeans/formeditor-contentbeans.xml",
        "classpath:/framework/spring/blueprint-handlers.xml",
        "classpath:/framework/spring/blueprint-contentbeans.xml"
    },
    reader = ResourceAwareXmlBeanDefinitionReader.class
)
@ComponentScan(basePackages = "com.tallence.formeditor.cae")
@Import({XmlRepoConfiguration.class})
public class FormTestConfiguration {

  private static final String CONTENT_REPOSITORY = "classpath:/com/tallence/formeditor/cae/testdata/contenttest.xml";

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
  public FormElementFactory formElementFactory(Collection<AbstractFormElement> formElements,
                                               Collection<ElementPropertyConfigurer> configurers,
                                               Collection<ElementInitializer> initializers) {
    return new FormElementFactory(formElements, configurers, initializers);
  }

  /**
   * Mocking the {@link ReCaptchaService} it is not yet tested.
   */
  @Bean
  @Scope(SCOPE_SINGLETON)
  public ReCaptchaService reCaptchaService() {
    return new ReCaptchaServiceImpl(new ReCaptchaServiceImpl.ReCaptchaAuthentication(null, null));
  }

  @Bean
  @Scope(SCOPE_SINGLETON)
  public FormFreemarkerFacade freemarkerFacade(FormElementFactory formElementFactory) {
    return new FormFreemarkerFacade(formElementFactory);
  }

}
