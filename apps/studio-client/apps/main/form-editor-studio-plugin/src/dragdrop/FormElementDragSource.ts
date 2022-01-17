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

import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import Component from "@jangaroo/ext-ts/Component";
import DragDrop from "@jangaroo/ext-ts/dd/DragDrop";
import DragSource from "@jangaroo/ext-ts/dd/DragSource";
import Event from "@jangaroo/ext-ts/event/Event";
import Config from "@jangaroo/runtime/Config";
import FormElementDropContainerBase from "./FormElementDropContainerBase";

/**
 * A drag source that handles drag / drop events of applied elements and new form elements. Overwritten methods update
 * the ValueExpressions, which stores the current state of the drag/drop event to customize the design of
 * the drop container.
 */
class FormElementDragSource extends DragSource {

  #dragActiveVE: ValueExpression = null;

  #readOnlyVE: ValueExpression = null;

  #component: Component = null;

  constructor(component: Component,
              dragActiveVE: ValueExpression,
              readOnlyVE: ValueExpression) {
    const cfg = Config(DragSource, {
      ddGroup: FormElementDropContainerBase.MOVE_TARGET_DD_GROUP,
      scroll: false
    });
    super(...((): [any, any] => {
      this.#dragActiveVE = dragActiveVE;
      this.#component = component;
      this.#readOnlyVE = readOnlyVE;
      return [component.getEl(), cfg];
    })());
  }

  override onBeforeDrag(data: any, e: Event): boolean {
    this.resizeFrame = true;
    return this.#dragIsPossible();
  }

  #dragIsPossible(): boolean {
    return !this.#component.disabled && !this.#readOnlyVE.getValue();
  }

  override onStartDrag(x: number, y: number): void {
    this.#setDragActiveVeToTrue();
    super.onStartDrag(x, y);
  }

  override afterInvalidDrop(e: Event, id: string): void {
    this.#setDragActiveToFalse();
  }

  override afterValidDrop(target: any, e: Event, id: string): void {
    this.#setDragActiveToFalse();
  }

  override afterDragDrop(target: DragDrop, e: Event, id: string): void {
    this.#setDragActiveToFalse();
  }

  #setDragActiveVeToTrue(): void {
    this.#dragActiveVE.setValue(true);
  }

  #setDragActiveToFalse(): void {
    this.#dragActiveVE.setValue(false);
  }
}

export default FormElementDragSource;
