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
import DisplayFieldSkin from "@coremedia/studio-client.ext.ui-components/skins/DisplayFieldSkin";
import DisplayField from "@jangaroo/ext-ts/form/field/Display";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";

interface AdvancedSettingsTabConfig extends Config<Panel>, Partial<Pick<AdvancedSettingsTab,
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "advancedSettingsVE" |
  "description"
>> {
}

class AdvancedSettingsTab extends Panel {
  declare Config: AdvancedSettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedSettingsTab";

  #bindTo: ValueExpression = null;

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #advancedSettingsVE: ValueExpression = null;

  get advancedSettingsVE(): ValueExpression {
    return this.#advancedSettingsVE;
  }

  set advancedSettingsVE(value: ValueExpression) {
    this.#advancedSettingsVE = value;
  }

  #description: string = null;

  get description(): string {
    return this.#description;
  }

  set description(value: string) {
    this.#description = value;
  }

  constructor(config: Config<AdvancedSettingsTab> = null) {
    super(ConfigUtils.apply(Config(AdvancedSettingsTab, {

      dockedItems: [
        Config(DisplayField, {
          ui: ConfigUtils.asString(DisplayFieldSkin.ITALIC),
          margin: "10 0 10 0",
          dock: "top",
          value: config.description,
        }),
      ],

    }), config));
  }
}

export default AdvancedSettingsTab;
