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
import com.coremedia.ui.data.ValueExpression;

import ext.form.field.NumberField;

public class NumberFieldBase extends ext.form.field.NumberField {

  private var numberVE:ValueExpression;

  public function NumberFieldBase(config:com.tallence.formeditor.studio.fields.NumberField = null) {
    super(config);
  }

  protected function getNumberVE(config:com.tallence.formeditor.studio.fields.NumberField):ValueExpression {
    if (!numberVE) {
      numberVE = config.formElement.getFormElementVE().extendBy(config.propertyName);
    }
    return numberVE;
  }

}
}