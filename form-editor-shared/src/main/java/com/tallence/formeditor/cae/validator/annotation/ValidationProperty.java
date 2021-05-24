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

package com.tallence.formeditor.cae.validator.annotation;

import com.tallence.formeditor.cae.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Annotation used for field properties of classes implementing {@link Validator}.
 *
 *  Links a key in the resource bundles to a field property of a validator.
 *  The resulting validator message is directly linked to the annotated field property of the Validator class by using the field name.
 *
 *  This is used for the serialization of FormEditor inside FormConfigController.
 *  It will only be added if the annotated field has a value set.
 *  The validation message will be serialized as { "fieldName": "resolved message"} as part of the resulting JSON.
 *
 */
//TODO removed links in javadoc
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidationProperty {

  /**
   * the key inside the resource bundles to use for message resolving
   */
  String messageKey();

  /**
   * (optional) the key of the message inside the JSON representation.
   * by default it will use the name of the annotated field property.
   */
  String name() default "";
}

