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

package com.tallence.formeditor.studio.fields {
import com.coremedia.cms.editor.sdk.editorContext;
import com.coremedia.ui.data.ValueExpression;

import ext.form.field.TextField;

public class TextFieldBase extends ext.form.field.TextField {

  private var textVE:ValueExpression;

  public function TextFieldBase(config:com.tallence.formeditor.studio.fields.TextField = null) {
    super(config);
  }

  protected function getTextVE(config:com.tallence.formeditor.studio.fields.TextField):ValueExpression {

    if (!textVE) {
      textVE = config.formElement.getFormElementVE().extendBy(config.propertyname);
    }
    return textVE;
  }

}
}