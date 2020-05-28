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

package com.tallence.formeditor.studio.elements {

import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.fields.EditOptionWindow;
import com.tallence.formeditor.studio.model.GroupElementStructWrapper;

import ext.panel.Panel;

public class ElementGroupEntryBase extends Panel {

  [Bindable]
  public var removeGroupElementFn:Function;

  [Bindable]
  public var updateOptionElementFn:Function;

  [Bindable]
  public var moveOptionElementFn:Function;

  [Bindable]
  public var option:GroupElementStructWrapper;

  public function ElementGroupEntryBase(config:ElementGroupEntryBase = null) {
    super(config);
  }

  internal function removeOption():void {
    this.removeGroupElementFn.call(NaN, this.option.getId());
  }

  internal function editOption():void {

    function onHandleSave(newId:String, newValue:String, isChecked:Boolean):void {
      updateOptionElementFn.call(NaN, option, newId, newValue, isChecked);
    }

    function onHandleRemove():void {
      removeGroupElementFn.call(NaN, option.getId());
    }

    var window:EditOptionWindow = new EditOptionWindow(EditOptionWindow({
      width: 500,
      title: resourceManager.getString('com.tallence.formeditor.studio.bundles.FormEditor', 'FormEditor_text_edit_option') + " '" + this.option.getId() + "'",
      onSaveCallback: onHandleSave,
      onRemoveCallback: onHandleRemove,
      option: this.option
    }));

    window.show();
  }

  internal function moveUp():void {
    this.moveOptionElementFn.call(NaN, this.option.getId(), -1);
  }

  internal function moveDown():void {
    this.moveOptionElementFn.call(NaN, this.option.getId(), 1);
  }

  protected function getPanelHeaderModifiers(option:GroupElementStructWrapper):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      var modifiers:Array = [];
      if (option.getOptionSelectedByDefaultVE().getValue()) {
        modifiers.push("selected");
      }
      return modifiers;
    })
  }

}

}
