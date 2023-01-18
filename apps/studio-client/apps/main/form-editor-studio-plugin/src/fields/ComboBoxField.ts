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

import LocalComboBox from "@coremedia/studio-client.ext.ui-components/components/LocalComboBox";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import ComboBoxFieldBase from "./ComboBoxFieldBase";

interface ComboBoxFieldConfig extends Config<ComboBoxFieldBase>, Partial<Pick<ComboBoxField,
  "emptyText" |
  "store"
>> {
}

class ComboBoxField extends ComboBoxFieldBase {
  declare Config: ComboBoxFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.comboBoxField";

  #emptyText: string = null;

  get emptyText(): string {
    return this.#emptyText;
  }

  set emptyText(value: string) {
    this.#emptyText = value;
  }

  #store: any;

  /**
   * Different values can be passed here: an array or a store. This is also described in
   * {@link ext.form.field.ComboBox#store}
   */
  get store(): any {
    return this.#store;
  }

  set store(value: any) {
    this.#store = value;
  }

  constructor(config: Config<ComboBoxField> = null) {
    super((()=> ConfigUtils.apply(Config(ComboBoxField, {

      items: [
        Config(LocalComboBox, {
          fieldLabel: config.fieldLabel,
          emptyText: config.emptyText,
          store: config.store,
          editable: false,
          margin: "10 0 0 0",
          width: "100%",
          encodeItems: true,
          ...ConfigUtils.append({
            plugins: [
              Config(BindPropertyPlugin, {
                bindTo: this.getPropertyVE(config),
                skipIfUndefined: true,
                bidirectional: true,
                componentEvent: "select",
              }),
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
            ],
          }),
        }),
      ],

    }), config))());
  }
}

export default ComboBoxField;
