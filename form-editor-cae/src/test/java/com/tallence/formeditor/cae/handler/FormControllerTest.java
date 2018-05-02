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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.tallence.formeditor.cae.handler.FormController.PROCESS_SOCIAL_FORM;
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
  private static final URI TEST_URL = UriComponentsBuilder.fromUriString(PROCESS_SOCIAL_FORM).buildAndExpand("6", "2").toUri();
  private static final String FORM_DATA_SERIALIZED = "TestName: 12345<br/>TestName: 18<br/>TestName: 12345<br/>TestName: null<br/>TestName: null<br/>TestName: [12345, ]<br/>TestName: []<br/>TestName: 12345<br/>TestName: null<br/>TestName: ist Text<br/>TestName: Das ist ein langer Text zur Erklärung des Formulars<br/>TestName: test@test.de<br/>";

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
        .param("TextField", "12345")
        .param("NumberField", "18")
        .param("RadioButtonsMandatory", "12345")
        .param("CheckBoxesMandatory", "12345")
        .param("SelectBoxMandatory", "12345")
        .param("TextArea", "ist Text")
        .param("UsersMail", "test@test.de")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(storageAdapterMock.formData, FORM_DATA_SERIALIZED);
    assertEquals(mailAdapterMock.usersFormData, FORM_DATA_SERIALIZED);
    assertEquals(mailAdapterMock.usersRecipient, "test@test.de");

  }

  @Test
  public void testValidPostWithFile() throws Exception {

    MockMultipartFile firstFile = new MockMultipartFile("FileUpload", "filename.txt", "text/plain", "some xml".getBytes());

    mvc.perform(fileUpload(TEST_URL)
        .file(firstFile)
        .param("TextField", "12345")
        .param("NumberField", "18")
        .param("RadioButtonsMandatory", "12345")
        .param("CheckBoxesMandatory", "12345")
        .param("SelectBoxMandatory", "12345")
        .param("TextArea", "ist Text")
        .param("UsersMail", "test@test.de")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    assertEquals(storageAdapterMock.formData, FORM_DATA_SERIALIZED + "FileUpload: filename.txt<br/>");
    assertEquals(mailAdapterMock.usersFormData, FORM_DATA_SERIALIZED + "FileUpload: filename.txt<br/>");
    assertEquals(mailAdapterMock.usersRecipient, "test@test.de");
  }

  @Test
  public void testValidPostWithMailAction() throws Exception {

    URI mailTestUrl = UriComponentsBuilder.fromUriString(PROCESS_SOCIAL_FORM).buildAndExpand("6", "4").toUri();

    mvc.perform(post(mailTestUrl)
        .param("TextArea", "ist Text")
        .param("UsersMail", "test@test.de")
    )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string("{\"success\":true,\"error\":null}"))
        .andDo(MockMvcResultHandlers.print());

    //check if the storageAdapter was not called
    assertNull(storageAdapterMock.formData);
    assertEquals(mailAdapterMock.adminFormData, "TestName: ist Text<br/>TestName: test@test.de<br/>");
    assertEquals(mailAdapterMock.adminRecipient, "admin@test.de");
    assertEquals(mailAdapterMock.usersFormData, "TestName: ist Text<br/>TestName: test@test.de<br/>");
    assertEquals(mailAdapterMock.usersRecipient, "test@test.de");
  }


  @Test
  public void testInValidPost() throws Exception {

    //Performing a post with only the TextField given will cause a validation error, because other mandatory fields are missing.
    mvc.perform(fileUpload(TEST_URL).param("TextField", "12345"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("{\"success\":false,\"error\":\"server-validation-failed\"}"))
        .andDo(MockMvcResultHandlers.print());

  }

}
