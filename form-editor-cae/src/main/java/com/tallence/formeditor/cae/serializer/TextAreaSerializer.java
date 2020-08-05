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

import java.io.IOException;
import java.util.function.BiFunction;


/**
 * Serializer for the {@link TextArea}
 */
class TextAreaSerializer extends AbstractFormElementSerializer<TextArea> {

  TextAreaSerializer(BiFunction<String, Object[], String> messageResolver, ValidationSerializationHelper validationSerializationHelper) {
    super(TextArea.class, messageResolver, validationSerializationHelper);
  }

  @Override
  public void serializeTypeSpecificFields(TextArea field, JsonGenerator gen) throws IOException {
    if (field.getRows() != null) {
      gen.writeNumberField("rows", field.getRows());
    }
    if (field.getColumns() != null) {
      gen.writeNumberField("columns", field.getColumns());
    }
  }

}
