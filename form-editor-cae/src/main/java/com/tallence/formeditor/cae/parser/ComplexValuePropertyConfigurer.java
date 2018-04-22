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

package com.tallence.formeditor.cae.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.annotations.Configurer;
import com.tallence.formeditor.cae.elements.ComplexValue;
import org.springframework.beans.BeanWrapper;

import java.util.List;
import java.util.stream.Collectors;

@Configurer(List.class)
public class ComplexValuePropertyConfigurer implements ElementPropertyConfigurer {
  @Override
  public void configure(Struct configurationSource, String key, BeanWrapper wrapper, String propertyName) {
    if (configurationSource.get(key) != null) {
      final List<ComplexValue> complexValues = configurationSource.getStruct(key).getProperties().entrySet().stream()
          .filter(e -> e.getValue() instanceof Struct)
          .map(e -> new ComplexValue(e.getKey(), (Struct) e.getValue()))
          .collect(Collectors.toList());
      wrapper.setPropertyValue(propertyName, complexValues);
    }
  }
}
