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
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tallence.formeditor.cae.elements.AbstractFormElement;
import com.tallence.formeditor.cae.elements.AdvancedSettings;
import com.tallence.formeditor.cae.elements.ComplexValue;
import com.tallence.formeditor.cae.elements.FieldWithOptions;
import com.tallence.formeditor.cae.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.BiFunction;

import static com.tallence.formeditor.cae.FormElementFactory.FORM_DATA_KEY_TYPE;
import static com.tallence.formeditor.cae.parser.AbstractFormElementParser.*;

/**
 * Serializes default fields used for nearly all form elements.
 */
class AbstractFormElementSerializer<T extends AbstractFormElement<?, ?>> extends StdSerializer<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractFormElementSerializer.class);

  private final BiFunction<String, Object[], String> messageResolver;
  private final ValidationSerializationHelper validationSerializationHelper;

  AbstractFormElementSerializer(Class<T> type,
                                BiFunction<String, Object[], String> messageResolver,
                                ValidationSerializationHelper validationSerializationHelper) {
    super(type);
    this.validationSerializationHelper = validationSerializationHelper;
    this.messageResolver = messageResolver;
  }

  @Override
  public void serialize(T field, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    serializeDefaultFields(field, gen);
    serializeOptionFields(field, gen);
    serializeTypeSpecificFields(field, gen);
    serializeValidationFields(field, gen);
    gen.writeEndObject();
  }

  public void serializeDefaultFields(T field, JsonGenerator gen) throws IOException {
    gen.writeStringField("id", field.getId());
    gen.writeStringField("technicalName", field.getTechnicalName());
    gen.writeStringField(FORM_DATA_NAME, field.getName());
    if (StringUtils.isNotBlank(field.getHint())) {
      gen.writeStringField(FORM_DATA_HINT, field.getHint());
    }
    if (StringUtils.isNotBlank(field.getPlaceholder())) {
      gen.writeStringField(FORM_DATA_PLACEHOLDER, field.getPlaceholder());
    }
    gen.writeStringField(FORM_DATA_KEY_TYPE, field.getClass().getSimpleName());

    writeAdvancedSettings(field, gen);
  }

  protected void serializeOptionFields(T field, JsonGenerator gen) throws IOException {
    if (field instanceof FieldWithOptions) {
      gen.writeArrayFieldStart("options");
      for (ComplexValue option : ((FieldWithOptions) field).getOptions()) {
        gen.writeStartObject();
        gen.writeStringField("name", option.getDisplayName());
        gen.writeStringField("id", option.getValue());
        if (option.isSelectedByDefault()) {
          gen.writeBooleanField("selectedByDefault", true);
        }
        gen.writeEndObject();
      }
      gen.writeEndArray();
    }
  }

  public void serializeTypeSpecificFields(T field, JsonGenerator gen) throws IOException {
    //implement this method when needed
  }

  public void writeAdvancedSettings(T field, JsonGenerator gen) throws IOException {
    AdvancedSettings settings = field.getAdvancedSettings();
    if (settings != null) {
      gen.writeObjectFieldStart(FORM_DATA_ADVANCED_SETTINGS);
      boolean visibilityDependent = settings.isVisibilityDependent();
      if (visibilityDependent) {
        gen.writeObjectFieldStart(FORM_DATA_VISIBILIY);
        gen.writeBooleanField(FORM_DATA_VISIBILITY_ACTIVATED, true);
        gen.writeStringField(FORM_DATA_VISIBILITY_ELEMENT_ID, settings.getDependentElementId());
        gen.writeStringField(FORM_DATA_VISIBILITY_ELEMENT_VALUE, settings.getDependentElementValue());
        gen.writeEndObject();
      }
      if (settings.getColumnWidth() != null) {
        gen.writeNumberField(FORM_DATA_CUSTOM_WIDTH, settings.getColumnWidth());
      }
      gen.writeBooleanField(FORM_DATA_BREAK_AFTER_ELEMENT, settings.isBreakAfterElement());
      gen.writeEndObject();
    }
  }

  private void serializeValidationFields(T element, JsonGenerator gen) throws IOException {
    if (element.getValidator() == null) {
      return;
    }

    Validator<?> validator = element.getValidator();
    gen.writeObjectFieldStart(FORM_DATA_VALIDATOR);

    /* validation values block */
    Map<String, Object> validationValues = validationSerializationHelper.getValidationValuesForConfig(validator);
    validationValues.forEach(ThrowingBiConsumer.unchecked((validatorName, validatorValue) -> {
      if (validatorValue instanceof Integer) {
        gen.writeNumberField(validatorName, (Integer) validatorValue);
      } else if (validatorValue instanceof Boolean) {
        gen.writeBooleanField(validatorName, (Boolean) validatorValue);
      } else if (validatorValue instanceof LocalDate) {
        //ISO 8601 WITHOUT any time information; this should be placed in a custom formatter
        gen.writeStringField(validatorName, ((LocalDate) validatorValue).format(DateTimeFormatter.ISO_LOCAL_DATE) + "T00:00:00.000Z");
      } else {
        gen.writeStringField(validatorName, String.valueOf(validatorValue));
      }
    }));

    /* message block */
    gen.writeObjectFieldStart(FORM_DATA_VALIDATOR_MESSAGES);
    Map<String, String> validationMessages = validationSerializationHelper.getValidationMessages(element.getName(), validator, messageResolver);
    validationMessages.forEach(ThrowingBiConsumer.unchecked(gen::writeStringField));
    gen.writeEndObject();

    gen.writeEndObject();
  }

}
