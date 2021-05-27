package com.tallence.formeditor.cae;

import com.coremedia.cap.common.IdHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.undoc.common.spring.CapRepositoriesConfiguration;
import com.tallence.formeditor.FormEditorConfiguration;
import com.tallence.formeditor.cae.elements.CheckBoxesGroup;
import com.tallence.formeditor.cae.elements.ConsentFormCheckBox;
import com.tallence.formeditor.cae.elements.DateField;
import com.tallence.formeditor.cae.elements.FaxField;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.elements.NumberField;
import com.tallence.formeditor.cae.elements.PhoneField;
import com.tallence.formeditor.cae.elements.RadioButtonGroup;
import com.tallence.formeditor.cae.elements.SelectBox;
import com.tallence.formeditor.cae.elements.StreetNumberField;
import com.tallence.formeditor.cae.elements.TextArea;
import com.tallence.formeditor.cae.elements.TextField;
import com.tallence.formeditor.cae.elements.TextOnly;
import com.tallence.formeditor.cae.elements.ZipField;
import com.tallence.formeditor.cae.validator.InvalidGroupElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.tallence.formeditor.cae.parser.ConsentFormCheckBoxParser.CONSENT_FORM_CHECK_BOX_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = {
        CapRepositoriesConfiguration.class,
        XmlRepoConfiguration.class,
        MultiSiteConfiguration.class,
        FormEditorConfiguration.class})
@TestPropertySource(properties = {
        "repository.factoryClassName=com.coremedia.cap.xmlrepo.XmlCapConnectionFactory",
        "repository.params.contentxml=classpath:/com/tallence/formeditor/cae/testdata/contenttest.xml",
})
public class FormElementFactoryTest {


  @Autowired
  private ContentRepository contentRepository;

  @Autowired
  private FormElementFactory formElementFactory;

  @Autowired
  private SitesService sitesService;

  private <T extends FormElement> T getTestFormElement(String id) {
    Content content = contentRepository.getContent(IdHelper.formatContentId(2));
    Struct formElements = FormEditorHelper.getFormElements(content)
            .orElseThrow(() -> new IllegalStateException("Could not load form data."));
    return formElementFactory.createFormElement(formElements.getStruct(id), id, sitesService.getContentSiteAspect(content).getLocale());
  }

  @Test
  public void testTextField() {
    TextField formElement = getTestFormElement("TextField");

    assertThat(formElement).isInstanceOf(TextField.class);
    assertThat(formElement.getPlaceholder()).isEqualTo("Platzhalter");

    //123 is too short and does not match the regex validator
    formElement.setValue("123");
    assertThat(formElement.getValidationResult()).hasSize(2);
    formElement.setValue("");
    assertThat(formElement.getValidationResult()).hasSize(1);

    //12346 ist not valid, because of the regex validator ("12345")
    formElement.setValue("12346");
    assertThat(formElement.getValidationResult()).hasSize(1);
    formElement.setValue("12345");
    assertThat(formElement.getValidationResult()).isEmpty();
  }


  @Test
  public void testNumberField() {
    NumberField formElement = getTestFormElement("NumberField");

    assertThat(formElement).isInstanceOf(NumberField.class);
    formElement.setValue("abc");
    assertThat(formElement.getValidationResult()).hasSize(1);
    formElement.setValue("2");
    assertThat(formElement.getValidationResult()).hasSize(1);
    formElement.setValue("150");
    assertThat(formElement.getValidationResult()).isEmpty();
  }

  @Test
  public void testRadioButton() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsMandatory");

    assertThat(formElement).isInstanceOf(RadioButtonGroup.class);
    formElement.setValue("value_456");
    assertThat(formElement.getValidationResult()).isEmpty();
    formElement.setValue("value_123");
    assertThat(formElement.getValidationResult()).isEmpty();
    assertThat(formElement.serializeValue()).isEqualTo("display_123");
    formElement.setValue("");
    assertThat(formElement.getValidationResult()).hasSize(1);


    assertThat(formElement.getOptions()).isNotNull();
    assertThat(formElement.getOptions().get(1).isSelectedByDefault()).isTrue();

  }

  @Test
  public void testDependentFields() {
    TextField formElement = getTestFormElement("DependentField");

    assertThat(formElement.getAdvancedSettings().getCustomId()).isEqualTo("myComplexCustomId");
    assertThat(formElement.getAdvancedSettings().getColumnWidth()).isEqualTo(3);
    assertThat(formElement.getAdvancedSettings().isBreakAfterElement()).isTrue();
    assertThat(formElement.getAdvancedSettings().getDependentElementId()).isEqualTo("RadioButtonsOptional");
    assertThat(formElement.getAdvancedSettings().getDependentElementValue()).isEqualTo("value_456");
    assertThat(formElement.getAdvancedSettings().isVisibilityDependent()).isTrue();
  }

  @Test
  public void testOptionalRadioButton() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsOptional");
    assertThat(formElement).isInstanceOf(RadioButtonGroup.class);
    formElement.setValue(null);
    assertThat(formElement.serializeValue()).isEqualTo("");
  }

  @Test
  public void testRadioButtonInvalid() {
    RadioButtonGroup formElement = getTestFormElement("RadioButtonsEmptyValidator");
    //An Exception is expected here
    formElement.setValue("invalid");
    assertThatThrownBy(formElement::getValidationResult).isInstanceOf(InvalidGroupElementException.class);
  }

  @Test
  public void testCheckBoxes() {
    CheckBoxesGroup formElement = getTestFormElement("CheckBoxesMandatory");

    assertThat(formElement).isInstanceOf(CheckBoxesGroup.class);
    formElement.setValue(Arrays.asList("value_123", "value_456"));
    assertThat(formElement.getValidationResult()).isEmpty();
    assertThat(formElement.serializeValue()).isEqualTo("[display_123, display_456]");

    formElement.setValue(Collections.emptyList());
    assertThat(formElement.getValidationResult()).hasSize(1);
    assertThat(formElement.getOptions()).isNotNull();
    assertThat(formElement.getOptions().get(1).isSelectedByDefault()).isTrue();

  }

  @Test
  public void testCheckBoxesInvalid() {
    CheckBoxesGroup formElement = getTestFormElement("CheckBoxesEmptyValidator");
    //An Exception is expected here
    formElement.setValue(Arrays.asList("invalid", "value_123"));
    assertThatThrownBy(formElement::getValidationResult).isInstanceOf(InvalidGroupElementException.class);
  }

  @Test
  public void testSelectBoxes() {
    SelectBox formElement = getTestFormElement("SelectBoxMandatory");

    assertThat(formElement).isInstanceOf(SelectBox.class);
    formElement.setValue("value_123");
    assertThat(formElement.getValidationResult()).isEmpty();
    assertThat(formElement.serializeValue()).isEqualTo("display_123");

    formElement.setValue(null);
    assertThat(formElement.getValidationResult()).hasSize(1);

  }

  @Test
  public void testSelectBoxesInvalid() {
    SelectBox formElement = getTestFormElement("SelectBoxEmptyValidator");
    //An Exception is expected here
    formElement.setValue("invalid");
    assertThatThrownBy(formElement::getValidationResult).isInstanceOf(InvalidGroupElementException.class);
  }

  @Test
  public void testSelectBoxesOnlyDisplayName() {
    SelectBox formElement = getTestFormElement("SelectBoxOnlyDisplayName");

    assertThat(formElement).isInstanceOf(SelectBox.class);
    formElement.setValue("display_123");
    assertThat(formElement.getValidationResult()).isEmpty();
    assertThat(formElement.serializeValue()).isEqualTo("display_123");

    formElement.setValue(null);
    assertThat(formElement.getValidationResult()).hasSize(1);

  }


  @Test
  public void testTextArea() {
    TextArea formElement = getTestFormElement("TextArea");

    assertThat(formElement).isInstanceOf(TextArea.class);
    formElement.setValue("123");
    assertThat(formElement.getValidationResult()).isEmpty();
    formElement.setValue(null);
    assertThat(formElement.getValidationResult()).hasSize(1);
    formElement.setValue("12345678900");
    assertThat(formElement.getValidationResult()).hasSize(1);

    assertThat(formElement.getColumns()).isEqualTo(4);
    assertThat(formElement.getRows()).isEqualTo(5);
  }

  @Test
  public void testTextOnly() {
    TextOnly formElement = getTestFormElement("TextOnly");

    assertThat(formElement).isInstanceOf(TextOnly.class);
    assertThat(formElement.getHint()).isEqualTo("Das ist ein langer Text zur Erklärung des Formulars");
    assertThat(formElement.getName()).isEqualTo("TextOnly");
  }

  @Test
  public void testConsentFormCheckBox() {
    ConsentFormCheckBox formElement = getTestFormElement(CONSENT_FORM_CHECK_BOX_TYPE);

    assertThat(formElement).isInstanceOf(ConsentFormCheckBox.class);
    assertThat(formElement.getHint()).isEqualTo("Please confirm the %data protection consent form%");
    assertThat(formElement.getLinkTarget()).isNotNull();
    assertThat(IdHelper.parseContentId(formElement.getLinkTarget().getId())).isEqualTo(8);
  }

  @Test
  public void testZipField() {
    ZipField formElement = getTestFormElement("ZipFieldTest");

    assertThat(formElement.getHint()).isEqualTo("Bitte Ihre Postleitzahl eingeben");
    assertThat(formElement.getValidator().getRegexp().toString()).isEqualTo("\\d{5}");

    //the field is mandatory -> an error without a value
    assertThat(formElement.getValidationResult()).hasSize(1);

    formElement.setValue("22945");
    assertThat(formElement.getValidationResult()).isEmpty();
  }

  @Test
  public void testPhoneField() {
    PhoneField formElement = getTestFormElement("PhoneFieldTest");
    assertThat(formElement.getName()).isEqualTo("Phone");
  }

  @Test
  public void testFaxField() {
    FaxField formElement = getTestFormElement("FaxFieldTest");
    assertThat(formElement.getName()).isEqualTo("Fax");
  }

  @Test
  public void testStreetNumberField() {
    StreetNumberField formElement = getTestFormElement("StreetFieldTest");
    assertThat(formElement.getName()).isEqualTo("Street and number");
  }

  @Test
  public void testDateField_min() {
    DateField formElement = getTestFormElement("DateFieldMin");
    assertThat(formElement.getName()).isEqualTo("DateField min");

    assertThat(formElement).isInstanceOf(DateField.class);
    assertThat(formElement.getValidationResult()).isNotEmpty();

    formElement.setValue("abc");
    assertThat(formElement.getValidationResult()).hasSize(1);

    formElement.setValue(ZonedDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(1);

    formElement.setValue(ZonedDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(0);
  }

  @Test
  public void testDateField_max() {
    DateField formElement = getTestFormElement("DateFieldMax");
    assertThat(formElement.getName()).isEqualTo("DateField max");

    assertThat(formElement).isInstanceOf(DateField.class);
    assertThat(formElement.getValidationResult()).isNotEmpty();

    formElement.setValue("abc");
    assertThat(formElement.getValidationResult()).hasSize(1);

    //using ZondeDateTime here, frontend is sending including time info
    // max date + 3
    formElement.setValue(ZonedDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(1);

    // max date - 3
    formElement.setValue(ZonedDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(0);

    // same date as max date
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(0);

    //same check for different date format
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(formElement.getValidationResult()).hasSize(0);

    //same check for different date format
    formElement.setValue(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    assertThat(formElement.getValidationResult()).hasSize(0);

  }
}
