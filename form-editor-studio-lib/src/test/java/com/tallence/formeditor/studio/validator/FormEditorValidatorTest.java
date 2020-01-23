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

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
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
    assertEquals(1, issues.getByProperty().get("formData.formElements.TextField.validator.minSize").size());
    assertEquals(1, issues.getByProperty().get("formData.formElements.TextField.validator.maxSize").size());
    assertEquals(1, issues.getByProperty().get("formData.formElements.TextField.validator.regexpValidator").size());
    Issue<Content> issue1 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.TextField.validator.minSize", "formfield_validator_invalid_minsize", Arrays.asList("TextField", -1));
    Issue<Content> issue2 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.TextField.validator.maxSize", "formfield_validator_invalid_maxsize", Arrays.asList("TextField", -1));
    Issue<Content> issue3 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.TextField.validator.regexpValidator", "formfield_validator_invalid_regexp", Collections.emptyList());
    assertEquals(issues.getByProperty().get("formData.formElements.TextField.validator.minSize").iterator().next(), issue1);
    assertEquals(issues.getByProperty().get("formData.formElements.TextField.validator.maxSize").iterator().next(), issue2);
    assertEquals(issues.getByProperty().get("formData.formElements.TextField.validator.regexpValidator").iterator().next(), issue3);
  }

  @Test
  public void testMissingConsentForm() {
    Content testContent = contentRepository.getContent("4");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);

    // Ensure, all expected validation errors occurred.
    assertEquals(1, issues.getByProperty().get("formData.formElements").size());
    Issue<Content> consentFormIssue = new Issue<>(testContent, Severity.WARN, "formData.formElements", "consentForm_missing_field", Collections.emptyList());
    assertEquals(issues.getByProperty().get("formData.formElements").iterator().next(), consentFormIssue);
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

    String key = "formData.formElements.FileUpload.name";
    assertEquals(1, issues.getByProperty().get(key).size());
    Issue<Content> issue2 = new Issue<>(testContent, Severity.ERROR, key, "form_action_mail_file", Collections.emptyList());
    assertThat(issues.getByProperty().get(key), hasItem(issue2));
  }

  @Test
  public void testEmptyOptions() {
    Content testContent = contentRepository.getContent("10");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);

    String key = "formData.formElements.Test Buttons without options.groupElements";
    Issue<Content> issue1 = new Issue<>(testContent, Severity.ERROR, key, "radiobuttons_missing_options", Collections.singletonList("without buttons"));
    Set<Issue<Content>> formElementIssues = issues.getByProperty().get(key);
    assertEquals(formElementIssues.iterator().next(), issue1);

    assertThat(formElementIssues.size(), equalTo(1));
  }

  @Test
  public void testConsentForm() {
    Content testContent = contentRepository.getContent("12");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);


    Issue<Content> issue1 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.ConsentForm missing hint.hint", "consentForm_missing_hint", Collections.singletonList("ConsentForm missing hint"));
    assertEquals(issues.getByProperty().get("formData.formElements.ConsentForm missing hint.hint").iterator().next(), issue1);

    Issue<Content> issue2 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.ConsentForm missing target.linkTarget", "consentForm_missing_linkTarget", Collections.singletonList("ConsentForm missing target"));
    assertEquals(issues.getByProperty().get("formData.formElements.ConsentForm missing target.linkTarget").iterator().next(), issue2);

    Issue<Content> issue3 = new Issue<>(testContent, Severity.ERROR, "formData.formElements.ConsentForm invalid hint.hint", "consentForm_invalid_hint", Collections.singletonList("ConsentForm invalid hint"));
    assertEquals(issues.getByProperty().get("formData.formElements.ConsentForm invalid hint.hint").iterator().next(), issue3);
  }

  @Test
  public void testInvalidFieldName() {
    Content testContent = contentRepository.getContent("8");
    IssuesImpl<Content> issues = new IssuesImpl<>(testContent, testContent.getProperties().keySet());
    formEditorValidator.validate(testContent, issues);
    // Ensure, all expected validation errors occurred.
    String key = "formData.formElements.TextField.name";

    assertEquals(1, issues.getByProperty().get(key).size());
    Issue<Content> issue = new Issue<>(testContent, Severity.ERROR, key, "formField_missing_name", Collections.singletonList("TextField"));
    assertThat(issues.getByProperty().get(key), hasItem(issue));
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
