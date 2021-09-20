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

package com.tallence.formeditor.studio.dragdrop {
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.helper.FormElementsManager;

import ext.container.Container;
import ext.dd.DropTarget;

public class FormElementDropContainerBase extends Container {

  public static const MOVE_TARGET_DD_GROUP:String = "AppliedElementDD";
  public static const TARGET_MODE_ADD:String = "add";
  public static const TARGET_MODE_MOVE:String = "move";

  private var dropTarget:DropTarget;
  private var formElementsManager:FormElementsManager;
  private var formElementId:String;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var dropActiveVE:ValueExpression;

  public function FormElementDropContainerBase(config:FormElementDropContainer = null) {
    super(config);
    this.formElementsManager = config.formElementsManager;
    this.formElementId = config.formElementId;
    this.forceReadOnlyValueExpression = config.forceReadOnlyValueExpression;
  }

  override protected function afterRender():void {
    super.afterRender();
    dropTarget = new FormElementDropTarget(
        formElementsManager,
        getDropActiveVE(),
        formElementId,
        forceReadOnlyValueExpression,
        this, DropTarget({
          el: this.getEl(),
          ddGroup: MOVE_TARGET_DD_GROUP
        }));
  }

  override public function destroy(...params):void {
    dropTarget.unreg();
    super.destroy(params);
  }

  protected function getModifiers(config:FormElementDropContainer):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      var modifiers:Array = [];
      if (config.formElementsManager.getDragActiveVE().getValue()) {
        modifiers.push("drag-active");
      }
      if (getDropActiveVE().getValue()) {
        modifiers.push("drop-active");
      }
      if ((config.formElementsManager.getFormElementsVE().getValue() as Array).length == 0) {
        modifiers.push("empty");
      }
      return modifiers;
    })
  }

  protected function getDropActiveVE():ValueExpression {
    if (!dropActiveVE) {
      dropActiveVE = ValueExpressionFactory.createFromValue(false);
    }
    return dropActiveVE;
  }
}
}
