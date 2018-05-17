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
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.dragdrop.FormElementDroppable;

import ext.Component;
import ext.container.Container;
import ext.panel.Panel;

public class ApplicableElementsBase extends Container {

  protected static const APPLICABLE_ELEMENTS_CONTAINER_ID:String = "groupsCt";

  private var groupedFormElements:Object;
  private var groupsCt:Container;
  private var dragActiveVE:ValueExpression;
  private var readOnlyVE:ValueExpression;

  public function ApplicableElementsBase(config:ApplicableElements = null) {
    super(config);
    this.groupedFormElements = groupFormElements(config.formElements);
    this.dragActiveVE = config.dragActiveVE;
    this.readOnlyVE = config.readOnlyVE;
  }

  override protected function afterRender():void {
    super.afterRender();
    renderGroupedConditions();
  }

  private function renderGroupedConditions():void {
    for (var group:String in groupedFormElements) {
      var groupPanel:Panel = new CollapsiblePanel(CollapsiblePanel({
        title: getGroupTitle(group),
        margin: "10 0 0 0"
      }));

      for (var i:Number = 0; i < groupedFormElements[group].length; i++) {
        var formElementConfig:Object = groupedFormElements[group][i];
        groupPanel.add(
            new FormElementDroppable(FormElementDroppable({
              width: 200,
              formElementType: formElementConfig["formElementType"],
              dragActiveVE: dragActiveVE,
              readOnlyVE: readOnlyVE
            }))
        );
      }
      getGroupsCt().add(groupPanel);
    }

  }

  private function getGroupsCt():Container {
    if (!groupsCt) {
      groupsCt = this.queryBy(groupContainerFilter)[0] as Container;
    }
    return groupsCt;
  }

  private static function groupContainerFilter(component:Component):Boolean {
    return component.itemId === APPLICABLE_ELEMENTS_CONTAINER_ID;
  }

  private static function groupFormElements(formElements:Array):Object {
    var config:Object = {};
    var group:String;
    var groupedElements:Object = {};

    for (var i:Number = 0; i < formElements.length; i++) {
      config = formElements[i];
      group = config["group"];
      if (!groupedElements[group]) {
        groupedElements[group] = [];
      }
      groupedElements[group].push(config);
    }

    return groupedElements;
  }

  private function getGroupTitle(groupName:String):String {
    return resourceManager.getString('com.tallence.formeditor.studio.bundles.FormEditor', 'FormEditor_title_group_' + groupName) || groupName;
  }

}
}