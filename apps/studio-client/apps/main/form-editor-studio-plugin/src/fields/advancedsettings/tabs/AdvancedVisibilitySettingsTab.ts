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

import StatefulCheckbox from "@coremedia/studio-client.ext.ui-components/components/StatefulCheckbox";
import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import SwitchingContainer from "@coremedia/studio-client.ext.ui-components/components/SwitchingContainer";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindVisibilityPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindVisibilityPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../../../bundles/FormEditor_properties";
import CheckBoxesEditor from "../../../elements/CheckBoxesEditor";
import RadioButtonsEditor from "../../../elements/RadioButtonsEditor";
import SelectBoxEditor from "../../../elements/SelectBoxEditor";
import AdvancedVisibilitySettingsTabBase from "./AdvancedVisibilitySettingsTabBase";
import VisibilityComboBoxField from "./VisibilityComboBoxField";

interface AdvancedVisibilitySettingsTabConfig extends Config<AdvancedVisibilitySettingsTabBase> {
}

class AdvancedVisibilitySettingsTab extends AdvancedVisibilitySettingsTabBase {
  declare Config: AdvancedVisibilitySettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedVisibilitySettingsTab";

  constructor(config: Config<AdvancedVisibilitySettingsTab> = null) {
    super((()=> ConfigUtils.apply(Config(AdvancedVisibilitySettingsTab, {
      title: FormEditor_properties.FormEditor_advancedSettings_tabs_visibility_title,
      description: FormEditor_properties.FormEditor_advancedSettings_tabs_visibility_description,
      items: [
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_visibility_active_fieldLabel,
          items: [
            Config(StatefulCheckbox, {
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  skipIfUndefined: true,
                  bindTo: this.getActivatedVE(config),
                }),
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
              ],
            }),
          ],
        }),
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_visibility_element_fieldLabel,
          items: [
            Config(VisibilityComboBoxField, {
              itemId: AdvancedVisibilitySettingsTabBase.ELEMENTS_COMBO_ITEM_ID,
              bindTo: config.bindTo,
              forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              elementsVE: this.getAvailableElementsVE(config),
              selectedVE: this.getSelectedIdVE(config),
            }),
          ],
          plugins: [
            Config(BindVisibilityPlugin, { bindTo: this.getActivatedVE(config) }),
          ],
        }),
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_visibility_value_fieldLabel,
          items: [
            Config(SwitchingContainer, {
              activeItemValueExpression: this.getSelectedIdVE(config),
              transformer: bind(this, this.selectedFormElementTypeTransformer),
              items: [
                /* If a special editor is required, an editor can be defined based on the type of the form element. For
            checkboxes and radio boxes, the defined options are displayed in a drop down box and can be selected by the
            user */
                Config(VisibilityComboBoxField, {
                  itemId: RadioButtonsEditor.FIELD_TYPE,
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                  elementsVE: this.getAvailableOptionsVE(config),
                  selectedVE: this.getVisibilityValueVE(config),
                }),
                Config(VisibilityComboBoxField, {
                  itemId: CheckBoxesEditor.FIELD_TYPE,
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                  elementsVE: this.getAvailableOptionsVE(config),
                  selectedVE: this.getVisibilityValueVE(config),
                }),
                Config(VisibilityComboBoxField, {
                  itemId: SelectBoxEditor.FIELD_TYPE,
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                  elementsVE: this.getAvailableOptionsVE(config),
                  selectedVE: this.getVisibilityValueVE(config),
                }),
              ],
              itemTemplate:
            /* By default the value can be set using a string property field. */
            Config(StatefulTextField, {
              width: "100%",
              ...ConfigUtils.append({
                plugins: [
                  Config(BindPropertyPlugin, {
                    bidirectional: true,
                    skipIfUndefined: true,
                    bindTo: this.getVisibilityValueVE(config),
                  }),
                  Config(BindDisablePlugin, {
                    bindTo: config.bindTo,
                    forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                  }),
                ],
              }),
            }),
              ...ConfigUtils.append({
                plugins: [
                  Config(BindVisibilityPlugin, { bindTo: this.getActivatedVE(config) }),
                ],
              }),
            }),
          ],
          plugins: [
            Config(BindVisibilityPlugin, { bindTo: this.getActivatedVE(config) }),
          ],
        }),
      ],
    }), config))());
  }
}

export default AdvancedVisibilitySettingsTab;
