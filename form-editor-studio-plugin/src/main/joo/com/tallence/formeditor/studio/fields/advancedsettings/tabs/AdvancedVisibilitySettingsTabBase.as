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

package com.tallence.formeditor.studio.fields.advancedsettings.tabs {
import com.coremedia.cap.struct.Struct;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.coremedia.ui.util.createComponentSelector;
import com.tallence.formeditor.studio.AppliedElementsContainer;
import com.tallence.formeditor.studio.FormUtils;
import com.tallence.formeditor.studio.fields.advancedsettings.*;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

public class AdvancedVisibilitySettingsTabBase extends AdvancedSettingsTab {

  protected static const ELEMENTS_COMBO_ITEM_ID:String = "elementsComboBox";

  private var activeVE:ValueExpression;
  private var selectedIdVE:ValueExpression;
  private var visibilityValueVE:ValueExpression;

  public function AdvancedVisibilitySettingsTabBase(config:AdvancedVisibilitySettingsTab = null) {
    super(config);
    query(createComponentSelector().itemId(ELEMENTS_COMBO_ITEM_ID).build())[0].addListener("select", invalidateData);
  }

  protected function getAvailableElementsVE(config:AdvancedVisibilitySettingsTab):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      if (!getActivatedVE(config).getValue()) {
        return [];
      }

      var elements:Array = getAppliedElementsContainer().getFormElementsVE().getValue();

      var el:Array = [];
      elements.forEach(function (wrapper:FormElementStructWrapper):void {
        el.push(createElementComboBoxEntry(wrapper, getFormElementTitle(wrapper)));
      });

      return el;
    });
  }

  protected function selectedFormElementTypeTransformer(formElementId:String):String {
    var elements:Array = getAppliedElementsContainer().getFormElementsVE().getValue();
    var selected:Array = elements.filter(function (wrapper:FormElementStructWrapper):Boolean {
      return isSelected(wrapper, formElementId);
    }).map(getType);
    return selected && selected.length > 0 ? selected[0] : "default";
  }

  /**
   * If a checkbox form element or a radio form element is selected, the stored options are made available for
   * the value selection in a dropdown.
   */
  protected function getAvailableOptionsVE(config:AdvancedVisibilitySettingsTab):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      if (!getActivatedVE(config).getValue()) {
        return [];
      }

      var selectedId:String = getSelectedIdVE(config).getValue();
      var elements:Array = getAppliedElementsContainer().getFormElementsVE().getValue();
      var selected:Array = elements.filter(function (wrapper:FormElementStructWrapper):Boolean {
        return isSelected(wrapper, selectedId);
      });
      if (selected && selected.length > 0) {
        var options:Struct = (selected[0] as FormElementStructWrapper).getFormElementVE().extendBy("groupElements").getValue();
        if (options) {
          return options.getType().getPropertyNames().map(function (value:String):Object {
            return createComboBoxEntry(value, value);
          });
        }
      }
      return [];
    });
  }

  /**
   * After a new form element is selected, the visibility value expression is set back to the default value.
   */
  protected function invalidateData():void {
    getVisibilityValueVE().setValue("");
  }

  // getter for value expressions

  protected function getActivatedVE(config:AdvancedVisibilitySettingsTab):ValueExpression {
    if (!activeVE) {
      activeVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_ACTIVATED);
    }
    return activeVE;
  }

  protected function getSelectedIdVE(config:AdvancedVisibilitySettingsTab):ValueExpression {
    if (!selectedIdVE) {
      selectedIdVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_ELEMENT_ID);
    }
    return selectedIdVE;
  }

  protected function getVisibilityValueVE(config:AdvancedVisibilitySettingsTab = null):ValueExpression {
    if (!visibilityValueVE) {
      visibilityValueVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_VALUE);
    }
    return visibilityValueVE;
  }

  // helper methods

  /**
   * Returns true, if the selected id belongs to the given wrapper.
   */
  private static function isSelected(wrapper:FormElementStructWrapper, selectedId:String):Boolean {
    var customId:String = wrapper.getCustomId();
    return (customId ? customId : wrapper.getId()) == selectedId;
  }

  /**
   * Simple getter method used to return the type of the given form element.
   */
  private static function getType(wrapper:FormElementStructWrapper):String {
    return wrapper.getType();
  }

  /**
   * Returns the parent applied form elements container. The container is used to get the value expression evaluation
   * to all form elements of the form.
   */
  private function getAppliedElementsContainer():AppliedElementsContainer {
    return AppliedElementsContainer(findParentByType(AppliedElementsContainer.xtype));
  }

  /**
   * Maps the from element wrapper to a title displayed in a combo box.
   */
  private static function getFormElementTitle(wrapper:FormElementStructWrapper):String {
    var name:String = wrapper.getFormElementVE().extendBy("name").getValue();
    var formType:String = FormUtils.getConditionTitle(wrapper.getType());
    return name ? formType + ": " + name : formType;
  }

  /**
   * Creates an object used by the
   * {@link com.tallence.formeditor.studio.fields.advancedsettings.tabs.VisibilityComboBoxField}.
   * @param id the id of the entry
   * @param value the displayed text of the entry
   * @return an object
   */
  private static function createComboBoxEntry(id:String, value:String):Object {
    var obj:Object = {};
    obj[VisibilityComboBoxField.FIELD_ID] = id;
    obj[VisibilityComboBoxField.FIELD_VALUE] = value;
    return obj;
  }

  private static function createElementComboBoxEntry(wrapper:FormElementStructWrapper, value:String):Object {
    var customId:String = wrapper.getCustomId();

    return createComboBoxEntry(customId ? customId : wrapper.getId(), value);
  }

}
}
