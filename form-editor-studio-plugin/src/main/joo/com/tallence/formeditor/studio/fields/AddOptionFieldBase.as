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

import ext.MessageBox;

public class AddOptionFieldBase extends FormEditorField {

  private static const GROUP_ELEMENTS_STRUCT_NAME:String = "groupElements";

  private var addOptionVE:ValueExpression;
  private var formElementsStruct:Struct;

  public function AddOptionFieldBase(config:AddOptionField = null) {
    super(config);
  }

  public function addGroupElement():void {

    var newOptionText:String = addOptionVE.getValue();
    if (newOptionText) {

      if (!formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME)) {
        formElementsStruct.getType().addStructProperty(GROUP_ELEMENTS_STRUCT_NAME)
      }
      var groupElements:Struct = formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME);
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
    if (!formElementsStruct.get(GROUP_ELEMENTS_STRUCT_NAME)) {
      formElementsStruct.getType().addStructProperty(GROUP_ELEMENTS_STRUCT_NAME)
    }
  }

  public function getAddOptionVE():ValueExpression {
    if (!addOptionVE) {
      addOptionVE = ValueExpressionFactory.createFromValue("");
    }
    return addOptionVE;
  }

}
}