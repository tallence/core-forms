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

package com.tallence.formeditor.studio.model {
import com.coremedia.cap.common.impl.StructSubBean;
import com.coremedia.ui.data.ValueExpression;

public class FormElementStructWrapper {

  private static const TYPE_PROPERTY:String = "type";

  private var formElementStruct:StructSubBean;
  private var id:String;
  private var formElementVE:ValueExpression;

  public function FormElementStructWrapper(formElementStruct:StructSubBean, id:String, formElementVE:ValueExpression) {
    this.formElementStruct = formElementStruct;
    this.id = id;
    this.formElementVE = formElementVE;
  }

  public function getId():String {
    return id;
  }

  public function getFormElementVE():ValueExpression {
    return formElementVE;
  }

  public function getType():String {
    return getString(TYPE_PROPERTY);
  }

  private function getString(propertyName:String):String {
    if (formElementStruct && formElementStruct.get(propertyName)) {
      return formElementStruct.get(propertyName);
    }
    return "";
  }

}
}