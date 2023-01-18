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

import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Checkbox from "@jangaroo/ext-ts/form/field/Checkbox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import CheckboxFieldBase from "./CheckboxFieldBase";

interface CheckboxFieldConfig extends Config<CheckboxFieldBase> {
}

class CheckboxField extends CheckboxFieldBase {
  declare Config: CheckboxFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.checkboxField";

  constructor(config: Config<CheckboxField> = null) {
    super((()=> ConfigUtils.apply(Config(CheckboxField, {

      items: [
        Config(FieldContainer, {
          fieldLabel: config.fieldLabel,
          items: [
            Config(Checkbox, {
              boxLabel: ConfigUtils.asString(config.boxLabel ? config.boxLabel : ""),
              plugins: [
                /* a checkbox cannot be set to undefined, so skipIfUndefined is set to true */
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  skipIfUndefined: true,
                  bindTo: this.getPropertyVE(config),
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

    }), config))());
  }
}

export default CheckboxField;
