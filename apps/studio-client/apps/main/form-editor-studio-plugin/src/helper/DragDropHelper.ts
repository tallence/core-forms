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
import DragSource from "@jangaroo/ext-ts/dd/DragSource";
import FormElementDragSource from "../dragdrop/FormElementDragSource";

class DragDropHelper {

  static createFormDragSource(component: Component,
    dragData: any,
    dragStateVE: ValueExpression,
    forceReadOnlyVE: ValueExpression): DragSource {
    return new FormElementDragSource(component, dragData, dragStateVE, forceReadOnlyVE);
  }

}

export default DragDropHelper;
