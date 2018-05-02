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
 * Marks a configurer for a certain value.
 *
 * This annotation has no effect (other than marking a class as a {@link Component}) on types other than
 * {@link com.tallence.formeditor.cae.parser.ElementPropertyConfigurer}.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Component
public @interface Configurer {
  /**
   * The type of the property that the annotated configurer can configure.
   */
  Class value();
}
