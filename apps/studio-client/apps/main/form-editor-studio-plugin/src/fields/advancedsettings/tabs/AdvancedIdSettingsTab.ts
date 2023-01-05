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

import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../../../bundles/FormEditor_properties";
import AdvancedSettingsFieldBase from "../AdvancedSettingsFieldBase";
import AdvancedSettingsTab from "./AdvancedSettingsTab";

interface AdvancedIdSettingsTabConfig extends Config<AdvancedSettingsTab> {
}

class AdvancedIdSettingsTab extends AdvancedSettingsTab {
  declare Config: AdvancedIdSettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedIdSettingsTab";

  constructor(config: Config<AdvancedIdSettingsTab> = null) {
    super(ConfigUtils.apply(Config(AdvancedIdSettingsTab, {
      title: FormEditor_properties.FormEditor_advancedSettings_tabs_id_title,
      description: FormEditor_properties.FormEditor_advancedSettings_tabs_id_description,
      items: [
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_id_fieldLabel,
          items: [
            Config(StatefulTextField, {
              emptyText: FormEditor_properties.FormEditor_advancedSettings_tabs_id_emptyText,
              width: "100%",
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  bindTo: config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.CUSTOM_ID),
                }),
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
              ],
            }),
          ],
        }),
      ],

    }), config));
  }
}

export default AdvancedIdSettingsTab;
