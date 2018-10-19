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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.tallence.formeditor.cae.handler.FormController.FORM_EDITOR_SUBMIT_URL;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for {@link FormController}
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FormTestConfiguration.class)
public class FormControllerTest {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private StorageAdapterMock storageAdapterMock;
  @Autowired
  private MailAdapterMock mailAdapterMock;

  private MockMvc mvc;
  private static final URI TEST_URL = UriComponentsBuilder.fromUriString(FORM_EDITOR_SUBMIT_URL).buildAndExpand("6", "2").toUri();
  private static final String MAIL_ADDRESS_TEST = "test@example.com";
  private static final String FORM_DATA_SERIALIZED =
      "TestName: 12345<br/>" +
          "Postleitzahl: 22945<br/>" +
          "Phone: <br/>" +
          "Fax: <br/>" +
          "Street and number: <br/>" +
          "Alter: 18<br/>" +
          "Radio: 12345<br/>RadioOptional: <br/>" +
          "RadioEmptyValidator: <br/>" +
          "CheckBoxes: [12345, ]<br/>" +
          "CheckBoxesEmptyValidator: []<br/>" +
          "SelectBox: 12345<br/>" +
          "SelectBoxEmptyValidator: <br/>" +
          "TextArea: ist Text<br/>" +
          "TextOnly: Das ist ein langer Text zur Erklärung des Formulars<br/>" +
          "UsersMail: " + MAIL_ADDRESS_TEST + "<br/>" +
          "Data protection consent form: true<br/>";

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();
  }

  @After
  public void tearDown() {
    mailAdapterMock.clear();
    storageAdapterMock.clear();
  }


  @Test
  public void testValidPost() throws Exception {

    mvc.perform(fileUpload(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "12345")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "12345")
        .param("SelectBox_SelectBoxMandatory", "12345")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(FORM_DATA_SERIALIZED, storageAdapterMock.formData);
    assertEquals(FORM_DATA_SERIALIZED, mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);

  }


  @Test
  public void testValidPostWithJavascript() throws Exception {

    mvc.perform(fileUpload(TEST_URL)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "12345")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "12345")
        .param("SelectBox_SelectBoxMandatory", "12345")
        .param("TextArea_TextArea", "<script>")
        .param("ZipField_ZipFieldTest", "22945")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    assertTrue(storageAdapterMock.formData.contains("TextArea"));
    assertTrue(storageAdapterMock.formData.contains(HtmlUtils.htmlEscape("<script>")));
    assertFalse(storageAdapterMock.formData.contains("<script>"));

  }

  @Test
  public void testValidPostWithFile() throws Exception {

    MockMultipartFile firstFile = new MockMultipartFile("FileUpload_FileUpload", "filename.txt", "text/plain", "some xml".getBytes());

    mvc.perform(fileUpload(TEST_URL)
        .file(firstFile)
        .param("TextField_TextField", "12345")
        .param("NumberField_NumberField", "18")
        .param("RadioButtonGroup_RadioButtonsMandatory", "12345")
        .param("CheckBoxesGroup_CheckBoxesMandatory", "12345")
        .param("SelectBox_SelectBoxMandatory", "12345")
        .param("TextArea_TextArea", "ist Text")
        .param("ZipField_ZipFieldTest", "22945")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
        .param("ConsentFormCheckBox_ConsentFormCheckBox", "on")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(FORM_DATA_SERIALIZED + "FileUpload_FileUpload: filename.txt<br/>", storageAdapterMock.formData);
    assertEquals(FORM_DATA_SERIALIZED + "FileUpload_FileUpload: filename.txt<br/>", mailAdapterMock.usersFormData);
    assertEquals(MAIL_ADDRESS_TEST, mailAdapterMock.usersRecipient);
  }

  @Test
  public void testValidPostWithMailAction() throws Exception {

    URI mailTestUrl = UriComponentsBuilder.fromUriString(FORM_EDITOR_SUBMIT_URL).buildAndExpand("6", "4").toUri();

    mvc.perform(post(mailTestUrl)
        .param("TextArea_TextArea", "ist Text")
        .param("UsersMail_UsersMail", MAIL_ADDRESS_TEST)
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
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
    mvc.perform(fileUpload(TEST_URL).param("TextField_TextField", "12345"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("{\"success\":false,\"error\":\"server-validation-failed\"}"))
        .andDo(MockMvcResultHandlers.print());

  }

}
