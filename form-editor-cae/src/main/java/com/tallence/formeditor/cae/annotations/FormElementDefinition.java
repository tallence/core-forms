/*
 * Copyright 2018 Tallence AG
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

package com.tallence.formeditor.cae.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark a class as form element.
 *
 * Applying this annotation to a subclass of {@link com.tallence.formeditor.cae.elements.AbstractFormElement} marks
 * the class as the definition of a form element.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
@Component
public @interface FormElementDefinition {

  /**
   * The type key used in the configuration of a form element.
   *
   * This is the key by which the {@link com.tallence.formeditor.cae.FormElementFactory} decides, which element type
   * it needs to create. It defaults to the name of the form element class.
   */
  String value() default "";
}
