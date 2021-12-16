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
import Container from "@jangaroo/ext-ts/container/Container";
import Config from "@jangaroo/runtime/Config";
import DragDropHelper from "../helper/DragDropHelper";
import FormElementDropContainerBase from "./FormElementDropContainerBase";
import FormElementDroppable from "./FormElementDroppable";

interface FormElementDroppableBaseConfig extends Config<Container> {
}

/**
 * A form element that can be dragged and dropped at the end of the applied
 * form element list and thus added to the same list.
 */
class FormElementDroppableBase extends Container {
  declare Config: FormElementDroppableBaseConfig;

  #formElementType: string = null;

  #dragActiveVE: ValueExpression = null;

  #readOnlyVE: ValueExpression = null;

  constructor(config: Config<FormElementDroppable> = null) {
    super(config);
    this.#formElementType = config.formElementType;
    this.#dragActiveVE = config.dragActiveVE;
    this.#readOnlyVE = config.readOnlyVE;
  }

  protected override afterRender(): void {
    const dragData: Record<string, any> = {
      mode: FormElementDropContainerBase.TARGET_MODE_ADD,
      formElementType: this.#formElementType,
    };
    DragDropHelper.createFormDragSource(this, dragData, this.#dragActiveVE, this.#readOnlyVE);
    super.afterRender();
  }

}

export default FormElementDroppableBase;
