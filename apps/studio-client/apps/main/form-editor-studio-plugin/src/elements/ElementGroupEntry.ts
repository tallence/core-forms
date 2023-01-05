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
import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import BEMPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BEMPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import ColumnLayout from "@jangaroo/ext-ts/layout/container/Column";
import PanelHeader from "@jangaroo/ext-ts/panel/Header";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ElementGroupEntryBase from "./ElementGroupEntryBase";

interface ElementGroupEntryConfig extends Config<ElementGroupEntryBase>, Partial<Pick<ElementGroupEntry,
  "forceReadOnlyValueExpression" |
  "bindTo"
>> {
}

class ElementGroupEntry extends ElementGroupEntryBase {
  declare Config: ElementGroupEntryConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.selectBoxOption";

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #bindTo: ValueExpression = null;

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  constructor(config: Config<ElementGroupEntry> = null) {
    super((()=> ConfigUtils.apply(Config(ElementGroupEntry, {
      title: config.option.getId(),

      /*Using focusableContainer(cm9-17 style) and enableFocusableContainer(cm9-18 style) to be compatible with all cm9-versions*/
      header: Config(PanelHeader, {
        titlePosition: 2.0,
        focusableContainer: false,
        ...{ enableFocusableContainer: false },
        itemPosition: 2,
        ...ConfigUtils.append({
          plugins: [
            Config(BEMPlugin, {
              block: "core-forms-options-header",
              modifier: this.getPanelHeaderModifiers(config.option),
            }),
          ],
        }),
        items: [
          Config(IconButton, {
            iconAlign: "center",
            iconCls: "cm-core-icons cm-core-icons--arrow-up",
            itemId: "moveButtonUp",
            ariaLabel: FormEditor_properties.FormEditor_text_move_option_up,
            handler: bind(this, this.moveUp),
            plugins: [
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
            ],
          }),

          Config(IconButton, {
            iconAlign: "center",
            iconCls: "cm-core-icons cm-core-icons--arrow-down",
            itemId: "moveButtonDown",
            ariaLabel: FormEditor_properties.FormEditor_text_move_option_down,
            handler: bind(this, this.moveDown),
            plugins: [
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
            ],
          }),

          Config(IconButton, {
            iconAlign: "right",
            iconCls: Editor_properties["lifecycleStatus_checked-out_icon"],
            itemId: "editButton",
            ariaLabel: FormEditor_properties.FormEditor_text_edit_option,
            handler: bind(this, this.editOption),
            plugins: [
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
            ],
          }),

          Config(IconButton, {
            iconAlign: "right",
            iconCls: Editor_properties.lifecycleStatus_deleted_icon,
            itemId: "removeButton",
            ariaLabel: FormEditor_properties.FormEditor_text_delete_option,
            handler: bind(this, this.removeOption),
            plugins: [
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
            ],
          }),
        ],
      }),
      layout: Config(ColumnLayout),
    }), config))());
  }
}

export default ElementGroupEntry;
