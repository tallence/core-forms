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

import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import createComponentSelector from "@coremedia/studio-client.ext.ui-components/util/createComponentSelector";
import { as, bind, cast } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import AppliedFormElementContainer from "../../../AppliedFormElementContainer";
import FormUtils from "../../../FormUtils";
import FormElementStructWrapper from "../../../model/FormElementStructWrapper";
import AdvancedSettingsFieldBase from "../AdvancedSettingsFieldBase";
import AdvancedSettingsTab from "./AdvancedSettingsTab";
import AdvancedVisibilitySettingsTab from "./AdvancedVisibilitySettingsTab";
import VisibilityComboBoxField from "./VisibilityComboBoxField";

interface AdvancedVisibilitySettingsTabBaseConfig extends Config<AdvancedSettingsTab> {
}

class AdvancedVisibilitySettingsTabBase extends AdvancedSettingsTab {
  declare Config: AdvancedVisibilitySettingsTabBaseConfig;

  protected static readonly ELEMENTS_COMBO_ITEM_ID: string = "elementsComboBox";

  #activeVE: ValueExpression = null;

  #selectedIdVE: ValueExpression = null;

  #visibilityValueVE: ValueExpression = null;

  constructor(config: Config<AdvancedVisibilitySettingsTab> = null) {
    super(config);
    this.query(createComponentSelector().itemId(AdvancedVisibilitySettingsTabBase.ELEMENTS_COMBO_ITEM_ID).build())[0].addListener("select", bind(this, this.invalidateData));
  }

  protected getAvailableElementsVE(config: Config<AdvancedVisibilitySettingsTab>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      if (!this.getActivatedVE(config).getValue()) {
        return [];
      }

      const elements: Array<any> = this.#getAppliedElementsContainer().getFormElementsVE().getValue();

      const el = [];
      elements.forEach((wrapper: FormElementStructWrapper): void => {
        el.push(AdvancedVisibilitySettingsTabBase.#createElementComboBoxEntry(wrapper, AdvancedVisibilitySettingsTabBase.#getFormElementTitle(wrapper)));
      });

      return el;
    });
  }

  protected selectedFormElementTypeTransformer(formElementId: string): string {
    const elements: Array<any> = this.#getAppliedElementsContainer().getFormElementsVE().getValue();
    const selected = elements.filter((wrapper: FormElementStructWrapper): boolean =>
      AdvancedVisibilitySettingsTabBase.#isSelected(wrapper, formElementId),
    ).map(AdvancedVisibilitySettingsTabBase.#getType);
    return selected && selected.length > 0 ? selected[0] : "default";
  }

  /**
   * If a checkbox form element or a radio form element is selected, the stored options are made available for
   * the value selection in a dropdown.
   */
  protected getAvailableOptionsVE(config: Config<AdvancedVisibilitySettingsTab>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      if (!this.getActivatedVE(config).getValue()) {
        return [];
      }

      const selectedId: string = this.getSelectedIdVE(config).getValue();
      const elements: Array<any> = this.#getAppliedElementsContainer().getFormElementsVE().getValue();
      const selected = elements.filter((wrapper: FormElementStructWrapper): boolean =>
        AdvancedVisibilitySettingsTabBase.#isSelected(wrapper, selectedId),
      );
      if (selected && selected.length > 0) {
        const options: Struct = as(selected[0], FormElementStructWrapper).getFormElementVE().extendBy("groupElements").getValue();
        if (options) {
          return options.getType().getPropertyNames().map((displayValue: string): any => {

            //Field with options, e.g. checkboxes/selectboxes/radiogroups may have an (optional) value set for each
            //option entry. If so, use this one, otherwise use the displayed label
            const internalValue: string = options.get(displayValue).get("value");
            return AdvancedVisibilitySettingsTabBase.#createComboBoxEntry(internalValue != null ? internalValue : displayValue, displayValue);
          });
        }
      }
      return [];
    });
  }

  /**
   * After a new form element is selected, the visibility value expression is set back to the default value.
   */
  protected invalidateData(): void {
    this.getVisibilityValueVE().setValue("");
  }

  // getter for value expressions

  protected getActivatedVE(config: Config<AdvancedVisibilitySettingsTab>): ValueExpression {
    if (!this.#activeVE) {
      this.#activeVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_ACTIVATED);
    }
    return this.#activeVE;
  }

  protected getSelectedIdVE(config: Config<AdvancedVisibilitySettingsTab>): ValueExpression {
    if (!this.#selectedIdVE) {
      this.#selectedIdVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_ELEMENT_ID);
    }
    return this.#selectedIdVE;
  }

  protected getVisibilityValueVE(config: Config<AdvancedVisibilitySettingsTab> = null): ValueExpression {
    if (!this.#visibilityValueVE) {
      this.#visibilityValueVE = config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.VISIBILITY, AdvancedSettingsFieldBase.VISIBILITY_VALUE);
    }
    return this.#visibilityValueVE;
  }

  // helper methods

  /**
   * Returns true, if the selected id belongs to the given wrapper.
   */
  static #isSelected(wrapper: FormElementStructWrapper, selectedId: string): boolean {
    const customId = wrapper.getCustomId();
    return (customId ? customId : wrapper.getId()) == selectedId;
  }

  /**
   * Simple getter method used to return the type of the given form element.
   */
  static #getType(wrapper: FormElementStructWrapper): string {
    return wrapper.getType();
  }

  /**
   * Returns the parent applied form elements container. The container is used to get the value expression evaluation
   * to all form elements of the form.
   */
  #getAppliedElementsContainer(): AppliedFormElementContainer {
    return cast(AppliedFormElementContainer, this.findParentByType(AppliedFormElementContainer.xtype));
  }

  /**
   * Maps the from element wrapper to a title displayed in a combo box.
   */
  static #getFormElementTitle(wrapper: FormElementStructWrapper): string {
    const name: string = wrapper.getFormElementVE().extendBy("name").getValue();
    const formType = FormUtils.getConditionTitle(wrapper.getType());
    return name ? formType + ": " + name : formType;
  }

  /**
   * Creates an object used by the
   * {@link com.tallence.formeditor.studio.fields.advancedsettings.tabs.VisibilityComboBoxField}.
   * @param id the id of the entry
   * @param value the displayed text of the entry
   * @return an object
   */
  static #createComboBoxEntry(id: string, value: string): any {
    const obj: Record<string, any> = {};
    obj[VisibilityComboBoxField.FIELD_ID] = id;
    obj[VisibilityComboBoxField.FIELD_VALUE] = value;
    return obj;
  }

  static #createElementComboBoxEntry(wrapper: FormElementStructWrapper, value: string): any {
    const customId = wrapper.getCustomId();

    return AdvancedVisibilitySettingsTabBase.#createComboBoxEntry(customId ? customId : wrapper.getId(), value);
  }

}

export default AdvancedVisibilitySettingsTabBase;
