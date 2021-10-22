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
import com.tallence.formeditor.elements.AbstractFormElement;
import com.tallence.formeditor.elements.AdvancedSettings;
import com.tallence.formeditor.elements.ComplexValue;
import com.tallence.formeditor.elements.FieldWithOptions;
import com.tallence.formeditor.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.BiFunction;

import static com.tallence.formeditor.FormElementFactory.FORM_DATA_KEY_TYPE;
import static com.tallence.formeditor.parser.AbstractFormElementParser.*;
import static com.tallence.formeditor.cae.serializer.FormElementSerializerConstants.*;
import static com.tallence.formeditor.parser.AbstractFormElementParser.FORM_DATA_VISIBILITY_ELEMENT_VALUES;

/**
 * Serializes default fields used for nearly all form elements.
 */
public class FormElementSerializerBase<T extends AbstractFormElement<?, ?>> extends StdSerializer<T> {

  private static final Logger LOG = LoggerFactory.getLogger(FormElementSerializerBase.class);

  //Context specific components, which will be set
  private final BiFunction<String, Object[], String> messageResolver;

  public FormElementSerializerBase(Class<T> type,
                                   BiFunction<String, Object[], String> messageResolver) {
    super(type);
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
    gen.writeStringField(FORM_SERIALIZER_FIELDS_ID, field.getId());
    gen.writeStringField(FORM_SERIALIZER_FIELDS_TECHNICAL_NAME, field.getTechnicalName());
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
      gen.writeArrayFieldStart(FORM_SERIALIZER_FIELDS_OPTIONS);
      for (ComplexValue option : ((FieldWithOptions<?>) field).getOptions()) {
        gen.writeStartObject();
        gen.writeStringField(FORM_SERIALIZER_FIELDS_NAME, option.getDisplayName());
        gen.writeStringField(FORM_SERIALIZER_FIELDS_ID, option.getValue());
        if (option.isSelectedByDefault()) {
          gen.writeBooleanField(FORM_SERIALIZER_FIELDS_SELECTED_BY_DEFAULT, true);
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
        //The value field is deprecated, will be removed
        gen.writeStringField(FORM_DATA_VISIBILITY_ELEMENT_VALUE, settings.getDependentElementValue());
        gen.writeArrayFieldStart(FORM_DATA_VISIBILITY_ELEMENT_VALUES);
        for (String value : settings.getDependentElementValues()) {
          gen.writeString(value);
        }
        gen.writeEndArray();
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

    //Look up the properties of the validator, which are relevant for serialization
    Map<String, Object> validationValues = ValidationSerializationHelper.getValidationValuesForConfig(validator);
    for (Map.Entry<String, Object> entry : validationValues.entrySet()) {
      final Object validatorValue = entry.getValue();
      final String validatorName = entry.getKey();
      if (validatorValue instanceof Integer) {
        gen.writeNumberField(validatorName, (Integer) validatorValue);
      } else if (validatorValue instanceof Boolean) {
        gen.writeBooleanField(validatorName, (Boolean) validatorValue);
      } else if (validatorValue instanceof LocalDate) {
        //Transform to an ISO 8601 zonedDateTime. Using the timeZone UTC as a default, the frontend will strip off the time anyway.
        final OffsetDateTime offsetDateTime = OffsetDateTime.of(((LocalDate) validatorValue).atTime(0, 0), ZoneOffset.UTC);
        gen.writeStringField(validatorName, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime));
      } else {
        gen.writeStringField(validatorName, String.valueOf(validatorValue));
      }
    }

    //Lookup up the global messages of a validator
    gen.writeObjectFieldStart(FORM_DATA_VALIDATOR_MESSAGES);
    Map<String, String> validationMessages = ValidationSerializationHelper.getValidationMessages(element.getName(), validator, messageResolver);
    for (Map.Entry<String, String> entry : validationMessages.entrySet()) {
      gen.writeStringField(entry.getKey(), entry.getValue());
    }
    gen.writeEndObject();

    gen.writeEndObject();
  }
}
