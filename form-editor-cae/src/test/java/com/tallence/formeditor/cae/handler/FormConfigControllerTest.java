package com.tallence.formeditor.cae.handler;

import com.tallence.formeditor.cae.FormTestConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.tallence.formeditor.cae.handler.FormConfigController.FORM_EDITOR_CONFIG_URL;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FormTestConfiguration.class)
@WebAppConfiguration
public class FormConfigControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @After
  public void tearDown() {
  }

  @Test
  @Ignore
  public void testConfigJson() throws Exception {
    URI CONFIG_URL = UriComponentsBuilder.fromUriString(FORM_EDITOR_CONFIG_URL).buildAndExpand("8", "2").toUri();
    String expectedConfig = IOUtils.toString(getClass().getResourceAsStream("/com/tallence/formeditor/cae/testdata/expectedConfig-6-2.json"), StandardCharsets.UTF_8);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.get(CONFIG_URL))
            .andExpect(status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    //this test does not care about the minDate/maxDate validators for DateField since they are dynamic; but they should be in the response
    //TODO check for formatted dateString
    assertTrue(responseBody.contains("\"minDate\" : \"20"));
    assertTrue(responseBody.contains("\"minDate\" : \"mockedValue, arg1: DateField min, arg2:"));
    assertTrue(responseBody.contains("\"maxDate\" : \"20"));
    assertTrue(responseBody.contains("\"maxDate\" : \"mockedValue, arg1: DateField max, arg2:"));

    JSONAssert.assertEquals(expectedConfig, responseBody, JSONCompareMode.LENIENT);
  }

  @Test
  @Ignore
  public void testEmptyConfigJson() throws Exception {
    URI CONFIG_URL = UriComponentsBuilder.fromUriString(FORM_EDITOR_CONFIG_URL).buildAndExpand("8", "6").toUri();

    mvc.perform(MockMvcRequestBuilders.get(CONFIG_URL))
            .andExpect(status().is(406))
            .andReturn();

  }

}
