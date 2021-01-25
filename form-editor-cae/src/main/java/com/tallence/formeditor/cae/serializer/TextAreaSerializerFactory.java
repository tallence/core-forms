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
import com.tallence.formeditor.cae.elements.TextArea;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiFunction;

import static com.tallence.formeditor.cae.serializer.FormElementSerializerConstants.FORM_SERIALIZER_FIELDS_COLUMNS;
import static com.tallence.formeditor.cae.serializer.FormElementSerializerConstants.FORM_SERIALIZER_FIELDS_ROWS;

/**
 * A factory, creating a {@link TextAreaSerializer}
 */
@Component
public class TextAreaSerializerFactory implements FormElementSerializerFactory<TextAreaSerializerFactory.TextAreaSerializer> {

  @Override
  public TextAreaSerializer createInstance(BiFunction<String, Object[], String> messageResolver,
                                           HttpServletRequest request, HttpServletResponse response) {
    return new TextAreaSerializer(messageResolver);
  }

  /**
   * Serializer for the {@link TextArea}
   */
  public static class TextAreaSerializer extends FormElementSerializerBase<TextArea> {

    public TextAreaSerializer(BiFunction<String, Object[], String> messageResolver) {
      super(TextArea.class, messageResolver);
    }

    @Override
    public void serializeTypeSpecificFields(TextArea field, JsonGenerator gen) throws IOException {
      if (field.getRows() != null) {
        gen.writeNumberField(FORM_SERIALIZER_FIELDS_ROWS, field.getRows());
      }
      if (field.getColumns() != null) {
        gen.writeNumberField(FORM_SERIALIZER_FIELDS_COLUMNS, field.getColumns());
      }
    }

  }
}
