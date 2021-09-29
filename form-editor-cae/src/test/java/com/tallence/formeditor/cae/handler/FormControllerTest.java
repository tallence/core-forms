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
package com.tallence.formeditor.cae.handler;

import com.tallence.formeditor.cae.FormTestConfiguration;
import com.tallence.formeditor.cae.mocks.MailAdapterMock;
import com.tallence.formeditor.cae.mocks.StorageAdapterMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

import static com.tallence.formeditor.cae.handler.FormController.FORM_EDITOR_SUBMIT_URL;
import static org.junit.Assert.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for {@link FormController}
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FormTestConfiguration.class)
@DirtiesContext(classMode = AFTER_CLASS)
@TestPropertySource(properties = "cae.single-node=true")
public class FormControllerTest {

  private static final String SUCCESS_RESPONSE = "{\"success\":true,\"error\":null,\"errorData\":null,\"successData\":{\"textHeader\":\"mockedValue, arg1: {0}, arg2: {1}\",\"textMessage\":\"mockedValue, arg1: {0}, arg2: {1}\",\"textButton\":\"mockedValue, arg1: {0}, arg2: {1}\"}}";
  private static final String FAILURE_VALIDATION_RESPONSE = "{\"success\":false,\"error\":\"server-validation-failed\",\"errorData\":{\"globalError\":null,\"fieldErrors\":{";

  @Autowired
  private StorageAdapterMock storageAdapterMock;

  @Autowired
  private MailAdapterMock mailAdapterMock;

  @Autowired
  private MockMvc mvc;

  private static final URI TEST_URL = UriComponentsBuilder.fromUriString(FORM_EDITOR_SUBMIT_URL).buildAndExpand("8", "2").toUri();
  private static final String MAIL_ADDRESS_TEST = "test@example.com";
  private static final String FORM_DATA_SERIALIZED =
      "TestName: 12345<br/>" +
          "Postleitzahl: 22945<br/>" +
          "Phone: <br/>" +
          "Fax: <br/>" +
          "Street and number: <br/>" +
          "Alter: 18<br/>" +
          "Radio: display_123<br/>" +
          "RadioOptional: <br/>" +
          "RadioEmptyValidator: <br/>" +
          "CheckBoxes: [display_123]<br/>" +
          "CheckBoxesEmptyValidator: []<br/>" +
          "SelectBox: display_123<br/>" +
          "SelectBox DisplayName: display_123<br/>" +
          "SelectBoxEmptyValidator: <br/>" +
          "TextArea: ist Text<br/>" +
          "DateField min: December 31, 2099<br/>" +
          "DateField max: December 31, 1999<br/>" +
          "TextOnly: Das ist ein langer Text zur Erklärung des Formulars<br/>" +
          "UsersMail: " + MAIL_ADDRESS_TEST + "<br/>" +
          "Data protection consent form: true<br/>";

  @After
  public void tearDown() {
    mailAdapterMock.clear();
    storageAdapterMock.clear();
  }

  @Test
  public void testValidPost() throws Exception {

    mvc.perform(multipart(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "value_123")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "value_123")
        .param("SelectBox_SelectBoxMandatory", "value_123")
        .param("SelectBox_SelectBoxOnlyDisplayName", "display_123")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("DateField_DateFieldMin", "2099-12-31T00:00:00.000Z")
        .param("DateField_DateFieldMax", "1999-12-31T00:00:00.000Z")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(SUCCESS_RESPONSE))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(FORM_DATA_SERIALIZED, storageAdapterMock.formData);
    assertEquals(FORM_DATA_SERIALIZED, mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);

  }


  @Test
  public void testValidPostWithJavascript() throws Exception {

    mvc.perform(multipart(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "value_123")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "value_123")
        .param("SelectBox_SelectBoxMandatory", "value_123")
        .param("SelectBox_SelectBoxOnlyDisplayName", "display_123")
        .param("TextArea_TextArea", "<script>")
        .param("ZipField_ZipFieldTest", "22945")
        .param("DateField_DateFieldMin", "2099-12-31T00:00:00.000Z")
        .param("DateField_DateFieldMax", "1999-12-31T00:00:00.000Z")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
//        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(SUCCESS_RESPONSE))
        .andDo(MockMvcResultHandlers.print());

    assertTrue(storageAdapterMock.formData.contains("TextArea"));
    assertTrue(storageAdapterMock.formData.contains(HtmlUtils.htmlEscape("<script>")));
    assertFalse(storageAdapterMock.formData.contains("<script>"));

  }

  @Test
  public void testValidPostWithFile() throws Exception {

    MockMultipartFile firstFile = new MockMultipartFile("FileUpload_FileUpload", "filename.txt", "text/plain", "some xml".getBytes());

    mvc.perform(multipart(TEST_URL)
        .file(firstFile)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "value_123")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "value_123")
        .param("SelectBox_SelectBoxMandatory", "value_123")
        .param("SelectBox_SelectBoxOnlyDisplayName", "display_123")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("DateField_DateFieldMin", "2099-12-31T00:00:00.000Z")
        .param("DateField_DateFieldMax", "1999-12-31T00:00:00.000Z")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(SUCCESS_RESPONSE))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(FORM_DATA_SERIALIZED + "FileUpload_FileUpload: filename.txt<br/>", storageAdapterMock.formData);
    assertEquals(FORM_DATA_SERIALIZED + "FileUpload_FileUpload: filename.txt<br/>", mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);
  }

  @Test
  public void testValidPostWithMailAction() throws Exception {

    URI mailTestUrl = UriComponentsBuilder.fromUriString(FORM_EDITOR_SUBMIT_URL).buildAndExpand("8", "4").toUri();

    mvc.perform(post(mailTestUrl)
        .param("TextArea_TextArea", "ist Text")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(SUCCESS_RESPONSE))
        .andDo(MockMvcResultHandlers.print());

    //check if the storageAdapter was not called
    assertNull(storageAdapterMock.formData);
    assertEquals("TestName: ist Text<br/>TestName: " + MAIL_ADDRESS_TEST + "<br/>", mailAdapterMock.adminFormData);
    assertEquals("admin@example.com", mailAdapterMock.adminRecipient);
    assertEquals("TestName: ist Text<br/>TestName: " + MAIL_ADDRESS_TEST + "<br/>", mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);
  }


  @Test
  public void testInValidPost() throws Exception {

    //Performing a post with only the TextField given will cause a validation error, because other mandatory fields are missing.
    mvc.perform(multipart(TEST_URL).param("TextField_TextField", "12345"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(org.hamcrest.Matchers.containsString(FAILURE_VALIDATION_RESPONSE)))
        .andDo(MockMvcResultHandlers.print());

  }

  @Test
  public void testPostWithDependentFieldNegative() throws Exception {
    mvc.perform(multipart(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "value_123")
        .param("RadioButtonGroup_RadioButtonsOptional", "value_456")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "value_123")
        .param("SelectBox_SelectBoxMandatory", "value_123")
        .param("SelectBox_SelectBoxOnlyDisplayName", "display_123")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("DateField_DateFieldMin", "2099-12-31T00:00:00.000Z")
        .param("DateField_DateFieldMax", "1999-12-31T00:00:00.000Z")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
        .andExpect(content().string(org.hamcrest.Matchers.containsString(FAILURE_VALIDATION_RESPONSE)))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("\"TextField_myComplexCustomId\":[\"mockedValue, arg1: DependentField, arg2: {1}\"]")))
        .andDo(MockMvcResultHandlers.print());

    assertNull(storageAdapterMock.formData);
    assertNull(mailAdapterMock.usersFormData);
    assertNull(mailAdapterMock.usersRecipient);
  }

  @Test
  public void testPostWithDependentFieldPositive() throws Exception {
    mvc.perform(multipart(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "value_123")
        .param("RadioButtonGroup_RadioButtonsOptional", "value_456")
        .param("TextField_myComplexCustomId", "testValue")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "value_123")
        .param("SelectBox_SelectBoxMandatory", "value_123")
        .param("SelectBox_SelectBoxOnlyDisplayName", "display_123")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("DateField_DateFieldMin", "2099-12-31T00:00:00.000Z")
        .param("DateField_DateFieldMax", "1999-12-31T00:00:00.000Z")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(SUCCESS_RESPONSE))
        .andDo(MockMvcResultHandlers.print());

    String withNewField = FORM_DATA_SERIALIZED.replace("RadioOptional: <br/>", "DependentField: testValue<br/>RadioOptional: display_456<br/>");

    assertEquals(withNewField, storageAdapterMock.formData);
    assertEquals(withNewField, mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);
  }

}
