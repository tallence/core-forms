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
import DragSource from "@jangaroo/ext-ts/dd/DragSource";
import DropTarget from "@jangaroo/ext-ts/dd/DropTarget";
import Event from "@jangaroo/ext-ts/event/Event";
import Config from "@jangaroo/runtime/Config";
import trace from "@jangaroo/runtime/trace";
import FormElementsManager from "../helper/FormElementsManager";
import FormElementDropContainerBase from "./FormElementDropContainerBase";

/**
 * A drop target that handles drag / drop events of applied elements and new form elements. Overwritten methods update
 * the ValueExpressions, which stores the current state of the drag/drop event to customize the design of
 * the drop container.
 */
class FormElementDropTarget extends DropTarget {

  #formElementsManager: FormElementsManager = null;

  #formElementId: string = null;

  #forceReadOnlyValueExpression: ValueExpression = null;

  #dropActiveVE: ValueExpression = null;

  constructor(formElementsManager: FormElementsManager,
    dropActiveVE: ValueExpression,
    formElementId: string,
    forceReadOnlyValueExpression: ValueExpression,
    el: any, config: Config<DropTarget> = null) {
    super(el, config);
    this.#formElementsManager = formElementsManager;
    this.#dropActiveVE = dropActiveVE;
    this.#formElementId = formElementId;
    this.#forceReadOnlyValueExpression = forceReadOnlyValueExpression;
  }

  override notifyDrop(source: DragSource, e: Event, data: any): boolean {
    if (!this.#isWritable()) {
      return false;
    }
    const mode: string = data.mode;
    if (mode == FormElementDropContainerBase.TARGET_MODE_ADD) {
      this.#formElementsManager.addFormElement(this.#formElementId, data.formElementType);
      return true;
    } else if (mode == FormElementDropContainerBase.TARGET_MODE_MOVE) {
      this.#formElementsManager.moveFormElement(this.#formElementId, data.formElementId);
      return true;
    }

    trace("[ERROR] - Form element drop mode: " + mode + " not supported.");
    return false;
  }

  override notifyOver(source: DragSource, e: Event, data: any): string {
    if (!this.#isWritable()) {
      this.#dropActiveVE.setValue(false);
      return source.dropNotAllowed;
    }
    this.#dropActiveVE.setValue(true);
    return super.notifyOver(source, e, data);
  }

  override notifyOut(source: DragSource, e: Event, data: any): void {
    this.#dropActiveVE.setValue(false);
    super.notifyOut(source, e, data);
  }

  #isWritable(): boolean {
    return !(this.#forceReadOnlyValueExpression.getValue() === true);
  }
}

export default FormElementDropTarget;
