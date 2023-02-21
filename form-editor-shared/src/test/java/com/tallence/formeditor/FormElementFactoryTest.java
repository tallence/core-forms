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
package com.tallence.formeditor;

import com.coremedia.cap.common.IdHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.elements.*;
import com.tallence.formeditor.parser.AbstractFormElementParser;
import com.tallence.formeditor.validator.InvalidGroupElementException;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static com.coremedia.cap.common.CapStructHelper.getStruct;
import static com.tallence.formeditor.parser.ConsentFormCheckBoxParser.CONSENT_FORM_CHECK_BOX_TYPE;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Tests the parsing, serialization and validation of each form field.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FormElementFactoryTest.LocalTestConfig.class)
public class FormElementFactoryTest {

  @ImportResource(reader = ResourceAwareXmlBeanDefinitionReader.class)
  @Import({XmlRepoConfiguration.class, MultiSiteConfiguration.class})
  @ComponentScan(basePackages = "com.tallence.formeditor.parser")
  public static class LocalTestConfig {
    private static final String CONTENT_REPOSITORY = "classpath:/com/tallence/formeditor/testdata/contenttest.xml";

    @Bean
    @Scope(SCOPE_SINGLETON)
    public XmlUapiConfig xmlUapiConfig() {
      return new XmlUapiConfig(CONTENT_REPOSITORY);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public FormElementFactory formElementFactory(List<AbstractFormElementParser<? extends FormElement<?>>> parsers) {
      return new FormElementFactory(parsers);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public Supplier<Locale> localeSupplier() {
      return () -> Locale.US;
    }
  }


  @Autowired
  private FormElementFactory factory;

  @Autowired
  private ContentRepository contentRepository;


  @Nullable
  private <T extends FormElement<?>> T getTestFormElement(String id) {
    Content form = contentRepository.getContent(IdHelper.formatContentId(2));
    var element = FormEditorHelper.getFormElements(form)
            .map(formElements -> getStruct(formElements, id))
            .orElse(null);
    if (element == null) {
      return null;
    }
    return this.factory.createFormElement(form, element, id);
  }

  @Test
  public void testTextField() {
    TextField formElement = getTestFormElement("TextField");

    assertNotNull(formElement);
    assertThat(formElement.getPlaceholder(), is("Platzhalter"));

    //123 is too short and does not match the regex validator
    formElement.setValue("123");
    assertThat(formElement.getValidationResult().size(), is(2));
    formElement.setValue("");
    assertThat(formElement.getValidationResult().size(), is(1));

    //12346 ist not valid, because of the regex validator ("12345")
    formElement.setValue("12346");
    assertThat(formElement.getValidationResult().size(), is(1));
    formElement.setValue("12345");
    assertTrue(formElement.getValidationResult().isEmpty());
  }


  @Test
  public void testNumberField() {
    NumberField formElement = getTestFormElement("NumberField");

    assertNotNull(formElement);
    formElement.setValue("abc");
    assertThat(formElement.getValidationResult().size(), is(1));
    formElement.setValue("2");
    assertThat(formElement.getValidationResult().size(), is(1));
    formElement.setValue("150");
    assertTrue(formElement.getValidationResult().isEmpty());
  }

  @Test
  public void testRadioButton() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsMandatory");

    assertNotNull(formElement);
    formElement.setValue("value_456");
    assertTrue(formElement.getValidationResult().isEmpty());
    formElement.setValue("value_123");
    assertTrue(formElement.getValidationResult().isEmpty());
    assertThat(formElement.serializeValue(), is("display_123"));
    formElement.setValue("");
    assertThat(formElement.getValidationResult().size(), is(1));


    assertThat(formElement.getOptions(), notNullValue());
    assertThat(formElement.getOptions().get(1).isSelectedByDefault(), is(true));

  }

  @Test
  public void testDependentFields() {
    TextField formElement = getTestFormElement("DependentField");

    assertNotNull(formElement);
    assertEquals("myComplexCustomId", formElement.getAdvancedSettings().getCustomId());
    assertEquals(Integer.valueOf(3), formElement.getAdvancedSettings().getColumnWidth());
    assertTrue(formElement.getAdvancedSettings().isBreakAfterElement());
    assertEquals("RadioButtonsOptional", formElement.getAdvancedSettings().getDependentElementId());
    assertEquals("value_456", formElement.getAdvancedSettings().getDependentElementValue());
    assertTrue(formElement.getAdvancedSettings().isVisibilityDependent());
  }

  @Test
  public void testOptionalRadioButton() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsOptional");

    assertNotNull(formElement);

    formElement.setValue(null);
    assertEquals("", formElement.serializeValue());

  }

  @Test (expected = InvalidGroupElementException.class)
  public void testRadioButtonInvalid() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsEmptyValidator");
    assertNotNull(formElement);
    //An Exception is expected here
    formElement.setValue("invalid");
    assertThat(formElement.getValidationResult().size(), is(1));
  }

  @Test
  public void testCheckBoxes() {
    CheckBoxesGroup formElement = getTestFormElement("CheckBoxesMandatory");

    assertNotNull(formElement);
    formElement.setValue(Arrays.asList("value_123", "value_456"));
    assertTrue(formElement.getValidationResult().isEmpty());
    assertThat(formElement.serializeValue(), is("[display_123, display_456]"));

    formElement.setValue(Collections.emptyList());
    assertThat(formElement.getValidationResult().size(), is(1));
    assertThat(formElement.getOptions(), notNullValue());
    assertThat(formElement.getOptions().get(1).isSelectedByDefault(), is(true));

  }

  @Test (expected = InvalidGroupElementException.class)
  public void testCheckBoxesInvalid() {
    CheckBoxesGroup formElement = getTestFormElement("CheckBoxesEmptyValidator");
    assertNotNull(formElement);
    //An Exception is expected here
    formElement.setValue(Arrays.asList("invalid", "value_123"));
    assertThat(formElement.getValidationResult().size(), is(1));
  }

  @Test
  public void testSelectBoxes() {
    SelectBox formElement = getTestFormElement("SelectBoxMandatory");

    assertNotNull(formElement);
    formElement.setValue("value_123");
    assertTrue(formElement.getValidationResult().isEmpty());
    assertThat(formElement.serializeValue(), is("display_123"));

    formElement.setValue(null);
    assertThat(formElement.getValidationResult().size(), is(1));

  }

  @Test (expected = InvalidGroupElementException.class)
  public void testSelectBoxesInvalid() {
    SelectBox formElement = getTestFormElement("SelectBoxEmptyValidator");
    assertNotNull(formElement);
    //An Exception is expected here
    formElement.setValue("invalid");
    formElement.getValidationResult();
  }

  @Test
  public void testSelectBoxesOnlyDisplayName() {
    SelectBox formElement = getTestFormElement("SelectBoxOnlyDisplayName");

    assertNotNull(formElement);
    formElement.setValue("display_123");
    assertTrue(formElement.getValidationResult().isEmpty());
    assertThat(formElement.serializeValue(), is("display_123"));

    formElement.setValue(null);
    assertThat(formElement.getValidationResult().size(), is(1));

  }



  @Test
  public void testTextArea() {
    TextArea formElement = getTestFormElement("TextArea");

    assertNotNull(formElement);
    formElement.setValue("123");
    assertTrue(formElement.getValidationResult().isEmpty());
    formElement.setValue(null);
    assertThat(formElement.getValidationResult().size(), is(1));
    formElement.setValue("12345678900");
    assertThat(formElement.getValidationResult().size(), is(1));

    assertThat(formElement.getColumns(), is(4));
    assertThat(formElement.getRows(), is(5));
  }

  @Test
  public void testTextOnly() {
    TextOnly formElement = getTestFormElement("TextOnly");

    assertNotNull(formElement);
    assertThat(formElement.getHint(), is("Das ist ein langer Text zur Erklärung des Formulars"));
    assertThat(formElement.getName(), is("TextOnly"));
  }

  @Test
  public void testConsentFormCheckBox() {
    ConsentFormCheckBox formElement = getTestFormElement(CONSENT_FORM_CHECK_BOX_TYPE);

    assertNotNull(formElement);
    assertThat(formElement.getHint(), is("Please confirm the %data protection consent form%"));
    assertNotNull(formElement.getLinkTarget());
    assertThat(IdHelper.parseContentId(formElement.getLinkTarget().getId()), equalTo(8));
  }

  @Test
  public void testZipField() {
    ZipField formElement = getTestFormElement("ZipFieldTest");
    assertNotNull(formElement);

    assertThat(formElement.getHint(), is("Bitte Ihre Postleitzahl eingeben"));
    assertThat(formElement.getValidator().getRegexp().toString(), equalTo("\\d{5}"));

    //the field is mandatory -> an error without a value
    assertThat(formElement.getValidationResult().size(), is(1));

    formElement.setValue("22945");
    assertTrue(formElement.getValidationResult().isEmpty());
  }

  @Test
  public void testPhoneField() {
    PhoneField formElement = getTestFormElement("PhoneFieldTest");
    assertNotNull(formElement);
    assertEquals(formElement.getName(), "Phone");
  }

  @Test
  public void testFaxField() {
    FaxField formElement = getTestFormElement("FaxFieldTest");
    assertNotNull(formElement);
    assertEquals(formElement.getName(), "Fax");
  }

  @Test
  public void testStreetNumberField() {
    StreetNumberField formElement = getTestFormElement("StreetFieldTest");
    assertNotNull(formElement);
    assertEquals(formElement.getName(), "Street and number");
  }

  @Test
  public void testDateField_min() {
    DateField formElement = getTestFormElement("DateFieldMin");
    assertNotNull(formElement);
    assertEquals(formElement.getName(), "DateField min");

    assertThat(formElement, is(instanceOf(DateField.class)));
    assertFalse(formElement.getValidationResult().isEmpty());

    formElement.setValue("abc");
    assertThat(formElement.getValidationResult().size(), is(1));

    formElement.setValue(ZonedDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(1));

    formElement.setValue(ZonedDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(0));
  }

  @Test
  public void testDateField_max() {
    DateField formElement = getTestFormElement("DateFieldMax");
    assertNotNull(formElement);
    assertEquals(formElement.getName(), "DateField max");

    assertThat(formElement, is(instanceOf(DateField.class)));
    assertFalse(formElement.getValidationResult().isEmpty());

    formElement.setValue("abc");
    assertThat(formElement.getValidationResult().size(), is(1));

    //using ZondeDateTime here, frontend is sending including time info
    // max date + 3
    formElement.setValue(ZonedDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(1));

    // max date - 3
    formElement.setValue(ZonedDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(0));

    // same date as max date
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(0));

    //same check for different date format
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult().size(), is(0));

    //same check for different date format
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    assertThat(formElement.getValidationResult().size(), is(0));

  }
}
