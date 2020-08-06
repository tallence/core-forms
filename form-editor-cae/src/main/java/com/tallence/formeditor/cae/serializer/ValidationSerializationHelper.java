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

import com.tallence.formeditor.cae.validator.ValidationFieldError;
import com.tallence.formeditor.cae.validator.Validator;
import com.tallence.formeditor.cae.validator.annotation.ValidationMessage;
import com.tallence.formeditor.cae.validator.annotation.ValidationProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A Helper class to extract the validation configs of form fields.
 */
public class ValidationSerializationHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ValidationSerializationHelper.class);

  /**
   * this methods will return a map with the name of the validation rule and the raw value to use for the validation.
   * the value can be set in Studio, the method is used for the JSON config of a form.
   *
   * e.g. {
   *  min: 5,
   *  max: 15,
   *  mandatory: true
   * }
   *
   * @param validator the validator
   *
   * @return map with field names and values used for the validation
   */
  public static Map<String, Object> getValidationValuesForConfig(Validator<?> validator) {
    Map<String, Object> values = new HashMap<>();

    List<Field> fields = getFieldsWithValues(validator);
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        ValidationProperty annotation = field.getAnnotation(ValidationProperty.class);
        String name = !StringUtils.isBlank(annotation.name()) ? annotation.name() : field.getName();
        Object value = field.get(validator);
        values.put(name, value);
      } catch (Exception x) {
        LOG.warn("cannot process ValidationProperty annotation of field {} for validator {}", field.getName(), validator.getClass());
      }
    }
    return values;
  }

  /**
   * this method will provide the correct the correct translated validation messages for each validation rule of a given field.
   *
   *
   * e.g. {
   *    min: "The minimum value for field _FIELDNAME_ is 5",
   *    max: "The maximum value for field _FIELDNAME_ is 15",
   *    mandatory: "Field _FIELDNAME_ is required"
   * }
   *
   * @param fieldName output name of the field
   * @param validator validator
   * @param messageResolver resource bundles for the current locale
   *
   * @return map with field names and translated validation messages
   */
  public static Map<String, String> getValidationMessages(String fieldName, Validator<?> validator, BiFunction<String, Object[], String> messageResolver) {
    Map<String, String> messages = new HashMap<>();

    //class annotation
    //this is required for global messages for a field, e.g. type is not valid
    ValidationMessage globalMessage = validator.getClass().getAnnotation(ValidationMessage.class);
    if (globalMessage != null) {
      messages.put(globalMessage.name(), getMessage(globalMessage.messageKey(), new Object[]{fieldName}, messageResolver));
    }

    //field annotations
    List<Field> fields = getFieldsWithValues(validator);
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        ValidationProperty annotation = field.getAnnotation(ValidationProperty.class);
        String propertyName = !StringUtils.isBlank(annotation.name()) ? annotation.name() : field.getName();
        String messageKey = annotation.messageKey();
        Object value = field.get(validator);

        //TODO add possibility to provide custom formatters for the values
        Object[] args = {fieldName, value};

        messages.put(propertyName, getMessage(messageKey, args, messageResolver));
      } catch (Exception x) {
        LOG.warn("cannot process ValidationProperty annotation of field {} for validator {}", field.getName(), validator.getClass());
      }
    }
    return messages;
  }

  public static String getValidationMessage(String fieldName, ValidationFieldError fieldError, BiFunction<String, Object[], String> messageResolver) {
    return getMessage(fieldError.getMessageKey(), ArrayUtils.addAll(new Object[]{fieldName}, fieldError.getMessageParams()), messageResolver);
  }

  private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));
    if (type.getSuperclass() != null) {
      getAllFields(fields, type.getSuperclass());
    }
    return fields;
  }

  private static List<Field> getFieldsWithValues(Validator<?> validator) {
    return getAllFields(new LinkedList<Field>(), validator.getClass()).stream()
            .filter(f -> f.isAnnotationPresent(ValidationProperty.class))
            .filter(f -> {
              f.setAccessible(true);
              try {
                return f.get(validator) != null;
              } catch (Exception x) {
                return false;
              }
            }).collect(Collectors.toList());
  }

  private static String getMessage(String key, Object[] args, BiFunction<String, Object[], String> messageResolver) {
    String message = key;
    try {
      message = messageResolver.apply(key, args);
    } catch (Exception x) {
      LOG.warn("cannot find key {} in resource bundles", key);
    }
    return message;
  }

}
