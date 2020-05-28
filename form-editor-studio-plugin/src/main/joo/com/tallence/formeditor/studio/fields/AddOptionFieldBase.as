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
import ext.util.Format;

public class AddOptionFieldBase extends FormEditorField {

  [Bindable]
  public var allowMultiDefaultSelection:Boolean = false;

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
        formElementsStruct.getType().addStructProperty(groupElementStructName);
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
    super.initStruct(struct);
    formElementsStruct = struct;
    if (!formElementsStruct.get(groupElementStructName)) {
      formElementsStruct.getType().addStructProperty(groupElementStructName);
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
            return new GroupElementStructWrapper(groupsVE.extendBy(name), name, onDefaultSelectionChanged);
          });
        } else {
          return undefined;
        }
      });
  }

  /**
   * remove an option from the list
   * @param value
   */
  public function removeGroupElement(value:String):void {
    var groupElementsList:Struct = formElementsStruct.get(groupElementStructName);
    groupElementsList.getType().removeProperty(value);
    FormUtils.reloadPreview();
  }

  /**
   * updates an existing option
   * @param selectedOption existing option
   * @param newId new display value
   * @param newValue new option value
   * @param newChecked new checked state
   */
  public function updateGroupElement(selectedOption:GroupElementStructWrapper, newId:String, newValue:String, newChecked:Boolean):void {

    //update sub properties
    selectedOption.getOptionValueVE().setValue(newValue);
    selectedOption.getOptionSelectedByDefaultVE().setValue(newChecked);

    //rename struct
    var groupElementsList:Struct = formElementsStruct.get(groupElementStructName);
    groupElementsList.getType().renameProperty(selectedOption.getId(), newId);

    FormUtils.reloadPreview();
  }

  /**
   * moves an existing option in the list of all available options
   * @param id option to move
   * @param moveDistance positions to move, 1 for up, -1 for down
   */
  public function moveGroupElement(id:String, moveDistance:Number):void {
    var groupElementsList:Struct = formElementsStruct.get(groupElementStructName);
    var optionIds:Array = groupElementsList.getType().getPropertyNames();

    var startingIndex:Number = optionIds.indexOf(id);
    if (startingIndex == -1) {
      return;
    }

    var targetIndex:Number = startingIndex + moveDistance;
    if (targetIndex >= (optionIds.length)) {
      targetIndex = 0;
    }
    if (targetIndex < 0) {
      targetIndex = optionIds.length - 1;
    }

    optionIds.splice(targetIndex, 0, optionIds.splice(startingIndex, 1)[0]);

    groupElementsList.getType().rearrangeProperties(optionIds);
    FormUtils.reloadPreview();
  }

  /**
   * unselect all other options if allowMultiDefaultSelection is false
   *
   * @param optionId
   * @param selected
   */
  internal function onDefaultSelectionChanged(optionId:String, selected:Boolean):void {
    if (!allowMultiDefaultSelection && selected) {

      var groupElementsList:Struct = formElementsStruct.get(groupElementStructName);
      var optionIds:Array = groupElementsList.getType().getPropertyNames();

      optionIds.forEach(function(id:String):void {
        var optionStruct:Struct = groupElementsList.get(id);
        optionStruct.set(GroupElementStructWrapper.PROP_CHECKED, id == optionId)
      });
    }
  }

  protected function getAddOptionButtonDisabledVE():ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Boolean {
      var optionName:String = getAddOptionVE().getValue();
      return forceReadOnlyValueExpression.getValue() || optionName == null || !Format.trim(optionName).length;
    })
  }

}
}
