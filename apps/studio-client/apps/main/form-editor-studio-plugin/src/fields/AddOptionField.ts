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

import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindComponentsPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindComponentsPlugin";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import Container from "@jangaroo/ext-ts/container/Container";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import {bind} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ElementGroupEntry from "../elements/ElementGroupEntry";
import ShowFormIssuesPlugin from "../plugins/ShowFormIssuesPlugin";
import AddOptionFieldBase from "./AddOptionFieldBase";
import CoreIcons_properties from "@coremedia/studio-client.core-icons/CoreIcons_properties";
import FormUtils from "../FormUtils";

interface AddOptionFieldConfig extends Config<AddOptionFieldBase> {
}

class AddOptionField extends AddOptionFieldBase {
  declare Config: AddOptionFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.addOptionField";

  constructor(config: Config<AddOptionField> = null) {
    super((()=> ConfigUtils.apply(Config(AddOptionField, {

      items: [

        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_text_label_option,
          items: [

            Config(Container, {
              items: [
                Config(StatefulTextField, {
                  flex: 1,
                  minWidth: 150,
                  emptyText: FormEditor_properties.FormEditor_text_add_option,
                  plugins: [
                    Config(BindDisablePlugin, {
                      bindTo: config.bindTo,
                      forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                    }),
                    Config(BindPropertyPlugin, {
                      bidirectional: true,
                      skipIfUndefined: true,
                      bindTo: this.getAddOptionVE(),
                    }),
                    Config(ShowFormIssuesPlugin, {
                      issuesVE: config.formIssuesVE,
                      propertyName: config.propertyName,
                      propertyPathVE: config.propertyPathVE,
                    }),
                  ],
                }),
                Config(IconButton, {
                  iconCls: CoreIcons_properties.add,
                  ariaLabel: FormEditor_properties.FormEditor_text_add_option,
                  handler: bind(this, this.addGroupElement),
                  plugins: [
                    Config(BindDisablePlugin, {
                      bindTo: config.bindTo,
                      forceReadOnlyValueExpression: this.getAddOptionButtonDisabledVE(),
                    }),
                    Config(BindPropertyPlugin, {
                      componentProperty: "tooltip",
                      transformer: function(disabled:Boolean):String { return FormUtils.getOptionRemoveButtonToolTip(disabled); },
                      bindTo: this.getAddOptionButtonDisabledVE(),
                    }),
                  ],
                }),
              ],
              layout: Config(HBoxLayout),
            }),

          ],

        }),

        Config(Container, {
          width: "100%",
          padding: "0 0 0 105",
          plugins: [
            Config(BindComponentsPlugin, {
              configBeanParameterName: "option",
              valueExpression: this.getGroupElementsVE(config),
              template: Config(ElementGroupEntry, {
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                bindTo: config.bindTo,
                removeGroupElementFn: bind(this, this.removeGroupElement),
                updateOptionElementFn: bind(this, this.updateGroupElement),
                moveOptionElementFn: bind(this, this.moveGroupElement),
              }),
            }),
          ],
        }),

      ],
    }), config))());
  }
}

export default AddOptionField;
