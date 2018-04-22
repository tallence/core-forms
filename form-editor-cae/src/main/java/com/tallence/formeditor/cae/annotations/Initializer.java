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

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker for an initializer class.
 *
 * Initializer classes are run on elements created by a {@link com.tallence.formeditor.cae.parser.GenericParser} after
 * the automatic configurations have been run on the element. Initializers for a class are run in arbitrary order.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
@Documented
@Component
public @interface Initializer {
  /**
   * The class that this initializer can be run for.
   */
  Class value();
}
