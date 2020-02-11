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
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.fields.advancedsettings.AdvancedSettingsFieldBase;

public class FormElementStructWrapper {

  private static const TYPE_PROPERTY:String = "type";

  private var formElementStruct:StructSubBean;
  private var structPropertyName:String;
  private var id:String;
  private var formElementVE:ValueExpression;
  private var type:String;
  private var bindTo:ValueExpression;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var formIssuesVE:ValueExpression;

  public function FormElementStructWrapper(formElementStruct:StructSubBean,
                                           structPropertyName:String,
                                           id:String,
                                           formElementVE:ValueExpression,
                                           bindTo:ValueExpression,
                                           forceReadOnlyValueExpression:ValueExpression) {
    this.formElementStruct = formElementStruct;
    this.structPropertyName = structPropertyName;
    this.id = id;
    this.formElementVE = formElementVE;
    this.type = getString(TYPE_PROPERTY);
    this.bindTo = bindTo;
    this.forceReadOnlyValueExpression = forceReadOnlyValueExpression;
  }

  public function getId():String {
    return id;
  }

  public function getCustomId(): String {
    return getAdvancedSettingsString(AdvancedSettingsFieldBase.CUSTOM_ID);
  }

  public function getFormElementVE():ValueExpression {
    return formElementVE;
  }

  public function getBindTo():ValueExpression {
    return bindTo;
  }

  public function getForceReadOnlyValueExpression():ValueExpression {
    return forceReadOnlyValueExpression;
  }

  public function getType():String {
    return type;
  }

  public function getFormIssuesVE():ValueExpression {
    if (!formIssuesVE) {
      formIssuesVE = ValueExpressionFactory.createFromFunction(function ():FormIssues {
        return new FormIssues(formElementStruct, structPropertyName, bindTo, id);
      });
    }
    return formIssuesVE;
  }

  private function getString(propertyName:String):String {
    if (formElementStruct && formElementStruct.get(propertyName)) {
      return formElementStruct.get(propertyName);
    }
    return "";
  }

  private function getAdvancedSettingsString(propertyName:String):String {
    if (formElementStruct && formElementStruct.get("advancedSettings")) {
      var advancedSettings:StructSubBean = StructSubBean(formElementStruct.get("advancedSettings"));
      return advancedSettings.get(propertyName);
    }
    return "";
  }

}
}
