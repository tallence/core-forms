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

package com.tallence.formeditor.elements;

import com.coremedia.cap.content.Content;

/**
 * Model bean for a configured FaxField.
 * It extends the {@link TextField} with all possible configuration values -> no extra Parser and Validator and no
 * extra Template is required for this field.
 */
public class FaxField extends TextField {

  public FaxField(Content formEditor) {
    super(formEditor);
  }
}
