/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tallence.formeditor.studio.validator;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.rest.validation.Severity;
import com.coremedia.rest.validation.impl.Issue;
import com.coremedia.rest.validation.impl.IssuesImpl;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FormEditorValidatorTest.LocalConfig.class)
public class FormEditorValidatorTest {

  @Autowired
  private ContentRepository contentRepository;

  @Autowired
  private FormEditorValidator formEditorValidator;

  @Test
  public void testValidDoc() {
    Content testContent = contentRepository.getContent("2");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);
    // Ensure, no validation errors occurred.
    assertFalse(issues.hasIssueAtSeverityOrWorse(Severity.INFO));
  }

  @Test
  public void testInvalidTextField() {
    Content testContent = contentRepository.getContent("4");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);
    // Ensure, all expected validation errors occurred.
    assertEquals(3, issues.getByProperty().get(FormEditor.FORM_ELEMENTS).size());
    Issue<Content> issue1 = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ELEMENTS, "invalid_minsize", null);
    Issue<Content> issue2 = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ELEMENTS, "invalid_maxsize", null);
    Issue<Content> issue3 = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ELEMENTS, "form_action_invalid_regexp", null);
    assertThat(issues.getByProperty().get(FormEditor.FORM_ELEMENTS), hasItems(issue1, issue2, issue3));
  }

  @Test
  public void testInvaildMailAction() {
    Content testContent = contentRepository.getContent("6");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);
    // Ensure, all expected validation errors occurred.
    assertEquals(1, issues.getByProperty().get(FormEditor.FORM_ACTION).size());
    Issue<Content> issue1 = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ACTION, "form_action_mail", null);
    assertThat(issues.getByProperty().get(FormEditor.FORM_ACTION), hasItem(issue1));

    assertEquals(1, issues.getByProperty().get(FormEditor.FORM_ELEMENTS).size());
    Issue<Content> issue2 = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ELEMENTS, "form_action_mail_file", null);
    assertThat(issues.getByProperty().get(FormEditor.FORM_ELEMENTS), hasItem(issue2));
  }

  @Test
  public void testInvalidFieldName() {
    Content testContent = contentRepository.getContent("8");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);
    // Ensure, all expected validation errors occurred.
    assertEquals(1, issues.getByProperty().get(FormEditor.FORM_ELEMENTS).size());
    Issue<Content> issue = new Issue<>(testContent, Severity.ERROR, FormEditor.FORM_ELEMENTS, "missing_name", null);
    assertThat(issues.getByProperty().get(FormEditor.FORM_ELEMENTS), hasItem(issue));
  }

  @Configuration
  @ImportResource(
          value = {"classpath:/META-INF/coremedia/component-form-editor-studio-lib.xml"},
          reader = ResourceAwareXmlBeanDefinitionReader.class
  )
  @Import(XmlRepoConfiguration.class)
  public static class LocalConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public XmlUapiConfig xmlUapiConfig() {
      return new XmlUapiConfig("classpath:/com/tallence/formeditor/studio/validator/FormEditorValidatorTest-content.xml");
    }
  }
}