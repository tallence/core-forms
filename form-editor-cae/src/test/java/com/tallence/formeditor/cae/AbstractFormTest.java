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
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.CACHE;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.CONTENT_BEAN_FACTORY;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.DATA_VIEW_FACTORY;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.ID_PROVIDER;
import static com.coremedia.cap.test.xmlrepo.XmlRepoResources.LINK_FORMATTER;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Abstract Test class to set up form test infrastructure.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AbstractFormTest.LocalConfig.class)
@ActiveProfiles(AbstractFormTest.LocalConfig.PROFILE)
public abstract class AbstractFormTest {


  @Configuration
  @ImportResource(
      value = {
          CACHE,
          CONTENT_BEAN_FACTORY,
          DATA_VIEW_FACTORY,
          ID_PROVIDER,
          LINK_FORMATTER,
          "classpath:/com/tallence/formeditor/contentbeans/formeditor-contentbeans.xml",
          "classpath:/com/tallence/formeditor/cae/test-forms-config.xml"
      },
      reader = ResourceAwareXmlBeanDefinitionReader.class
  )
  @Import({XmlRepoConfiguration.class})
  @Profile(AbstractFormTest.LocalConfig.PROFILE)
  public static class LocalConfig {
    public static final String PROFILE = "FormContentBeanTestBase";
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

  }

  @Inject
  private ContentTestHelper contentTestHelper;


  protected <T> T getContentBean(int id) {
    return contentTestHelper.getContentBean(id);
  }

}
