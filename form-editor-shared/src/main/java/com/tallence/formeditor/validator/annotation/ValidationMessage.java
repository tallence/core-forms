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

package com.tallence.formeditor.validator.annotation;

import com.tallence.formeditor.validator.UsersMailValidator;
import com.tallence.formeditor.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for classes implementing {@link Validator}.
 *
 * Links a key in the resource bundles to a validator class.
 * The main difference to {@link ValidationProperty} is that the message does not depend on a field value, it will always be added to the serialized JSON.
 *
 * This is used for the serialization of FormElements for Frontend Apps.
 * The validation message will be serialized as { "name": "resolved message"} as part of the resulting JSON.
 *
 * Example: {@link UsersMailValidator}
 * The frontend requires 2 validation messages: correct email format and mandatory.
 * mandatory can be linked to property inside the validator class, email format cannot.
 * We still have to provide such a message for the client side validation, this can be achieved by using this Annotation on a class.
 *
 * See also {@link ValidationProperty}
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ValidationMessage {

  /**
   * the key inside the resource bundles to use for message resolving
   */
  String messageKey();

  /**
   * the key of the message inside the JSON representation.
   */
  String name();
}
