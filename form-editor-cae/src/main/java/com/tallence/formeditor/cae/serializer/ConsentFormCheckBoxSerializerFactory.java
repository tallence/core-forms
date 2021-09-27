/*
 * Copyright 2020 Tallence AG
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

package com.tallence.formeditor.cae.serializer;

import com.coremedia.objectserver.web.links.LinkFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.tallence.formeditor.elements.ConsentFormCheckBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiFunction;

import static com.tallence.formeditor.parser.ConsentFormCheckBoxParser.FORM_LINK_TARGET;

/**
 * A factory, creating a {@link ConsentFormCheckBoxSerializer} with access to the instance-field: {@link #linkFormatter}
 */
@Component
public class ConsentFormCheckBoxSerializerFactory implements FormElementSerializerFactory<ConsentFormCheckBoxSerializerFactory.ConsentFormCheckBoxSerializer> {

  private final LinkFormatter linkFormatter;

  public ConsentFormCheckBoxSerializerFactory(LinkFormatter linkFormatter) {
    this.linkFormatter = linkFormatter;
  }

  @Override
  public ConsentFormCheckBoxSerializer createInstance(BiFunction<String, Object[], String> messageResolver,
                                                      HttpServletRequest request, HttpServletResponse response) {
    return new ConsentFormCheckBoxSerializer(messageResolver, request, response);
  }

  /**
   * Serializer for the {@link ConsentFormCheckBox}, taking care of linkBuilding for the targetUrl.
   */
  public class ConsentFormCheckBoxSerializer extends FormElementSerializerBase<ConsentFormCheckBox> {

    private final Logger LOG = LoggerFactory.getLogger(ConsentFormCheckBoxSerializer.class);

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ConsentFormCheckBoxSerializer(BiFunction<String, Object[], String> messageResolver,
                                         HttpServletRequest request, HttpServletResponse response) {
      super(ConsentFormCheckBox.class, messageResolver);
      this.request = request;
      this.response = response;
    }

    @Override
    public void serializeTypeSpecificFields(ConsentFormCheckBox field, JsonGenerator gen) throws IOException {
      try {
        gen.writeStringField(FORM_LINK_TARGET, linkFormatter.formatLink(field.getLinkTarget(), null, request, response, true));
      } catch (Exception x) {
        gen.writeStringField(FORM_LINK_TARGET, "#");
        LOG.warn("cannot build link for linked content of field element [{}]", field.getTechnicalName());
      }
    }

  }
}
