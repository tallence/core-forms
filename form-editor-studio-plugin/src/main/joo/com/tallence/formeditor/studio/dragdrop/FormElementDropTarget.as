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
import com.tallence.formeditor.studio.helper.FormElementsManager;

import ext.dd.DragSource;
import ext.dd.DropTarget;
import ext.event.Event;

/**
 * A drop target that handles drag / drop events of applied elements and new form elements. Overwritten methods update
 * the ValueExpressions, which stores the current state of the drag/drop event to customize the design of
 * the drop container.
 */
public class FormElementDropTarget extends DropTarget {

  private var formElementsManager:FormElementsManager;
  private var formElementId:String;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var dropActiveVE:ValueExpression;

  public function FormElementDropTarget(formElementsManager:FormElementsManager,
                                        dropActiveVE:ValueExpression,
                                        formElementId:String,
                                        forceReadOnlyValueExpression:ValueExpression,
                                        el:*, config:DropTarget = null) {
    super(el, config);
    this.formElementsManager = formElementsManager;
    this.dropActiveVE = dropActiveVE;
    this.formElementId = formElementId;
    this.forceReadOnlyValueExpression = forceReadOnlyValueExpression;
  }

  override public function notifyDrop(source:DragSource, e:Event, data:Object):Boolean {
    if (!isWritable()) {
      return false;
    }
    var mode:String = data.mode;
    if (mode == FormElementDropContainerBase.TARGET_MODE_ADD) {
      formElementsManager.addFormElement(formElementId, data.formElementType);
      return true;
    } else if (mode == FormElementDropContainerBase.TARGET_MODE_MOVE) {
      formElementsManager.moveFormElement(formElementId, data.formElementId);
      return true;
    }

    trace("[ERROR] - Form element drop mode: " + mode + " not supported.");
    return false;
  }

  override public function notifyOver(source:DragSource, e:Event, data:Object):String {
    if (!isWritable()) {
      dropActiveVE.setValue(false);
      return source.dropNotAllowed;
    }
    dropActiveVE.setValue(true);
    return super.notifyOver(source, e, data);
  }

  override public function notifyOut(source:DragSource, e:Event, data:Object):void {
    dropActiveVE.setValue(false);
    super.notifyOut(source, e, data);
  }

  private function isWritable():Boolean {
    return !(forceReadOnlyValueExpression.getValue() === true);
  }
}
}