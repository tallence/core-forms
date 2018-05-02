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

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark a field of a form element as being set automatically from configuration or a type as being automatically
 * configurable.
 */
@Retention(RUNTIME)
@Target({FIELD, TYPE})
@Documented
@Inherited
public @interface Configured {
  /**
   * The key under which the field's value is stored in the configuration structure.
   *
   * Defaults to the name of the field. Is ignored when used on a type.
   */
  String key() default "";

  /**
   * The configurer to be used to configure a field.
   *
   * The default value means that a {@link Configurer} for the field type is selected automatically. Is ignored when
   * used on a type.
   */
  Class configurer() default Configured.class;
}
