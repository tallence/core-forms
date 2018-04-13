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

import ext.Component;
import ext.dd.DragDrop;
import ext.dd.DragSource;
import ext.event.Event;

/**
 * A drag source that handles drag / drop events of applied elements and new form elements. Overwritten methods update
 * the ValueExpressions, which stores the current state of the drag/drop event to customize the design of
 * the drop container.
 */
public class FormElementDragSource extends DragSource {

  private var dragActiveVE:ValueExpression;
  private var component:Component;

  public function FormElementDragSource(component:Component, dragData:Object, dragActiveVE:ValueExpression) {
    var cfg:DragSource = DragSource({
      dragData: dragData,
      ddGroup: FormElementDropContainerBase.MOVE_TARGET_DD_GROUP,
      scroll: false,
      resizeFrame: true
    });
    this.dragActiveVE = dragActiveVE;
    this.component = component;
    super(component.getEl(), cfg);
  }

  override public function onBeforeDrag(data:Object, e:Event):Boolean {
    this.resizeFrame = true;
    return !component.disabled;
  }

  override public function onStartDrag(x:Number, y:Number):void {
    setDragActiveVeToTrue();
    super.onStartDrag(x, y);
  }

  override public function afterInvalidDrop(e:Event, id:String):void {
    setDragActiveToFalse();
  }

  override public function afterValidDrop(target:Object, e:Event, id:String):void {
    setDragActiveToFalse();
  }

  override public function afterDragDrop(target:DragDrop, e:Event, id:String):void {
    setDragActiveToFalse();
  }

  private function setDragActiveVeToTrue():void {
    dragActiveVE.setValue(true);
  }

  private function setDragActiveToFalse():void {
    dragActiveVE.setValue(false);
  }
}
}