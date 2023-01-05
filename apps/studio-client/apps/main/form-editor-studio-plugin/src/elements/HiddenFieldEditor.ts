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

import SvgIconUtil from "@coremedia/studio-client.base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/input-hidden.svg";
import AbstractFormElement from "./AbstractFormElement";

interface HiddenFieldEditorConfig extends Config<AbstractFormElement> {
}

class HiddenFieldEditor extends AbstractFormElement {
  declare Config: HiddenFieldEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.hiddenFieldEditor";

  static readonly FIELD_TYPE: string = "HiddenField";

  constructor(config: Config<HiddenFieldEditor> = null) {
    super(ConfigUtils.apply(Config(HiddenFieldEditor, {
      formElementType: HiddenFieldEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),
      formElementGroup: "input",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: FormEditor_properties.FormEditor_element_name_emptyText,
          propertyName: "name",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_hiddenField_value_label,
          emptyText: FormEditor_properties.FormEditor_element_hiddenField_value_emptyText,
          propertyName: "value",
        }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default HiddenFieldEditor;
