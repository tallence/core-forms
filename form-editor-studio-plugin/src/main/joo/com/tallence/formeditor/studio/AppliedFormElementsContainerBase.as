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

package com.tallence.formeditor.studio {
import com.coremedia.cms.editor.sdk.premular.CollapsiblePanel;
import com.coremedia.cms.editor.sdk.util.PropertyEditorUtil;
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.dragdrop.FormElementDropContainerBase;
import com.tallence.formeditor.studio.helper.DragDropHelper;
import com.tallence.formeditor.studio.helper.FormElementsManager;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.container.Container;
import ext.panel.PanelHeader;
import ext.panel.events.PanelEvent;

public class AppliedFormElementsContainerBase extends Container {

  protected static const FORM_ELEMENT_PANEL:String = "form-element-collapsible-panel";
  protected static const FORM_ELEMENT_HEADER:String = "form-element-header";


  [Bindable]
  public var bindTo:ValueExpression;

  [Bindable]
  public var forceReadOnlyValueExpression:ValueExpression;

  [Bindable]
  public var formElement:FormElementStructWrapper;

  [Bindable]
  public var formElementsManager:FormElementsManager;

  private var dragActiveVE:ValueExpression;
  private var readOnlyVE:ValueExpression;

  public function AppliedFormElementsContainerBase(config:AppliedFormElementsContainerBase = null) {
    super(config);
    this.formElement = config.formElement;
    this.formElementsManager = config.formElementsManager;
    this.dragActiveVE = config.formElementsManager.getDragActiveVE();
    // Create a value expression to bind the disabled state of the drag source. It is necessary to use the two
    // value expressions 'bindTo' and 'forceReadOnlyValueExpression' to create the read only value expression. If a
    // content is checked out by another user, the read only value is not stored in the forceReadOnlyValueExpression.
    this.readOnlyVE = PropertyEditorUtil.createReadOnlyValueExpression(config.bindTo, config.forceReadOnlyValueExpression);
  }

  override protected function afterRender():void {
    super.afterRender();
    var panel:CollapsiblePanel = queryById(FORM_ELEMENT_PANEL) as CollapsiblePanel;
    panel.addEventListener(PanelEvent.EXPAND, function (eventType:PanelEvent):void {
      formElementsManager.getCollapsedElementVE().setValue(formElement.getId());
    });

    makeFormElementDraggable();
  }

  private function makeFormElementDraggable():void {
    var formElementId:String = (getConfig("formElement") as FormElementStructWrapper).getId();
    var panelHealder:PanelHeader = PanelHeader(queryById(FORM_ELEMENT_HEADER));
    var dragData:Object = {
      mode: FormElementDropContainerBase.TARGET_MODE_MOVE,
      formElementId: formElementId
    };
    DragDropHelper.createFormDragSource(panelHealder, dragData, dragActiveVE, readOnlyVE);
  }

  public function collapsedTransformer(id:String):Boolean {
    return id !== formElement.getId();
  }

  public function titleTransformer(value:String):String {
    return value ? value : FormUtils.getConditionTitle(formElement.getType())
  }

  public static function getTitleUndefinedValue(formElement:FormElementStructWrapper):String {
    return FormUtils.getConditionTitle(formElement.getType());
  }

  public function removeElementHandler():void {
    formElementsManager.removeFormElement(formElement.getId());
  }
}
}