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
import com.tallence.formeditor.studio.helper.DragDropHelper;

import ext.container.Container;

/**
 * A form element that can be dragged and dropped at the end of the applied
 * form element list and thus added to the same list.
 */
public class FormElementDroppableBase extends Container {

  private var formElementType:String;
  private var dragActiveVE:ValueExpression;
  private var readOnlyVE:ValueExpression;

  public function FormElementDroppableBase(config:FormElementDroppable = null) {
    super(config);
    formElementType = config.formElementType;
    dragActiveVE = config.dragActiveVE;
    readOnlyVE = config.readOnlyVE;
  }

  override protected function afterRender():void {
    var dragData:Object = {
      mode: FormElementDropContainerBase.TARGET_MODE_ADD,
      formElementType: formElementType
    };
    DragDropHelper.createFormDragSource(this, dragData, dragActiveVE, readOnlyVE);
    super.afterRender();
  }

}
}