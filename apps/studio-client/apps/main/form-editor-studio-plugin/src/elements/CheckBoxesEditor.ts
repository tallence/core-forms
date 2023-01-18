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
import AddOptionField from "../fields/AddOptionField";
import CheckboxField from "../fields/CheckboxField";
import NumberField from "../fields/NumberField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/checkbox-group.svg";
import AbstractFormElement from "./AbstractFormElement";

interface CheckBoxesEditorConfig extends Config<AbstractFormElement> {
}

class CheckBoxesEditor extends AbstractFormElement {
  declare Config: CheckBoxesEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.checkBoxesEditor";

  static readonly FIELD_TYPE: string = "CheckBoxes";

  constructor(config: Config<CheckBoxesEditor> = null) {
    super(ConfigUtils.apply(Config(CheckBoxesEditor, {
      formElementType: CheckBoxesEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),
      formElementGroup: "multiple",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: FormEditor_properties.FormEditor_element_name_emptyText,
          propertyName: "name",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_hint_label,
          emptyText: FormEditor_properties.FormEditor_element_hint_emptyText,
          propertyName: "hint",
        }),
        Config(CheckboxField, {
          fieldLabel: FormEditor_properties.FormEditor_element_mandatory_label,
          propertyName: "validator.mandatory",
          defaultValue: false,
        }),
        Config(AddOptionField, {
          propertyName: "groupElements",
          allowMultiDefaultSelection: true,
        }),

        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_checkboxes_minSize_label,
          emptyText: FormEditor_properties.FormEditor_validator_checkboxes_minSize_text,
          propertyName: "validator.minSize",
          minValue: 0,
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_checkboxes_maxSize_label,
          emptyText: FormEditor_properties.FormEditor_validator_checkboxes_maxSize_text,
          propertyName: "validator.maxSize",
          minValue: 0,
        }),

        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default CheckBoxesEditor;
