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
import com.tallence.formeditor.studio.model.GroupElementStructWrapper;

import ext.MessageBox;

public class AddOptionFieldBase extends FormEditorField {

  private var addOptionVE:ValueExpression;
  private var formElementsStruct:Struct;
  private var groupElementStructName:String;

  public function AddOptionFieldBase(config:AddOptionField = null) {
    super(config);
    groupElementStructName = config.propertyName;
  }

  public function addGroupElement():void {

    var newOptionText:String = addOptionVE.getValue();
    if (newOptionText) {

      if (!formElementsStruct.get(groupElementStructName)) {
        formElementsStruct.getType().addStructProperty(groupElementStructName)
      }
      var groupElements:Struct = formElementsStruct.get(groupElementStructName);
      groupElements.getType().addStructProperty(newOptionText);

      //Reset the textField for the new GroupElement's name
      addOptionVE.setValue("");

      FormUtils.reloadPreview();
    } else {
      MessageBox.alert("Fehler", "Geben Sie den Namen des neuen Buttons in das Textfeld ein.");
    }
  }

  override protected function initStruct(struct:Struct):void {
    formElementsStruct = struct;
    if (!formElementsStruct.get(groupElementStructName)) {
      formElementsStruct.getType().addStructProperty(groupElementStructName)
    }
  }

  public function getAddOptionVE():ValueExpression {
    if (!addOptionVE) {
      addOptionVE = ValueExpressionFactory.createFromValue("");
    }
    return addOptionVE;
  }

  public function getGroupElementsVE(config:FormEditorField):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      var groupsVE:ValueExpression = getPropertyVE(config);
      if (groupsVE.getValue()) {
        var groupElementStruct:Struct = groupsVE.getValue();
        return groupElementStruct.getType().getPropertyNames().map(function (name:String):GroupElementStructWrapper {
          return new GroupElementStructWrapper(groupsVE.extendBy(name), name);
        });
      } else {
        return undefined;
      }
    });
  }

  public function removeGroupElement(value:String):void {
    var groupElementsList:Struct = formElementsStruct.get(groupElementStructName);
    groupElementsList.getType().removeProperty(value);
    FormUtils.reloadPreview();
  }

}
}