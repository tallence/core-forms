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
import com.coremedia.cap.struct.Struct;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.FormUtils;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;
import com.tallence.formeditor.studio.model.GroupElementStructWrapper;

import ext.container.Container;

public class OptionsFieldBase extends Container {

  private static const GROUP_ELEMENTS_STRUCT_NAME:String = "groupElements";

  [Bindable]
  public var formElement:FormElementStructWrapper;


  public function OptionsFieldBase(config:OptionsField = null) {
    super(config);
  }

  public function removeGroupElement(value:String):void {
    var formElementsStruct:Struct = getFormElementsStruct(this.formElement);
    var groupElementsList:Struct = formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME);
    groupElementsList.getType().removeProperty(value);

    FormUtils.reloadPreview();
  }

  public function getGroupElementsVE(formElement:FormElementStructWrapper):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {

      var formElementsStruct:Struct = getFormElementsStruct(formElement);
      if (!formElementsStruct || !formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME)) {
        return undefined;
      }
      var groupElementStruct:Struct = formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME);
      return groupElementStruct.getType().getPropertyNames().map(function (name:String):GroupElementStructWrapper {
        return new GroupElementStructWrapper(formElement.getFormElementVE().extendBy(GROUP_ELEMENTS_STRUCT_NAME + "." + name), name);
      });
    });
  }

  private static function getFormElementsStruct(formElement:FormElementStructWrapper):Struct {
    return formElement.getFormElementVE().getValue();
  }
}
}