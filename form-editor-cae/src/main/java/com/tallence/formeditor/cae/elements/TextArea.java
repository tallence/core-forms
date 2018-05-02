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

package com.tallence.formeditor.cae.elements;

import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.annotations.FormElementDefinition;
import com.tallence.formeditor.cae.validator.TextValidator;

/**
 * Model bean for a configured TextArea.
 */
@FormElementDefinition
public class TextArea extends AbstractFormElement<String, TextValidator> {

  @Configured
  private Integer columns;

  @Configured
  private Integer rows;

  public TextArea() {
    super(String.class);
  }

  public Integer getColumns() {
    return this.columns;
  }

  public void setColumns(Integer columns) {
    this.columns = columns;
  }

  public Integer getRows() {
    return this.rows;
  }

  public void setRows(Integer rows) {
    this.rows = rows;
  }
}
