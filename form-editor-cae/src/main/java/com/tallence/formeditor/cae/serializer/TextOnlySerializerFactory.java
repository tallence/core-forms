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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tallence.formeditor.elements.TextOnly;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiFunction;

import static com.tallence.formeditor.FormElementFactory.FORM_DATA_KEY_TYPE;
import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_DATA_NAME;
import static com.tallence.formeditor.cae.serializer.FormElementSerializerConstants.FORM_SERIALIZER_FIELDS_ID;
import static com.tallence.formeditor.cae.serializer.FormElementSerializerConstants.FORM_SERIALIZER_FIELDS_TECHNICAL_NAME;

/**
 * A factory, creating a {@link TextOnlySerializer}
 */
@Component
public class TextOnlySerializerFactory implements FormElementSerializerFactory<TextOnlySerializerFactory.TextOnlySerializer> {


  @Override
  public TextOnlySerializer createInstance(BiFunction<String, Object[], String> messageResolver,
                                           HttpServletRequest request, HttpServletResponse response) {
    return new TextOnlySerializer(messageResolver);
  }

  /**
   * Using a custom serializer, because the hint needs to be written in the name-property
   */
  public static class TextOnlySerializer extends FormElementSerializerBase<TextOnly> {

    public TextOnlySerializer(BiFunction<String, Object[], String> messageResolver) {
      super(TextOnly.class, messageResolver);
    }

    @Override
    public void serialize(TextOnly field, JsonGenerator gen, SerializerProvider provider) throws IOException {

      gen.writeStartObject();
      gen.writeStringField(FORM_SERIALIZER_FIELDS_ID, field.getId());
      gen.writeStringField(FORM_SERIALIZER_FIELDS_TECHNICAL_NAME, field.getTechnicalName());
      //The hint contains the content, but it is not to be used as a toolTip in the frontend -> used in the field name
      gen.writeStringField(FORM_DATA_NAME, field.getHint());
      gen.writeStringField(FORM_DATA_KEY_TYPE, field.getClass().getSimpleName());

      writeAdvancedSettings(field, gen);

      gen.writeEndObject();
    }
  }
}
